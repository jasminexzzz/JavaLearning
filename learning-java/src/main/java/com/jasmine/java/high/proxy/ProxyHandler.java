package com.jasmine.java.high.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author wangyf
 */
public class ProxyHandler implements InvocationHandler {

    private Target target;

    public ProxyHandler(Target target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用前");

        Object result = method.invoke(target, args);

        System.out.println("调用后");
        return result;
    }
}
