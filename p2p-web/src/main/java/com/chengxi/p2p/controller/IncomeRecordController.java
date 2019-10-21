package com.chengxi.p2p.controller;


import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.model.loan.IncomeRecord;
import com.chengxi.p2p.model.user.User;
import com.chengxi.p2p.model.vo.PaginatinoVO;
import com.chengxi.p2p.service.loan.IncomeRecordService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
public class IncomeRecordController {
	
	@Reference
	private IncomeRecordService incomeRecordService;
	
	@RequestMapping(value="loan/myIncome")
	public String myIncome(HttpServletRequest request,Model model,
			@RequestParam (value="currentPage",required=false) Integer currentPage) {
		
		if(null == currentPage) {
			currentPage = 1;
		}
		
		User sessionUser = (User) request.getSession().getAttribute(BizConstant.SESSION_USER);
		
		Map<String,Object> paramMap = new ConcurrentHashMap<>();
		paramMap.put("uid", sessionUser.getId());
		paramMap.put("currentPage",0 );
		paramMap.put("pageSize", BizConstant.PAGE_SIZE);


		PaginatinoVO<IncomeRecord> paginationVO = incomeRecordService.queryIncomeRecordByPage(paramMap);
		
		int totalPage = paginationVO.getTotal().intValue() / BizConstant.PAGE_SIZE;
		int mod = paginationVO.getTotal().intValue() % BizConstant.PAGE_SIZE;
		if( mod > 0) {
			totalPage = totalPage + 1;
		}
		
		model.addAttribute("incomeRecordList", paginationVO.getDataList());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalRows", paginationVO.getTotal());
		model.addAttribute("totalPage", totalPage);
		return "myIncome";
	}
}
