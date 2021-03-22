package org.jeecg.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;

/**
 * @description: 登录通用接口
 * @author: bestitxq
 * @date: 2021/3/17 下午3:01
 */
public interface ILoginCommonService {

    /**
     * @description: 登录通用
     * @param sysLoginModel: 登录数据封装
     * @param isadminCaptcha: 验证码是否开启
     * @author: bestitxq
     * @date: 2021/3/17 下午3:04
     */
    Result loginUser(SysLoginModel sysLoginModel, boolean isadminCaptcha);

    /**
     * 登录用户缓存用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    Result<JSONObject> loginUserCache(SysUser sysUser, Result<JSONObject> result);

    /**
     * @description: 生成token: 根据用户的用户名密码生成jwtToken
     * @param user: 用户信息
     * @author: bestitxq
     * @date: 2021/3/17 下午3:11
     */
    String saveToken(SysUser user);

}
