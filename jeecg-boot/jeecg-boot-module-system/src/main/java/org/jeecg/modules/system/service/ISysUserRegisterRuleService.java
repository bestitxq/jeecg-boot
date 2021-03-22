package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserRegisterRule;

import java.util.List;

/**
 * @Description: 用户邀请注册规则表
 * @Author: jeecg-boot
 * @Date:   2021-03-10
 * @Version: V1.0
 */
public interface ISysUserRegisterRuleService extends IService<SysUserRegisterRule> {

    /**
     * @description: 计算规则 人数与奖励天数累计
     * @author: bestitxq
     * @date: 2021/3/11 上午10:04
     */
    List<SysUserRegisterRule> sumVipRule();
}
