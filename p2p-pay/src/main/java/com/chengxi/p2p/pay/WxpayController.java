package com.chengxi.p2p.pay;


import com.alibaba.fastjson.JSON;
import com.chengxi.p2p.config.PayConfig;
import com.chengxi.p2p.utils.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WxpayController {
    @Autowired
    private PayConfig payConfig;

    private static final Logger logger = LoggerFactory.getLogger(WxpayController.class);

    @RequestMapping(value = "/api/wxpay")
    public @ResponseBody
    Object wxpay(HttpServletRequest request,
                 @RequestParam(value = "body", required = true) String body,
                 @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                 @RequestParam(value = "total_fee", required = true) String total_fee) throws Exception {
        logger.info("total_fee is: {}", total_fee);
        //创建一个请求参数Map
        Map<String, String> requestDataMap = new HashMap<>();
        requestDataMap.put("appid", payConfig.getWxpayAppId());
        // 商户号
        requestDataMap.put("mch_id", payConfig.getWxpayMchId());
        // 随机字符串
        requestDataMap.put("nonce_str", WXPayUtil.generateNonceStr());
        // 商品描述
        requestDataMap.put("body", body);
        // 商户订单号
        requestDataMap.put("out_trade_no", out_trade_no);
        requestDataMap.put("total_fee", total_fee);
        // 终端IP
        requestDataMap.put("spbill_create_ip", "127.0.0.1");
        requestDataMap.put("notify_url", payConfig.getWxPayNotifyUrl());
        requestDataMap.put("trade_type", payConfig.getWxpayTradeType());
        requestDataMap.put("product_id", out_trade_no);
        //生成签名值
        String signature = WXPayUtil.generateSignature(requestDataMap, payConfig.getWxpayKey());
        requestDataMap.put("sign", signature);
        logger.info("请求参数封装map: {}", JSON.toJSON(requestDataMap));

        //xml格式的请求上传参数
        String requestDataXml = WXPayUtil.mapToXml(requestDataMap);

        //调用微信统一下单API接口 -> 返回xml格式的字符串
        String responseDataXml = HttpClientUtils.doPostByXml(payConfig.getWxpayUrl(), requestDataXml);

        //将响应的xml格式的字符串转换为Map集合
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);

        return responseDataMap;
    }

    @RequestMapping("/api/wxPayBack")
    public @ResponseBody
    Object alipayQuery(HttpServletRequest request,
                       @RequestParam(value = "out_trade_no", required = true) String out_trade_no) throws Exception {
        Map paraMap = new HashMap();
        paraMap.put("appid", payConfig.getWxpayAppId());
        paraMap.put("mch_id", payConfig.getWxpayMchId());
        paraMap.put("out_trade_no", out_trade_no);
        paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //生成签名值
        String signature = WXPayUtil.generateSignature(paraMap, payConfig.getWxpayKey());
        paraMap.put("sign", signature);
        //xml格式的请求上传参数
        String requestDataXml = WXPayUtil.mapToXml(paraMap);
        logger.info("查询微信支付状态paraMap = {}", requestDataXml);
        System.out.println(payConfig.getWxpayBack());

        String responseDataXml = HttpClientUtils.doPostByXml(payConfig.getWxpayBack(), requestDataXml);
        logger.info("查询微信支付状态responseDataXml = {}", responseDataXml);
        //将响应的xml格式的字符串转换为Map集合
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);
        System.out.println(JSON.toJSON(responseDataMap));

        return responseDataMap;
    }
}
