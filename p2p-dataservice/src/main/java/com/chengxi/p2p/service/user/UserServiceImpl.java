package com.chengxi.p2p.service.user;

import com.chengxi.p2p.constants.BizConstant;
import com.chengxi.p2p.mapper.user.FinanceAccountMapper;
import com.chengxi.p2p.mapper.user.UserMapper;
import com.chengxi.p2p.model.user.User;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public User getUserById(Integer id) {
        User user = userMapper.getUser(id);
        redisTemplate.opsForValue().set("id", id);
        return user;
    }

    @Override
    public Long queryAllUserCount() {
        //首先去redis缓存中查询，有：直接用

        //获取指定操作某一个key的操作对象
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(BizConstant.ALL_USER_COUNT);

        //获取指定key的value值
//        Long allUserCount = (Long) boundValueOps.get();
        Number number = (Number) boundValueOps.get();
        Long allUserCount = number == null? null : number.longValue();
        //判断是否有值
        if (null == allUserCount) {
            //去数据库查询
            allUserCount = userMapper.selectAllUserCount();
            //将该值存放到redis缓存中
            boundValueOps.set(allUserCount, 15, TimeUnit.SECONDS);
        }
        return allUserCount;
    }
}
