package com.chengxi.p2p.mapper.user;

import com.chengxi.p2p.model.user.FinanceAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Mapper
public interface FinanceAccountMapper {
    FinanceAccount selectFinanceAccountByUid(Integer id);
}
