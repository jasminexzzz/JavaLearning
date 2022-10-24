package com.jasmine.B3_design_mode.代理模式_Proxy.动态代理.例子一;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        五粮液 五粮液 = new 五粮液();
        卖大福 卖大福 = new 卖大福();


        /**
         *
         */
        InvocationHandler 柜台wly = new 柜台(五粮液);
        InvocationHandler 柜台df = new 柜台(卖大福);



        /**
         *
         * public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
         * loader 自然是类加载器
         * interfaces 代码要用来代理的接口
         * h 一个 InvocationHandler 引用和对象
         *
         */

        卖酒 代理商1 = (卖酒) Proxy.newProxyInstance(五粮液.class.getClassLoader(),
                五粮液.class.getInterfaces(),柜台wly);

        卖香烟 代理商2 = (卖香烟) Proxy.newProxyInstance(卖大福.class.getClassLoader(),
                卖大福.class.getInterfaces(),柜台df);


        代理商1.maijiu();
        代理商2.sell(3);
    }
}
