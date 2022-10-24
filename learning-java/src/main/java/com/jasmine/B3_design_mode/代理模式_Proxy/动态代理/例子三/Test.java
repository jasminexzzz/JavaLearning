package com.jasmine.B3_design_mode.代理模式_Proxy.动态代理.例子三;


/**
 * 简单的动态代理
 *
 * @author : jasmineXz
 */
public class Test {
    public static void main(String[] args) {
        // 目标对象
        UserService target = new UserServiceImpl();
        // 给目标对象创建代理对象
        UserService proxy = (UserService) new ProxyFactory(target).getProxyInstance();
        User u = new User("admin",2);
        // 通过代理对象执行方法
        proxy.save(u);
        User getu = proxy.getUser(u);
    }
}
