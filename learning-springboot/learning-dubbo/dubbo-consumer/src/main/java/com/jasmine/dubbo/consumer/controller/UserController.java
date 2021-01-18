package com.jasmine.dubbo.consumer.controller;

import com.jasmine.dubbo.api.User;
import com.jasmine.dubbo.api.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyf
 * @since 0.0.1
 */
@RestController
@RequestMapping("/consumer")
public class UserController {

    @DubboReference(version = "1.0.0", loadbalance = "roundrobin")
    private UserService userService;

    @RequestMapping("/user")
    public User findUser() {
        return userService.findUser();
    }
}
