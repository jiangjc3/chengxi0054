package com.chengxi.p2p.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author CHENGXI
 * @date 2019/8/3
 */
@Configuration
@PropertySource(value = {"common.properties"})
@Data
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

    /**
     * 调用支付宝支付地址
     */
    @Value("${alipay_url}")
    private String alipayUrl;

    /**
     * 查询支付订单
     */
    @Value("${query_order}")
    private String queryOrder;

    /**
     * 调用微信支付生成二维码
     */
    @Value("${wxpay_url}")
    private String wxpayUrl;


}
