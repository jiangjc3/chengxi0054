package com.chengxi.p2p.controller;

import com.alibaba.fastjson.JSONObject;
import com.chengxi.p2p.config.CommonProperties;
import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.model.loan.RechargeRecord;
import com.chengxi.p2p.model.user.User;
import com.chengxi.p2p.model.vo.PaginatinoVO;
import com.chengxi.p2p.service.loan.RechargeRecordService;
import com.chengxi.p2p.service.loan.UniqueNumberService;
import com.chengxi.p2p.utils.DateUtils;
import com.chengxi.p2p.utils.HttpClientUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/19
 */
@Controller
public class RechargeRecordController {
    @Reference
    private RechargeRecordService rechargeRecordService;

    @Reference
    private UniqueNumberService uniqueNumberService;

    @Autowired
    private CommonProperties commonProperties;

    private static final Logger logger = LoggerFactory.getLogger(RechargeRecordController.class);

    @RequestMapping(value = "/loan/myRecharge")
    public String myRecharge(HttpServletRequest request, Model model,
                             @RequestParam(value = "currentPage", required = false) Integer currentPage) {

        //判断是否为第1页
        if (null == currentPage) {
            currentPage = 1;
        }
        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);

        //准备分页查询参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uid", sessionUser.getId());
        int pageSize = 10;
        paramMap.put("currentPage", (currentPage - 1) * pageSize);
        paramMap.put("pageSize", pageSize);

        //根据用户标识分页查询投资记录（用户标识，页码，每页显示条数） -> 返回PaginationVO
        PaginatinoVO<RechargeRecord> paginationVO = rechargeRecordService.queryRechargeRecordByPage(paramMap);

