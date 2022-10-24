package com.xzzz.B3_design_mode.代理模式_Proxy.动态代理.例子三;

import java.lang.reflect.Proxy;

/**
 * @author : jasmineXz
 */
public class ProxyFactory {
    private Object target;
    public ProxyFactory(Object target){
        this.target = target;
    }

    //给目标对象生成代理对象
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    System.out.println("=============================进入代理=============================");
                    System.out.println("参数:"+((User)args[0]).getName());

                    System.out.println("方法名:"+method.getName());
                    System.out.println("动态代理-开始事务");
                    //执行目标对象方法
                    Object returnValue = method.invoke(target, args);
                    System.out.println("动态代理-提交事务");
                    System.out.println("=============================结束代理=============================");
                    return returnValue;
                }
        );
    }
}
