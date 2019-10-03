package com.chengxi.p2p.controller;

import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.model.loan.LoanInfo;
import com.chengxi.p2p.service.loan.BidInfoService;
import com.chengxi.p2p.service.loan.LoanInfoService;
import com.chengxi.p2p.service.user.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Controller
public class IndexController {
    @Reference
    private LoanInfoService loanInfoService;

    @Reference
    private BidInfoService bidInfoService;

    @Reference
    private UserService userService;

    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model) {
        // 获取历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        model.addAttribute(BizConstant.HISTORY_AVERAGE_RATE, historyAverageRate);

        // 获取平台注册总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(BizConstant.ALL_USER_COUNT, allUserCount);

        // 获取平台累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(BizConstant.ALL_BID_MONEY, allBidMoney);

        //将以下查询看成是一个分页，实际功能：根据产品类型查询产品信息显示前几个
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //页码：起始下标
        paramMap.put("currentPage", 0);

        //获取新手宝产品:产品类型：0，显示第1页，每页显示1个
        paramMap.put("productType", BizConstant.PRODUCT_TYPE_X);
        paramMap.put("pageSize", 1);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //获取优选产品：产品类型：1，显示第1页，每页显示4个
        paramMap.put("productType", BizConstant.PRODUCT_TYPE_U);
        paramMap.put("pageSize", 4);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);


        //获取散标产品：产品类型：2，显示第2页，每页显示8个
        paramMap.put("productType", BizConstant.PRODUCT_TYPE_S);
        paramMap.put("pageSize", 8);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        model.addAttribute("xLoanInfoList", xLoanInfoList);
        model.addAttribute("uLoanInfoList", uLoanInfoList);
        model.addAttribute("sLoanInfoList", sLoanInfoList);


        return "index";
    }

}
