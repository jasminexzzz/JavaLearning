package com.jasmine.设计模式.代理模式_Proxy.静态代理;

/**
 * service代理类
 * @author : jasmineXz
 */
public class UserServiceProxy implements UserService {
    private UserService userService;
    public UserServiceProxy(UserService userService){
        this.userService = userService;
    }

    @Override
    public void save() {
        System.out.println("开始事务");
        userService.save();
        System.out.println("提交事务");
    }
}
