package org.jeecg.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.DictConstant;
import org.jeecg.common.constant.config.ConfigConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysUserRegisterMapper;
import org.jeecg.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 用户注册信息表
 * @Author: jeecg-boot
 * @Date: 2021-03-10
 * @Version: V1.0
 */
@Service
public class SysUserRegisterServiceImpl extends ServiceImpl<SysUserRegisterMapper, SysUserRegister> implements ISysUserRegisterService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserRegisterService userRegisterService;
    @Autowired
    private ISysUserRegisterRuleService userRegisterRuleService;
    @Autowired
    private ISysUserMemberService userMemberService;
    @Autowired
    private ISysUserMemberRecordService userMemberRecordService;
    @Autowired
    private ISysUserRegisterCodeService userRegisterCodeService;

    @Override
    @Transactional
    public void addUserRegister(String invitecode, String regMode, String byUserId, Date nowDate) {

        // -------------------------------1.邀请码校验start-------------------------------------
        // 邀请码为空，不添加奖励信息
        if(StringUtils.isBlank(invitecode)){
            return;
        }
        Object codeCache = redisUtil.get(invitecode);

        // 邀请码redis中有效期 30天，到期后数据库若存在有效邀请码则刷新缓存30天
        if (ObjectUtil.isNull(codeCache)) {
            LambdaQueryWrapper<SysUserRegisterCode> qw = Wrappers.lambdaQuery();
            qw.eq(SysUserRegisterCode::getCode, invitecode);
            List<SysUserRegisterCode> codeList = userRegisterCodeService.list(qw);
            // 如果本邀请码不存在于我们平台，或已过期，则不添加奖励信息
            if(CollectionUtil.isEmpty(codeList) ||
                    !DateUtil.isIn(nowDate, codeList.get(0).getStartTime(), codeList.get(0).getEndTime() )){
                return;
            }
            redisUtil.set(invitecode, codeList.get(0), ConfigConstant.INVITE_CODE_CACHE_TIME);
        }
        // 邀请码自身过期，删除redis中信息
        SysUserRegisterCode regcode = JSONObject.parseObject(codeCache.toString(), SysUserRegisterCode.class);
        if(!DateUtil.isIn(nowDate, regcode.getStartTime(), regcode.getEndTime())){
            redisUtil.remove(invitecode);
            return;
        }
        // -------------------------------1.end-----------------------------------------

        // -------------------------------2.用户及注册规则校验start--------------------------------------
        String userId = regcode.getUserId();

        LambdaQueryWrapper<SysUser> userqw = Wrappers.lambdaQuery();
        userqw.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
        userqw.eq(SysUser::getId, userId);
        SysUser user = userService.getOne(userqw);
        // 正常状态邀请人若不存在，则不添加他的邀请注册信息
        if (ObjectUtil.isNull(user)) return;

        LambdaQueryWrapper<SysUserRegisterRule> regRuleQw = Wrappers.lambdaQuery();
        regRuleQw.eq(SysUserRegisterRule::getStatus, DictConstant.YN_YES);
        regRuleQw.orderByAsc(SysUserRegisterRule::getExecuteSort);
        List<SysUserRegisterRule> ruleList = userRegisterRuleService.list(regRuleQw);
        // 如果奖励规则不存在，则不添加注册信息
        if (CollectionUtil.isEmpty(ruleList)) return;
        // -------------------------------2.end--------------------------------------

        // -------------------------------3.获取当前规则及下次执行的规则start--------------------------------------
        // 用户第一次邀请注册,执行规则为执行顺序最小的规则id
        if (StringUtils.isBlank(user.getRegRuleId())) {
            user.setRegRuleId(ruleList.get(0).getId());
            userService.updateById(user);
        }

        // 获取当前规则
        SysUserRegisterRule nowRule = null;
        // 获取下一次执行的规则
        SysUserRegisterRule nextRule = null;
        for (int i = 0; i < ruleList.size(); i++) {
            SysUserRegisterRule rule = ruleList.get(i);
            if (rule.getId().equals(user.getRegRuleId())) {
                nowRule = rule;
                // 如果下一次执行的规则存在，则保存下来，不存在，则开始新的一轮规则循环
                if (i + 1 < ruleList.size()) {
                    nextRule = ruleList.get(i + 1);
                }
            }
        }

        // 用户执行完毕奖励规则，又检测到了管理员添加了新的奖励规则，则更新用户当前执行规则rule_id，且修改当前奖励未下发
        if(DictConstant.YN_YES.equals(user.getRegRuleComplete()) && ObjectUtil.isNotNull(nextRule)){
            nowRule = nextRule;
            user.setRegRuleId(nextRule.getId());
            user.setRegRuleComplete(DictConstant.YN_NO);
            userService.updateById(user);
        }
        // -------------------------------3.end--------------------------------------

        // -------------------------------4.用户注册记录存储start--------------------------------------
        SysUserRegister register = new SysUserRegister();
        // 邀请人id
        register.setUserId(userId);
        // 被邀请人id
        register.setByUserId(byUserId);
        // 注册方式  例，微信扫码，QQ扫码，手机号注册
        register.setRegisterMode(regMode);
        // 注册类型 受邀请人邀请注册
        register.setRegisterType(DictConstant.REGISTER_TYPE_INVITE);
        // 注册时间 当前时间
        register.setRegisterTime(nowDate);
        // 保存记录
        userRegisterService.save(register);
        // -------------------------------4.end--------------------------------------

        // ------------5.满足条件vip发放奖励，旧注册记录标记发放完成，开始新一轮vip奖励 start---------------
        LambdaQueryWrapper<SysUserRegister> qw = Wrappers.lambdaQuery();
        qw.eq(SysUserRegister::getUserId, userId);
        int count = this.count(qw);
        // 已注册人数满足用户当前邀请规则， 发放vip，更新用户下一邀请规则
        if (count == nowRule.getUserNum() && !DictConstant.YN_YES.equals(user.getRegRuleComplete())) {
            // 发放vip天数
            this.grantVip(nowRule, user, nowDate);
            // 当前规则奖励已下发
            user.setRegRuleComplete(DictConstant.YN_YES);
            userService.updateById(user);
        }
        // -------------------------------5.end--------------------------------------

    }

    /**
     * @description: 邀请人奖励发放
     * @param nowRule:  发放奖励的规则信息
     * @param user:    邀请人
     * @param nowDate: 当前时间
     * @author: bestitxq
     * @date: 2021/3/10 下午3:46
     */
    private void grantVip(SysUserRegisterRule nowRule, SysUser user, Date nowDate) {
        // ------------------- 1.存储会员奖励发放记录 -------------------
        SysUserMemberRecord memberRecord = new SysUserMemberRecord();
        memberRecord.setMemberDay(nowRule.getVipDay());
        memberRecord.setRuleId(nowRule.getId());
        memberRecord.setUserId(user.getId());
        memberRecord.setUserId(user.getId());
        memberRecord.setStatus(DictConstant.YN_YES);
        userMemberRecordService.save(memberRecord);

        // ------------------- 2.存储或更新用户会员信息 --------------------
        // 用户会员是否早已存在
        LambdaQueryWrapper<SysUserMember> qw = Wrappers.lambdaQuery();
        qw.eq(SysUserMember::getUserId, user.getId());
        List<SysUserMember> memberList = userMemberService.list(qw);
        SysUserMember hasMember = CollectionUtil.isNotEmpty(memberList) ? memberList.get(0) : null;

        // 用户会员存在，更新会员有效期
        if (ObjectUtil.isNotNull(hasMember)) {

            // 当前会员未过期，则延长会员日
            if (DateUtil.isIn(nowDate, hasMember.getStartTime(), hasMember.getEndTime())) {
                // 未过期会员的结束日期：结束日期 = 会员开始日期 + 偏移天数
                hasMember.setEndTime(DateUtil.offsetDay(hasMember.getStartTime(), nowRule.getVipDay()));
            } else {
                // 当前会员已过期，则重新设置开始结束会员日期
                hasMember.setStartTime(nowDate);
                // 已过期会员结束日期：结束日期 = 当前日期 + 偏移天数
                hasMember.setEndTime(DateUtil.offsetDay(nowDate, nowRule.getVipDay()));
            }
            userMemberService.updateById(hasMember);
            return;
        }

        // 用户会员不存在，则存储会员信息
        SysUserMember userMember = new SysUserMember();
        // 会员开始日期：当前时间为记录发放日期
        userMember.setStartTime(nowDate);
        userMember.setEndTime(DateUtil.offsetDay(nowDate, nowRule.getVipDay()));
        userMember.setUserId(user.getId());
        // 启用会员
        userMember.setStatus(DictConstant.YN_YES);
        userMemberService.save(userMember);
    }

}
