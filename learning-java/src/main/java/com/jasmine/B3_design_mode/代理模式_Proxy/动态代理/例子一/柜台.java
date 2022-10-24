package com.jasmine.B3_design_mode.代理模式_Proxy.动态代理.例子一;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class 柜台 implements InvocationHandler {
    private Object pingpai;

    public 柜台(Object pingpai){
        this.pingpai = pingpai;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("销售开始，柜台是:" + this.getClass().getSimpleName());
        Object obj = method.invoke(pingpai,args);
        System.out.println("销售结束");
        System.out.println("*************");
        return obj;
    }
}

