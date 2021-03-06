package com.chengxi.p2p.mapper.loan;

import com.chengxi.p2p.model.loan.LoanInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Mapper
public interface LoanInfoMapper {
    Double selectHistoryAverageRate();

    List<LoanInfo> selectLoanInfoByPage(Map<String, Object> paramMap);

    LoanInfo selectByPrimaryKey(Integer id);

    Long selectTotal(Map<String, Object> paramMap);

    int updateLeftProductMoneyByLoanId(Map<String, Object> paramMap);

    int updateByPrimaryKeySelective(LoanInfo updateLoanInfo);

    List<LoanInfo> selectLoanInfos(@Param(value="productType")Integer ptype);

    List<LoanInfo> selectLoanInfoByProductStatus(int i);
}
