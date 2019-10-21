package com.chengxi.p2p.controller;

import com.alibaba.fastjson.JSONObject;
import com.chengxi.p2p.config.CommonProperties;
import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.model.loan.BidInfo;
import com.chengxi.p2p.model.loan.IncomeRecord;
import com.chengxi.p2p.model.loan.RechargeRecord;
import com.chengxi.p2p.model.user.FinanceAccount;
import com.chengxi.p2p.model.user.User;
import com.chengxi.p2p.model.vo.ResultObject;
import com.chengxi.p2p.service.loan.BidInfoService;
import com.chengxi.p2p.service.loan.IncomeRecordService;
import com.chengxi.p2p.service.loan.RechargeRecordService;
import com.chengxi.p2p.service.user.FinanceAccountService;
import com.chengxi.p2p.service.user.UserService;
import com.chengxi.p2p.utils.HttpClientUtils;
import com.chengxi.p2p.utils.ReturnJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@RestController
public class UserController {

    @Reference
    private UserService userService;

    @Reference
    private FinanceAccountService financeAccountService;

    @Autowired
    private CommonProperties commonProperties;

    @Reference
    private BidInfoService bidInfoService;

    @Reference
    private RechargeRecordService rechargeRecordService;

    @Reference
    private IncomeRecordService incomeRecordService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/getUser")
    public Object getUser(@RequestParam("id") Integer id) {
        User user = userService.getUserById(id);
        return ReturnJson.success(user);
    }

    /**
     * 验证手机号是否存在
     * http://lcoalhost:8080/p2p/loan/checkPhone
     * @param request
     * @param phone 必填
     * @return
     */
    @RequestMapping(value = "/loan/checkPhone")
    public Object checkPhone(HttpServletRequest request,
                             @RequestParam (value = "phone",required = true) String phone) {
        Map<String,Object> retMap = new HashMap<>();

        //查询手机号码是否重复 -》 返回Boolean|User**|int
        //根据手机号查询用户信息
        User user = userService.queryUserByPhone(phone);

        //判断用户是否存在
        if (null != user) {
            retMap.put(BizConstant.ERROR_MESSAGE,"该手机号码已被注册，请更换手机号码");
            return retMap;
        }
        retMap.put(BizConstant.ERROR_MESSAGE,BizConstant.OK);

        return retMap;
    }

    @PostMapping(value = "/loan/checkCaptcha")
    public Map<String,Object> checkCaptcha(HttpServletRequest request,
                                           @RequestParam (value = "captcha",required = true) String captcha) {
        Map<String,Object> retMap = new HashMap<>();

        //获取session中的图形验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(BizConstant.CAPTCHA);

        //验证图形验证码是否一致
        if (!StringUtils.equalsIgnoreCase(sessionCaptcha,captcha)) {
            retMap.put(BizConstant.ERROR_MESSAGE,"请输入正确的图形验证码");
            return retMap;
        }

        retMap.put(BizConstant.ERROR_MESSAGE,BizConstant.OK);

        return retMap;
    }

    @RequestMapping(value = "/loan/register")
    public Object regiter(HttpServletRequest request,
                          @RequestParam(value = "phone",required = true) String phone,
                          @RequestParam(value = "loginPassword",required = true) String loginPassword,
                          @RequestParam(value = "replayLoginPassword",required = true) String replayLoginPassword) {

        Map<String,Object> retMap = new HashMap<>();

        //验证参数
        if (!Pattern.matches("^1[1-9]\\d{9}$",phone)) {
            retMap.put(BizConstant.ERROR_MESSAGE,"请输入正确的手机号码");
            return retMap;
        }

        if (!StringUtils.equals(loginPassword,replayLoginPassword)) {
            retMap.put(BizConstant.ERROR_MESSAGE,"两次密码输入不一致");
            return retMap;
        }

        //用户的注册(手机号，登录密码)【1.新增用户信息 2.开立帐户】 -> 返回Boolean|int|结果对象ResultObject
        ResultObject resultObject = userService.register(phone,loginPassword);

        //判断是否注册成功
        if (!StringUtils.equals(BizConstant.SUCCESS,resultObject.getErrorCode())) {
            retMap.put(BizConstant.ERROR_MESSAGE,"注册失败，请稍后重试...");
            return retMap;
        }

        //将用户的信息存放到session中
        request.getSession().setAttribute(BizConstant.SESSION_USER,userService.queryUserByPhone(phone));
        retMap.put(BizConstant.ERROR_MESSAGE,BizConstant.OK);

        return retMap;
    }

    @RequestMapping(value = "/loan/login")
    public Object login(HttpServletRequest request,
                        @RequestParam (value = "phone",required = true) String phone,
                        @RequestParam (value = "loginPassword",required = true) String loginPassword) {
        Map<String,Object> retMap = new HashMap<String,Object>();

        //验证手机号码格式
        if (!Pattern.matches("^1[1-9]\\d{9}$",phone)) {
            retMap.put(BizConstant.ERROR_MESSAGE,"请输入正确的手机号码");
            return retMap;
        }

        //用户登录【1.根据手机号和密码查询用户 2.更新最近登录时间】（手机号，密码） -> 返回User|Map
        User user = userService.login(phone,loginPassword);

        //判断用户是否存在
        if (null == user) {
            retMap.put(BizConstant.ERROR_MESSAGE,"用户名或密码输入有误，请重新输入");
            return retMap;
        }

        // 查询用户余额
        FinanceAccount account = financeAccountService.queryFinanceAccountByUid(user.getId());
        user.setAvailableMoney(account.getAvailableMoney());

        //将用户的信息存放到session中
        request.getSession().setAttribute(BizConstant.SESSION_USER,user);

        retMap.put(BizConstant.ERROR_MESSAGE,BizConstant.OK);
        return retMap;
    }

