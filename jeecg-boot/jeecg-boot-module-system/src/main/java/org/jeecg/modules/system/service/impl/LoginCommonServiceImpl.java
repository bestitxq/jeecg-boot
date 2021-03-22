package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.ILoginCommonService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.CaptchaUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 登录通用接口实现
 * @author: bestitxq
 * @date: 2021/3/17 下午3:02
 */
@Service
public class LoginCommonServiceImpl implements ILoginCommonService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CaptchaUtils captchaUtils;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;
    @Resource
    private BaseCommonService baseCommonService;

    /**
     * @description: 登录通用
     * @param sysLoginModel: 登录封装信息
     * @param captchaFlag: 验证码是否开启校验
     * @author: bestitxq
     * @date: 2021/3/5 上午11:51
     */
    public Result loginUser(SysLoginModel sysLoginModel, boolean captchaFlag){
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        String checkKey = sysLoginModel.getCheckKey();
        String captcha = sysLoginModel.getCaptcha();
        // 1.校验验证码是否合格
        Result result = captchaUtils.isTxCaptchaOk(checkKey, captcha, captchaFlag);
        if(result.isFail()){
            return result;
        }
        // 2.通过用户名校验用户是否合格, 合格->返回用户基本信息
        result = sysUserService.checkUserIsEffectiveBy(null, username);
        if(result.isFail()) {
            return result;
        }
        SysUser sysUser = (SysUser) result.getResult();

        // 3. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }

        // 缓存用户登录信息
        result = this.loginUserCache(sysUser, result);
        // 记录前台登录日志
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(sysUser, loginUser);
        baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);
        return result;
    }


    @Override
    public Result<JSONObject> loginUserCache(SysUser sysUser, Result<JSONObject> result) {
        String username = sysUser.getUsername();
        // 生成token
        String token = this.saveToken(sysUser);
        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            //查询当前是否有登录部门
            // update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            SysUser sysUserById = sysUserService.getById(sysUser.getId());
            if(oConvertUtils.isEmpty(sysUserById.getOrgCode())){
                sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            }
            // update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
            obj.put("multi_depart", 2);
        }
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    @Override
    public String saveToken(SysUser user) {
        // 生成token
        String token = JwtUtil.sign(user.getUsername(), user.getPassword());
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        // 设置超时时间
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);
        return token;
    }

}
