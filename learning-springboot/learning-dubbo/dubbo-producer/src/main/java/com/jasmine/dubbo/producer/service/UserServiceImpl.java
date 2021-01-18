package com.jasmine.dubbo.producer.service;

import com.jasmine.dubbo.api.User;
import com.jasmine.dubbo.api.UserService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author wangyf
 * @since 0.0.1
 */
@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Override
    public User findUser() {
        User user = new User();
        user.setId(2);
        user.setUserName("wyf");
        return user;
    }
}
