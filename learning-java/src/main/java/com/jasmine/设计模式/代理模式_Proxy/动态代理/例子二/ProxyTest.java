package com.jasmine.设计模式.代理模式_Proxy.动态代理.例子二;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Person {
    void walk(int m,String danw);
    void sayHello(String name);
}

 class MyInvokationHandler1 implements InvocationHandler {
    /*
    执行动态代理对象的所有方法时，都会被替换成执行如下的invoke方法
    其中：
    proxy：代表动态代理对象
    method：代表正在执行的方法
    args：代表调用目标方法时传入的实参。
    */
    public Object invoke(Object proxy, Method method, Object[] args) {
        System.out.println("----正在执行的方法:" + method);
        if (args != null) {
            System.out.println("下面是执行该方法时传入的实参为：");
            for (Object val : args) {
                System.out.println(val);
            }
        } else {
            System.out.println("调用该方法没有实参！");
        }
        return null;
    }
}

public class ProxyTest {
    public static void main(String[] args) throws Exception {
        // 创建一个InvocationHandler对象
        InvocationHandler handler = new MyInvokationHandler1();
        // 使用指定的InvocationHandler来生成一个动态代理对象
        Person p = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class[]{Person.class}, handler);
        // 调用动态代理对象的walk()和sayHello()方法
        p.walk(70,"米");
        p.sayHello("孙悟空");
    }
}
