package com.jasmine.B3_design_mode.代理模式_Proxy.CGLIB代理;

/**
 * @author : jasmineXz
 */
public class Test {


    public static void main(String[] args) {

        //目标对象
        UserServiceImpl target = new UserServiceImpl();
        System.out.println(target.getClass());
        //代理对象
        UserServiceImpl proxy = (UserServiceImpl) new ProxyFactory(target).getProxyInstance();
        System.out.println(proxy.getClass());
        //执行代理对象方法
        proxy.save();
    }
}
