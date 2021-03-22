package org.jeecg.common.constant.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 存储配置类字典常量
 * @author: bestitxq
 * @date: 2021/3/10 上午9:05
 */
@Getter
@Component
public class ConfigConstant {

    // 前端域名配置（1.邀请注册注册url生成） TODO bestitxq 后期localhost可通过配置文件灵活配置域名，
    public static final String BEFORE_DOMAIN_NAME  = "127.0.0.1:3001";
    // 邀请注册---url
    public static final String INVITE_URL  = "http://{}/register?invitecode={}";
    // 邀请注册---redis中存储邀请码有效期30天 单位 秒
    public static final Integer INVITE_CODE_CACHE_TIME = 60 * 60 * 24 * 30;
    // 邀请注册---数据库中邀请码有效期365天 单位 天
    public static final Integer INVITE_CODE_MYSQL_TIME = 365;
    // 后台注册---后台注册用户默认角色
    public static final String DEFAULT_ADMIN_ROLE = "ee8626f80f7c2619917b6236f3a7f02b";
    // 前台注册---前台商城注册用户默认角色
    public static final String DEFAULT_SHOP_ROLE = "1371352829243789314";
    // 前台验证码开关
    public static boolean ISRECEPTION_CAPTCHA;
    // 后台验证码开关
    public static boolean ISADMIN_CAPTCHA;

    @Value(value = "${jeecg.isReceptionCaptcha}")
    public static void setIsReceptionCaptcha(boolean isReceptionCaptcha) {
        ConfigConstant.ISRECEPTION_CAPTCHA = isReceptionCaptcha;
    }
    @Value(value = "${jeecg.isAdminCaptcha}")
    public static void setIsAdminCaptcha(boolean isAdminCaptcha) {
        ConfigConstant.ISADMIN_CAPTCHA = isAdminCaptcha;
    }
}
