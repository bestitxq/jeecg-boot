package org.jeecg.common.constant.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @description: 微信配置类
 * @author: bestitxq
 * @date: 2021/3/16 上午9:00
 */
@Component
@Getter
public class WxConfigConstant {

    // 二维码扫码链接
    public static String qrConnectUrl = "https://open.weixin.qq.com/connect/qrconnect?appid={}&redirect_uri={}&response_type=code&scope=snsapi_login&state={}#wechat_redirect";
    // 二维码扫码链接回调地址
    public static String redirectUrl = "https://{}/wx/qrcodeLoginByWx";
    // 通过code换取access_token
    public static String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code";
    // 通过access_token与open_id换取用户信息
    public static String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token={}&openid={}&lang=zh_CN";
    // 网站应用appid
    public static String appID = "wxefb5c361e9434e3d";
    // 网站应用appsecret
    public static String appsecret = "53197519caaa5cfe7eefea5758dee1b8";
}
