package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.DictConstant;
import org.jeecg.modules.system.entity.SysUserRegisterRule;
import org.jeecg.modules.system.mapper.SysUserRegisterRuleMapper;
import org.jeecg.modules.system.service.ISysUserRegisterRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 用户邀请注册规则表
 * @Author: jeecg-boot
 * @Date:   2021-03-10
 * @Version: V1.0
 */
@Service
public class SysUserRegisterRuleServiceImpl extends ServiceImpl<SysUserRegisterRuleMapper, SysUserRegisterRule> implements ISysUserRegisterRuleService {

    @Autowired
    private SysUserRegisterRuleMapper userRegisterRuleMapper;
    @Override
    public List<SysUserRegisterRule> sumVipRule() {
        LambdaQueryWrapper<SysUserRegisterRule> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUserRegisterRule::getStatus, DictConstant.YN_YES);
        queryWrapper.orderByAsc(SysUserRegisterRule::getExecuteSort);
        List<SysUserRegisterRule> ruleList = this.list(queryWrapper);
        return ruleList;
    }

}
