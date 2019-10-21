package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.model.loan.BidInfo;
import com.chengxi.p2p.model.vo.BidUserTop;
import com.chengxi.p2p.model.vo.PaginatinoVO;
import com.chengxi.p2p.model.vo.ResultObject;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
public interface BidInfoService {
    Double queryAllBidMoney();

    List<BidInfo> queryBidInfoListByLoanId(Integer id);

    List<BidUserTop> queryBidUserTop();

    ResultObject invest(Map<String, Object> paramMap);

    PaginatinoVO<BidInfo> queryBidInfoByPage(Map<String, Object> paramMap);

    List<BidInfo> queryBidInfoTopByUid(Map<String, Object> paramMap);
}
