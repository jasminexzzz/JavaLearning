package com.jasmine.B3_design_mode.代理模式_Proxy.静态代理;

/**
 * @author : jasmineXz
 */
public class Test {
    public static void main(String[] args) {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        UserServiceProxy proxy = new UserServiceProxy(userServiceImpl);
        proxy.save();
    }
}
