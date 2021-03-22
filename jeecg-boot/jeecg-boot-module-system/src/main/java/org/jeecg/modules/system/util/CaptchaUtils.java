package org.jeecg.modules.system.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 验证码工具类
 * @author: bestitxq
 * @date: 2021/3/15 下午5:46
 */
@Component
public class CaptchaUtils {

    @Autowired
    private RedisUtil redisUtil;
    /**
     * @description: 验证码校验 (图形验证码)
     * @param checkKey: 验证码key
     * @param captcha: 验证码
     * @param captchaFlag: 验证码是否开启校验
     * @author: bestitxq
     * @date: 2021/3/5 上午11:52
     */
    public Result isTxCaptchaOk(String checkKey, String captcha, boolean captchaFlag) {
        Result<JSONObject> result = new Result();
        // 验证码开启后才去做校验
        if(captchaFlag){
            if(StringUtils.isBlank(captcha)){
                return result.error500("验证码无效");
            }
            String lowerCaseCaptcha = captcha.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+checkKey, "utf-8");
            Object checkCode = redisUtil.get(realKey);
            //当进入登录页时，有一定几率出现验证码错误 #1714
            if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
                return result.error500("验证码错误");
            }
        }
        // 验证码正确或并未开启验证码开关
        return result.success("验证码正确");
    }

    /**
     * @description: 验证码校验 (手机验证码)
     * @param phone: 手机号
     * @param captcha: 验证码
     * @param captchaFlag: 验证码是否开启校验
     * @author: bestitxq
     * @date: 2021/3/5 上午11:52
     */
    public Result isSjCaptchaOk(String phone, String captcha, boolean captchaFlag) {
        Result<JSONObject> result = new Result();
        // 验证码开启后才去做校验
        if(captchaFlag){
            if(StringUtils.isBlank(captcha)){
                return result.error500("验证码无效");
            }
            String lowerCaseCaptcha = captcha.toLowerCase();
            Object checkCode = redisUtil.get(phone);
            //当进入登录页时，有一定几率出现验证码错误 #1714
            if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
                return result.error500("验证码错误");
            }
        }
        // 验证码正确或并未开启验证码开关
        return result.success("验证码正确");
    }

}
