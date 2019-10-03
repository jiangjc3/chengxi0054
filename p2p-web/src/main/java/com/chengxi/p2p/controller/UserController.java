package com.chengxi.p2p.controller;

import com.chengxi.p2p.model.user.User;
import com.chengxi.p2p.service.user.UserService;
import com.chengxi.p2p.utils.ReturnJson;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CHENGXI
 * @date 2019/10/3
 */
@RestController
public class UserController {
    @Reference
    private UserService userService;

    @RequestMapping("/getUser")
    public Object getUser(@RequestParam("id") Integer id) {
        User user = userService.getUserById(id);
        return ReturnJson.success(user);
    }

}
