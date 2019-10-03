package com.chengxi.p2p.mapper.user;

import com.chengxi.p2p.model.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Mapper
public interface UserMapper {
    User getUser(Integer id);

    Long selectAllUserCount();
}
