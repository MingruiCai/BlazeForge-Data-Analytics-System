package com.bcsd.project.sms;

import cn.com.flaginfo.sdk.cmc.api.ApiProvider;
import cn.com.flaginfo.sdk.cmc.api.request.ApiConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 短信帮助类
 * @author bcsd
 */
@Component
public class SmsUtils {

    //企业编号
    @Value("${sms.spCode}")
    private String spCode;
    //用户名称
    @Value("${sms.appKey}")
    private String appKey;
    //秘钥
    @Value("${sms.appSecret}")
    private String appSecret;

    /**
     * 短信模板
     */
    public static ApiProvider apiProvider = null;

    /**
     * 初始化短信配置
     */
    @PostConstruct
    private void initSmsConfig(){
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setSpCode(spCode);
        apiConfig.setAppKey(appKey);
        apiConfig.setAppSecret(appSecret);
        apiProvider = ApiProvider.getInstance(apiConfig);
    }

}
