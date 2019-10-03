package com.chengxi.p2p.service.user;

import com.chengxi.p2p.mapper.user.FinanceAccountMapper;
import com.chengxi.p2p.model.user.FinanceAccount;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer id) {
        return financeAccountMapper.selectFinanceAccountByUid(id);
    }
}
