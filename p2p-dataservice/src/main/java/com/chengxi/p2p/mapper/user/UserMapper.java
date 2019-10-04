package com.chengxi.p2p.mapper.user;

import com.chengxi.p2p.model.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@Mapper
public interface UserMapper {
    User getUser(Integer id);

    Long selectAllUserCount();

    User selectUserByPhone(String phone);

    int insertSelective(User user);

    User selectUserByPhoneAndLoginPassword(@Param("phone")String phone, @Param("loginPassword")String loginPassword);

    int updateByPrimaryKeySelective(User updateUser);
}
