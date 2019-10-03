package com.chengxi.p2p.service.user;

import com.chengxi.p2p.model.user.FinanceAccount;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
public interface FinanceAccountService {
    FinanceAccount queryFinanceAccountByUid(Integer id);
}
