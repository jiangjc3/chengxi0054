package com.chengxi.p2p.service.user;

import com.chengxi.p2p.model.user.User;
import com.chengxi.p2p.model.vo.ResultObject;

/**
 * @author CHENGXI
 * @date 2019/10/2
 */
public interface UserService {
    User getUserById(Integer id);

    Long queryAllUserCount();

    User queryUserByPhone(String phone);

    ResultObject register(String phone, String loginPassword);

    User login(String phone, String loginPassword);

    int modifyUserById(User user);
}
