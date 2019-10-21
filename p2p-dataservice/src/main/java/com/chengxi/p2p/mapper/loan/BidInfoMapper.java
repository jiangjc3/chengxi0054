package com.chengxi.p2p.mapper.loan;

import com.chengxi.p2p.model.loan.BidInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Mapper
public interface BidInfoMapper {
    Double selectAllBidMoney();

    List<BidInfo> selectBidInfoListByLoanId(Integer id);

    int insertSelective(BidInfo bidInfo);

    List<BidInfo> selectBidInfoByLoanId(Integer id);

    List<BidInfo> selectBidInfoByPage(Map<String, Object> paramMap);

    Long selectTotal(Map<String, Object> paramMap);
}
