package com.chengxi.p2p.mapper.loan;

import com.chengxi.p2p.model.loan.RechargeRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/19
 */
@Mapper
public interface RechargeRecordMapper {
    Long selectTotal(Map<String, Object> paramMap);

    List<RechargeRecord> selectRechargeRecordByPage(Map<String, Object> paramMap);

    int insertSelective(RechargeRecord record);

    /**
     * 根据充值订单号更新充值记录
     * @param rechargeRecord
     * @return
     */
    int updateRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);
}
