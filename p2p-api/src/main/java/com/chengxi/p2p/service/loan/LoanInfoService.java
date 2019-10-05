package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.model.loan.LoanInfo;
import com.chengxi.p2p.model.vo.PaginatinoVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
public interface LoanInfoService {
    Double queryHistoryAverageRate();

    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    LoanInfo queryLoanInfoById(Integer id);

    PaginatinoVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap);

    PageInfo<LoanInfo> queryLoanInfoListByType(Map<String, Integer> paraMap);
}
