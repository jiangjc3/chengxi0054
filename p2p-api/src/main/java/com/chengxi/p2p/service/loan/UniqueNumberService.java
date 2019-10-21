package com.chengxi.p2p.service.loan;

/**
 * @author CHENGXI
 * @date 2019/10/19
 */
public interface UniqueNumberService {
    /**
     * 获取redis全局唯一数字
     * @return
     */
    Long getUniqueNumber();
}
