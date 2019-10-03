package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.model.loan.BidInfo;
import com.chengxi.p2p.model.vo.BidUserTop;

import java.util.List;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
public interface BidInfoService {
    Double queryAllBidMoney();

    List<BidInfo> queryBidInfoListByLoanId(Integer id);

    List<BidUserTop> queryBidUserTop();
}
