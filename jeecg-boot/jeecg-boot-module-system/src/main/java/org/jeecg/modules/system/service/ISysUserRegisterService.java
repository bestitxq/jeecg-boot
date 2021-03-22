package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserRegister;

import java.util.Date;

/**
 * @Description: 用户注册信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-10
 * @Version: V1.0
 */
public interface ISysUserRegisterService extends IService<SysUserRegister> {

    /**
     * @description: 添加邀请注册记录，并检测符合奖励条件的奖励vip
     * @param invitecode: 注册邀请码
     * @param regMode: 注册方式 微信，qq.....
     * @param userId: 被邀请人id, 即本次注册成功的用户
     * @param nowDate: 注册时间（避免二次new，引用即可）
     * @author: bestitxq
     * @date: 2021/3/10 上午10:15
     */
    void addUserRegister(String invitecode, String regMode, String userId, Date nowDate);
}
