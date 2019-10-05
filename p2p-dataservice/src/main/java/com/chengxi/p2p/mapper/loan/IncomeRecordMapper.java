package com.chengxi.p2p.mapper.loan;

import com.chengxi.p2p.model.loan.IncomeRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/5
 */
@Mapper
public interface IncomeRecordMapper {
    List<IncomeRecord> selectIncomeRecordByPage(Map<String, Object> paramMap);

    int insertSelective(IncomeRecord record);

    /**
     * 根据收益状态且收益时间与当前时间相同的收益记录
     * @param incomeStatus
     * @return
     */
    List<IncomeRecord> selectIncomeRecordByIncomeStatusAndIncomeDate(Integer incomeStatus);

    int updateByPrimaryKeySelective(IncomeRecord record);
}
