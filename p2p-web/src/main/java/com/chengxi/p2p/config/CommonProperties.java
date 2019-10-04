package com.chengxi.p2p.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author CHENGXI
 * @date 2019/8/3
 */
@Configuration
@PropertySource(value = {"common.properties"})
public class CommonProperties {

    /**
     * 实名认证的key
     */
    @Value("${realName_appkey}")
    private String realNameAppkey;
    /**
     * 实名认证的URL
     */
    @Value("${realName_url}")
    private String realNameUrl;

    public String getRealNameAppkey() {
        return realNameAppkey;
    }

    public void setRealNameAppkey(String realNameAppkey) {
        this.realNameAppkey = realNameAppkey;
    }

    public String getRealNameUrl() {
        return realNameUrl;
    }

    public void setRealNameUrl(String realNameUrl) {
        this.realNameUrl = realNameUrl;
    }
}