    @RequestMapping(value = "/loan/myCenter")
    public ModelAndView myCenter(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        //从session中获取用户标识
        User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);

        //根据用户标识获取帐户可用余额
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());

        //准备请求参数
        Map<String,Object> paramMap = new ConcurrentHashMap<String,Object>();
        paramMap.put("uid", sessionUser.getId());//用户标识
        paramMap.put("currentPage", 0);//页码
        paramMap.put("pageSize", 5);//每页显示条数

        //查询最近的投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfoTopByUid(paramMap);

        //查询最近的充值记录
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryRechargeRecordTopByUid(paramMap);

        //查询最近的收益记录
        List<IncomeRecord> incomeRecordList = incomeRecordService.queryIncomeRecordTopByUid(paramMap);

        //将以上查询结果存放到model对象中
        model.addAttribute("financeAccount", financeAccount);
        model.addAttribute("bidInfoList", bidInfoList);
        model.addAttribute("rechargeRecordList", rechargeRecordList);
        model.addAttribute("incomeRecordList", incomeRecordList);
        ModelAndView mv = new ModelAndView("myCenter", model);
        return mv;
    }

    @RequestMapping(value = "/loan/verifyRealName")
    public Object verifyRealName(HttpServletRequest request,
                                 @RequestParam (value = "realName",required = true) String realName,
                                 @RequestParam (value = "idCard",required = true) String idCard,
                                 @RequestParam (value = "replayIdCard",required = true) String replayIdCard) {
        Map<String,Object> retMap = new HashMap<String,Object>();

        //验证参数
        if (!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$",realName)) {
            retMap.put(BizConstant.ERROR_MESSAGE,"真实姓名只支持中文");
            return retMap;
        }
        if (!Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)",idCard)) {
            retMap.put(BizConstant.ERROR_MESSAGE,"请输入正确的身份证号码");
            return retMap;
        }
        if (!StringUtils.equals(idCard,replayIdCard)) {
            retMap.put(BizConstant.ERROR_MESSAGE,"两次输入身份证号码不一致");
            return retMap;
        }

        //准备实名认证的参数
        Map<String,Object> paramMap = new HashMap<String,Object>();

        //实名认证appkey
        paramMap.put("appkey",commonProperties.getRealNameAppkey());

        //真实姓名
        paramMap.put("realName",realName);

        //身份证号码
        paramMap.put("cardNo",idCard);

        //发送请求验证用户真实信息 -> 返回json格式的字符串
        String jsonString = HttpClientUtils.doPost(commonProperties.getRealNameUrl(), paramMap);
//        String jsonString = "{\"code\":\"10000\",\"charge\":false,\"remain\":1305,\"msg\":\"查询成功\",\"result\":{\"error_code\":0,\"reason\":\"成功\",\"result\":{\"realname\":\"乐天磊\",\"idcard\":\"350721197702134399\",\"isok\":true}}}";

        //解析json格式字符串，使用fastjson
        //将json格式的字符串转换为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取指定key所对应的value,获取code通信标识
        String code = jsonObject.getString("code");

        //判断通信是否成功
        if (StringUtils.equals("10000",code)) {
            //通信成功
            //获取isok是否匹配标识
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
            //判断真实姓名与身份证号码是否一致
            if (isok) {
                //从session中获取用户的信息
                User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);
                //更新用户的信息
                User updateUser = new User();
                updateUser.setId(sessionUser.getId());
                updateUser.setName(realName);
                updateUser.setIdCard(idCard);
                int modifyUserCount = userService.modifyUserById(updateUser);

                if (modifyUserCount > 0) {
                    //更新session中用户的信息
                    request.getSession().setAttribute(BizConstant.SESSION_USER,userService.queryUserByPhone(sessionUser.getPhone()));
                    retMap.put(BizConstant.ERROR_MESSAGE,BizConstant.OK);
                } else {
                    retMap.put(BizConstant.ERROR_MESSAGE,"系统繁忙，请稍后重试...");
                    return retMap;
                }
            } else {
                retMap.put(BizConstant.ERROR_MESSAGE,"真实姓名与身份证号码不匹配");
                return retMap;
            }
        } else {
            //难信失败
            retMap.put(BizConstant.ERROR_MESSAGE,"通信失败，请稍后重试...");
            return retMap;
        }
        return retMap;
    }

    @RequestMapping("loan/myAccount")
    public ModelAndView myAccount(HttpServletRequest request, ModelMap model) {
        User user = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);
        if (!ObjectUtils.isEmpty(user)) {
            model.addAttribute("user", user);
            //根据用户标识获取帐户可用余额
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(user.getId());
            model.addAttribute("financeAccount",financeAccount);
        }

        ModelAndView mv = new ModelAndView("myAccount", model);
        return mv;
    }

    /*@RequestMapping("loan/myInvest")
    public ModelAndView myInvest(HttpServletRequest request, ModelMap model) {
        ModelAndView mv = new ModelAndView("myInvest");

        return mv;
    }*/

    /*@RequestMapping("loan/myRecharge")
    public ModelAndView myRecharge(HttpServletRequest request, ModelMap model) {
        ModelAndView mv = new ModelAndView("myRecharge");
        return mv;
    }*/

    /*@RequestMapping("loan/myIncome")
    public ModelAndView myIncome(HttpServletRequest request, ModelMap model) {
        ModelAndView mv = new ModelAndView("myIncome");
        return mv;
    }*/

}
