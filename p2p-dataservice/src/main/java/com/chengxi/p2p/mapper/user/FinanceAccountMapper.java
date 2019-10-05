package com.chengxi.p2p.mapper.user;

import com.chengxi.p2p.model.user.FinanceAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Mapper
public interface FinanceAccountMapper {
    FinanceAccount selectFinanceAccountByUid(Integer id);

    int insertSelective(FinanceAccount financeAccount);

    int updateFinanceAccountByBid(Map<String, Object> paramMap);

    int updateFinanceAccountByIncomeBack(Map<String, Object> paramMap);
}
