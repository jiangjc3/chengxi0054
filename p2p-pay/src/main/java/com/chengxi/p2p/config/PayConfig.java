package com.chengxi.p2p.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"pay-config.properties"})
@Data
public class PayConfig {

    /**
     * 支付宝支付网关
     */
    @Value("${alipayGatewayUrl}")
    private String alipayGatewayUrl;
    /**
     * 应用ID
     */
    @Value("${alipayAppId}")
    private String alipayAppId;
    /**
     * 应用私钥
     */
    @Value("${alipayPrivateKey}")
    private String alipayPrivateKey;
    /**
     * 格式
     */
    @Value("${alipayFormat}")
    private String alipayFormat;
    /**
     * 字符集
     */
    @Value("${alipayCharset}")
    private String alipayCharset;
    /**
     * 支付宝公钥：由支付宝系统通过应用公钥生成
     */
    @Value("${alipayPublicKey}")
    private String alipayPublicKey;
    /**
     * 签名类型：推荐使用RSA2
     */
    @Value("${alipaySignType}")
    private String alipaySignType;

    /**
     * 同步返回地址
     */
    @Value("${alipayReturnUrl}")
    private String alipayReturnUrl;

    /**
     * 异步返回地址
     */
    @Value("${alipayNotifyUrl}")
    private String alipayNotifyUrl;

    /**
     * 支付完成返回地址
     */
    @Value("${alipayP2pReturnUrl}")
    private String alipayP2pReturnUrl;

    /**
     * 异步返回地址(必须是公网可以访问的地址)
     */
    @Value("${wxpay_notify_url}")
    private String wxPayNotifyUrl;

    /**
     * 公众账号ID
     */
    @Value("${wxpay_appid}")
    private String wxpayAppId;

    /**
     * 微信支付秘钥
     */
    @Value("${wxpay_key}")
    private String wxpayKey;

    /**
     * 商户号
     */
    @Value("${wxpay_mch_id}")
    private String wxpayMchId;

    /**
     * 微信支付url
     */
    @Value("${wxpay_url}")
    private String wxpayUrl;

    /**
     * 交易类型
     */
    @Value("${wxpay_trade_type}")
    private String wxpayTradeType;


}
