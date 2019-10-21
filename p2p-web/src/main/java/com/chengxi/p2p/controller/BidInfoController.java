package com.chengxi.p2p.controller;

import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.model.loan.BidInfo;
import com.chengxi.p2p.model.user.User;
import com.chengxi.p2p.model.vo.PaginatinoVO;
import com.chengxi.p2p.model.vo.ResultObject;
import com.chengxi.p2p.service.loan.BidInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author CHENGXI
 * @date 2019/10/4
 */
@RestController
public class BidInfoController {
    @Reference
    private BidInfoService bidInfoService;

    @RequestMapping(value = "/loan/invest")
    public Object invest(HttpServletRequest request,
                         @RequestParam(value = "loanId", required = true) Integer loanId,
                         @RequestParam(value = "bidMoney", required = true) Double bidMoney) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        //从session中获取用户的信息
        User sesionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);

        //准备请求参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //用户标识
        paramMap.put("uid", sesionUser.getId());
        //产品标识
        paramMap.put("loanId", loanId);
        //投资金额
        paramMap.put("bidMoney", bidMoney);
        //手机号码
        paramMap.put("phone", sesionUser.getPhone());

        //用户投资（用户标识，产品标识，投资金额） -> 返回结果ResultObject
        ResultObject resultObject = bidInfoService.invest(paramMap);

        //判断是否成功
        if (StringUtils.equals(BizConstant.SUCCESS, resultObject.getErrorCode())) {
            retMap.put(BizConstant.ERROR_MESSAGE, BizConstant.OK);
        } else {
            retMap.put(BizConstant.ERROR_MESSAGE, "投资人数过多，请稍后重试...");
            return retMap;
        }
        return retMap;
    }

    @RequestMapping(value = "/loan/myInvest")
    public ModelAndView myInvest(HttpServletRequest request, ModelMap model,
                           @RequestParam(value = "currentPage", required = false) Integer currentPage) {
        //判断当前页码是否有值：如果没有值，默认为第1页
        if (null == currentPage) {
            currentPage = 1;
        }

        //从session中获取用户的信息
        User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);

        //分页查询：准备分页查询的参数
        Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
        paramMap.put("currentPage", (currentPage - 1) * BizConstant.PAGE_SIZE);//起始下标：页码
        paramMap.put("pageSize", BizConstant.PAGE_SIZE);//每页显示条数
        paramMap.put("uid", sessionUser.getId());//用户标识

        //调用投资业务接口中的分页查询投资记录的方法 -》 返回数据类型：PaginationVO<投资记录>
        PaginatinoVO<BidInfo> paginationVO = bidInfoService.queryBidInfoByPage(paramMap);

        //计算总页数
        int totalPage = paginationVO.getTotal().intValue() / BizConstant.PAGE_SIZE;
        //再次求余
        int mod = paginationVO.getTotal().intValue() % BizConstant.PAGE_SIZE;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }

        //将以上查询的数据存放到model对象中
        model.addAttribute("totalRows", paginationVO.getTotal());//总记录数
        model.addAttribute("totalPage", totalPage);//总页数
        model.addAttribute("bidInfoList", paginationVO.getDataList());//查询总记录数据
        model.addAttribute("currentPage", currentPage);

        ModelAndView mv = new ModelAndView("myInvest", model);

        return mv;
    }
}
