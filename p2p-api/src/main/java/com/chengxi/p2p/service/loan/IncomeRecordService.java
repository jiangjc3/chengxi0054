package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.model.loan.IncomeRecord;
import com.chengxi.p2p.model.vo.PaginatinoVO;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/5
 */
public interface IncomeRecordService {
    /**
     * 根据用户标识查询最近的收益
     * @param paramMap
     * @return
     */
    List<IncomeRecord> queryIncomeRecordTopByUid(Map<String, Object> paramMap);

    /**
     * 生成收益计划
     */
    void generateIncomePlan();

    /**
     * 收益返还
     */
    void generateIncomeBack();

    PaginatinoVO<IncomeRecord> queryIncomeRecordByPage(Map<String, Object> paramMap);
}