        //计算总页数
        int totalPage = paginationVO.getTotal().intValue() / pageSize;
        int mod = paginationVO.getTotal().intValue() % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }

        model.addAttribute("totalRows", paginationVO.getTotal());
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("rechargeRecordList", paginationVO.getDataList());
        model.addAttribute("currentPage", currentPage);

        return "myRecharge";
    }

    @RequestMapping(value = "/loan/toAlipayRecharge")
    public String toAlipayRecharge(HttpServletRequest request, Model model,
                                   @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {

        logger.info("================toAlipay==================");

        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);

        //生成一个全局唯一的充值订单号 = 时间戳 + redis全局唯一数字
        String rechargeNo = DateUtils.getTimeStamp() + uniqueNumberService.getUniqueNumber();

        //生成充值记录
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeStatus("0");//0充值中，1充值成功，2充值失败
        rechargeRecord.setRechargeDesc("支付宝充值");

        int addRechargeCount = rechargeRecordService.addRechargeRecord(rechargeRecord);

        if (addRechargeCount > 0) {
            model.addAttribute("p2p_pay_alipay_url", commonProperties.getAlipayUrl());
            model.addAttribute("rechargeNo", rechargeNo);
            model.addAttribute("rechargeMoney", rechargeMoney);
            model.addAttribute("subject", "支付宝充值");
            model.addAttribute("body", "支付宝充值");
        } else {
            model.addAttribute("trade_msg", "充值失败");
            return "toRechargeBack";
        }
        return "toAlipay";
    }

    @RequestMapping(value = "/loan/toWxpayRecharge")
    public String toWxpayRecharge(HttpServletRequest request, Model model,
                                  @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {
        logger.info("================toWxpay==================");

        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);

        //生成一个全局唯一的充值订单号 = 时间戳 + redis全局唯一数字
        String rechargeNo = DateUtils.getTimeStamp() + uniqueNumberService.getUniqueNumber();

        //生成充值记录
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeStatus("0");//0充值中，1充值成功，2充值失败
        rechargeRecord.setRechargeDesc("微信充值");

        int addRechargeCount = rechargeRecordService.addRechargeRecord(rechargeRecord);

        if (addRechargeCount > 0) {
            model.addAttribute("rechargeNo", rechargeNo);
            model.addAttribute("rechargeMoney", rechargeMoney);
            model.addAttribute("rechargeTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } else {
            model.addAttribute("trade_msg", "充值失败");
            return "toRechargeBack";
        }
        return "showQRCode";
    }

    @RequestMapping(value = "/loan/alipayBack")
    public String alipayBack(HttpServletRequest request, Model model,
                             @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                             @RequestParam(value = "total_amount", required = true) Double total_amount,
                             @RequestParam(value = "signVerified", required = true) String signVerified) {
        logger.info("================支付宝支付回调查询支付结果==================");

        //判断验证结果
        if (StringUtils.equals(BizConstant.SUCCESS, signVerified)) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("out_trade_no", out_trade_no);
            //调用pay工程的订单查询接口 -> 响应JSON格式的字符串
            String jsonResult = HttpClientUtils.doPost(commonProperties.getQueryOrder(), paramMap);
            logger.info("订单查询返回结果为：[{}]", jsonResult);
            //解析json格式字符串
            //将json格式的字符串转换为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(jsonResult);
            //获取指定key的value
            JSONObject tradeQueryResponse = jsonObject.getJSONObject("alipay_trade_query_response");
            //获取通信标识
            String code = tradeQueryResponse.getString("code");

            //判断通信是否成功
            if (StringUtils.equals("10000", code)) {
                //获取订单处理结果
                String trade_status = tradeQueryResponse.getString("trade_status");

                /*WAIT_BUYER_PAY	交易创建，等待买家付款
                TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款
                TRADE_SUCCESS	交易支付成功
                TRADE_FINISHED	交易结束，不可退款*/

                if ("TRADE_CLOSED".equals(trade_status)) {
                    //更新充值记录状态为2充值失败
                    RechargeRecord updateRecharge = new RechargeRecord();
                    updateRecharge.setRechargeNo(out_trade_no);
                    updateRecharge.setRechargeStatus("2");
                    int updateRechargeRecordCount = rechargeRecordService.modifyRechargeRecordByRechargeNo(updateRecharge);

                    if (updateRechargeRecordCount <= 0) {
                        model.addAttribute("trade_msg", "通信繁忙");
                        return "toRechargeBack";
                    }
                }
                if ("TRADE_SUCCESS".equals(trade_status)) {
                    //从session中获取用户信息
                    User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);
                    //准备充值参数
                    paramMap.put("uid", sessionUser.getId());
                    paramMap.put("rechargeMoney", total_amount);
                    paramMap.put("rechargeNo", out_trade_no);

                    //给用户充值（用户标识，充值金额,充值订单号）【给用户帐户余额加钱，更新充值记录状态为1】
                    int rechargeCount = rechargeRecordService.recharge(paramMap);
                    if (rechargeCount <= 0) {
                        model.addAttribute("trade_msg", "通信繁忙");
                        return "toRechargeBack";
                    }
                }
            } else {
                model.addAttribute("trade_msg", "通信繁忙");
                return "toRechargeBack";
            }
        } else {
            model.addAttribute("trade_msg", "验证签名失败");
            return "toRechargeBack";
        }
        return "redirect:/loan/myRecharge";
    }

    @RequestMapping(value = "/loan/generateQRCode")
    public void generateQRCode(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "rechargeNo", required = true) String rechargeNo,
                               @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) throws IOException, WriterException, WriterException {
        logger.info("================开始准备调用微信支付接口==================");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("body", "微信支付");
        paramMap.put("out_trade_no", rechargeNo);
        BigDecimal total_fee = new BigDecimal(rechargeMoney).multiply(new BigDecimal(100));
        paramMap.put("total_fee", String.valueOf(total_fee.intValue()));

        //调用pay统一下单API接口 -> 返回参数，参数中包含code_url
        String jsonResult = HttpClientUtils.doPost(commonProperties.getWxpayUrl(), paramMap);
        logger.info("调用微信支付接口返回值为： [{}]", jsonResult);
        //解析json格式的字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonResult);

        //获取通信标识
        String return_code = jsonObject.getString("return_code");

        if (StringUtils.equals(BizConstant.SUCCESS, return_code)) {
            //获取业务处理标识
            String result_code = jsonObject.getString("result_code");
            if (StringUtils.equals(BizConstant.SUCCESS, result_code)) {
                //生成二维码图片，首先我得获取到code_url
                String code_url = jsonObject.getString("code_url");

                //将code_url生成二维码图片
                int width = 200;
                int hight = 200;

                Map<EncodeHintType, Object> hintMap = new HashMap<>();
                hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                //创建一个矩阵对象
                BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, width, hight, hintMap);
                //获取输出流对象
                OutputStream outputStream = response.getOutputStream();
                //将矩阵对象转换为流
                MatrixToImageWriter.writeToStream(bitMatrix, "jpg", outputStream);
                outputStream.flush();
                outputStream.close();
            } else {
                response.sendRedirect(request.getContextPath() + "/toRecharge.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/toRecharge.jsp");
        }
    }

    @RequestMapping("/loan/wxPayIsSuccess")
    @ResponseBody
    public Object wxpayBack(HttpServletRequest request, Model model,
                            @RequestParam(value = "out_trade_no", required = true) String out_trade_no) {
        logger.info("================微信支付回调查询支付结果==================");
        Map paramMap = new HashMap();
        paramMap.put("out_trade_no", out_trade_no);

        //调用pay统一下单API接口 -> 返回参数，参数中包含code_url
        String jsonResult = HttpClientUtils.doPost(commonProperties.getWxpayBack(), paramMap);
        logger.info("调用微信支付订单查询结果： [{}]", jsonResult);
        //解析json格式的字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonResult);
        //获取通信标识
        String tradeState = jsonObject.getString("trade_state");
        Map result = new HashMap();
        if (BizConstant.SUCCESS.equals(tradeState)) {
            // 支付成功,更改支付状态
            logger.info("===========支付成功==========");
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRechargeNo(out_trade_no);
            //0充值中，1充值成功，2充值失败
            rechargeRecord.setRechargeStatus("1");
            rechargeRecordService.modifyRechargeRecordByRechargeNo(rechargeRecord);
            result.put("result", "success");
            return result;
        }

        result.put("result", "notPay");
        logger.info("============订单待支付============");

        return result;
    }

}
