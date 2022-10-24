package com.jasmine.A1_java.high.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleTest {

    public static void main(String[] args) {
        test1();
        test2();
    }

    /**
     *
     */
    private static void test1() {
        User user = new User("张三");
        MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
        MethodType methodType = MethodType.methodType(String.class, int.class);
        try {
            MethodHandle method = publicLookup.findVirtual(User.class, "printName", methodType);

            // 第一种方式，直接调用
            // invokeExact 会严格校验
            String result1 = (String) method.invokeExact(new User("张三"), 10);
            System.out.println(result1);

            // 第二种方式，绑定后调用
            MethodHandle bind = method.bindTo(new User("李四"));
            String result2 = (String) bind.invoke(20);
            System.out.println(result2);

        } catch (NoSuchMethodException e) {
            System.out.println("没有找到对应方法");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private static void test2() {
        System.out.println("========== test2 ==================================================");
        MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
        MethodType methodType = MethodType.methodType(String.class);
        try {
            // 直接绑定对象
            MethodHandle method = publicLookup.bind(new User("张三"), "printName", methodType);

            // 第一种方式，直接调用
            String result1 = (String) method.invoke();
            System.out.println(result1);

            /*
             * 此时是不允许再绑定到其他对象上，会报错
             * java.lang.IllegalArgumentException: no leading reference parameter
             */
            MethodHandle bind = method.bindTo(new User("李四"));

        } catch (NoSuchMethodException e) {
            System.out.println("没有找到对应方法");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
