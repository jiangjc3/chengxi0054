package com.chengxi.p2p.service.loan;

import com.chengxi.p2p.constants.BizConstant;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author CHENGXI
 * @date 2019/10/19
 */
@Service
public class UniqueNumberServiceImpl implements UniqueNumberService {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Long getUniqueNumber() {
        return redisTemplate.opsForValue().increment(BizConstant.ONLY_NUMBER,1);
    }
}
