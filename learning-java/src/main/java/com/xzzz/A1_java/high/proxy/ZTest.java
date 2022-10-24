package com.xzzz.A1_java.high.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangyf
 */
public class ZTest {

    public static void main(String[] args) throws Throwable {
        proxy1();
        proxy2();
    }

    /**
     * 第一种常用方式, 需要一个接口的实现类, 然后基于该实现类创建一个代理实现类, 可以拓展实现类的功能
     */
    public static void proxy1() {
        // 会将生成的代理类class文件保存至目录, 通常是一个 $Proxy0.class 文件
        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        System.out.println("\n========== 代理1 ==========================================");
        // 创建原始对象ŒŒ
        Target target = new TargetImpl();
        // 原始对象的拓展方法
        InvocationHandler handler = new ProxyHandler(target);
        // 创建代理对象, 已被拓展方法包围
        Target targetProxy = (Target) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                handler);

        System.out.println(targetProxy.getTarget());
    }

    /**
     * 第二种常用方式, 不需要默认是实现类, 接口的具体逻辑其实是在 InvocationHandler 的代理类中实现
     * <p>很多框架使用这种方式, 如Dubbo/Feign, 使用者只需要创建接口, 具体功能由代理类实现
     */
    public static void proxy2() throws Throwable {
        System.out.println("\n========== 代理2 ==========================================");
        // 获取接口的Class信息
        Class<?> TargetProxyClazz = Proxy.getProxyClass(Target.class.getClassLoader(), Target.class);
        // 获取接口的被拓展后的默认构造方法
        Constructor<?> targetConstructor = TargetProxyClazz.getConstructor(InvocationHandler.class);
        // 创建代理对象
        Target targetProxyImpl = (Target)targetConstructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(proxy.getClass().getName());
                System.out.println(method.getName());
                return "代理对象处理的结果";
            }
        });

        System.out.println(targetProxyImpl.getTarget());
    }
}
