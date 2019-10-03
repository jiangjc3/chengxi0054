package com.chengxi.p2p.service.user;

import com.chengxi.p2p.model.user.User;

/**
 * @author CHENGXI
 * @date 2019/10/2
 */
public interface UserService {
    User getUserById(Integer id);

    Long queryAllUserCount();
}
