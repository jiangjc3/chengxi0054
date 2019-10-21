package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.mapper.loan.RechargeRecordMapper;
import com.chengxi.p2p.mapper.user.FinanceAccountMapper;
import com.chengxi.p2p.model.loan.RechargeRecord;
import com.chengxi.p2p.model.vo.PaginatinoVO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author CHENGXI
 * @date 2019/10/19
 */
@Service
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<RechargeRecord> queryRechargeRecordTopByUid(Map<String, Object> paramMap) {
        return rechargeRecordMapper.selectRechargeRecordByPage(paramMap);
    }

    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
    }

    @Override
    public int recharge(Map<String, Object> paramMap) {
        //更新帐户的可用余额
        int updateFinanceCount = financeAccountMapper.updateFinanceAccountByRecharge(paramMap);
        if (updateFinanceCount > 0) {
            //更新充值记录的状态
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRechargeNo((String) paramMap.get("rechargeNo"));
            rechargeRecord.setRechargeStatus("1");
            int updateRechargeCount = rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
            if (updateRechargeCount <= 0) {
                return 0;
            }
        } else {
            return 0;
        }
        return 1;
    }

    @Override
    public PaginatinoVO<RechargeRecord> queryRechargeRecordByPage(Map<String, Object> paramMap) {
        PaginatinoVO<RechargeRecord> paginationVO = new PaginatinoVO<>();
        paginationVO.setTotal(rechargeRecordMapper.selectTotal(paramMap));
        paginationVO.setDataList(rechargeRecordMapper.selectRechargeRecordByPage(paramMap));

        return paginationVO;
    }
}
