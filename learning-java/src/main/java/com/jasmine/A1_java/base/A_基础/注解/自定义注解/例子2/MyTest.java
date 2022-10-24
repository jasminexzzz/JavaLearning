package com.jasmine.A1_java.base.A_基础.注解.自定义注解.例子2;

public class MyTest {

    @Testable
    public static void m1(){

    }
    public static void m2(){

    }
    @Testable
    public static void m3(){
        throw new IllegalArgumentException("参数错误了");
    }
    public static void m4(){

    }
    @Testable
    public static void m5(){

    }
    public static void m6(){

    }
    @Testable
    public static void m7(){
        throw new RuntimeException("程序业务出现异常");
    }
    public static void m8(){

    }
}
