package com.jasmine.A1_java.base.A_基础.类.变量;

/**
 * 成员变量是随类初始化或对象初始化而初始化的
 * 当类初始化时，系统会为该类的类变量，也就是static修饰的变量，也成为静态变量，分配内存，并分配默认值。
 * 当创建对象时，系统会为该对象的实例变量分配内存，并分配默认值。
 */
public class 成员变量 {

    //均称为成员变量
    public String name;//实例变量
    public static int age;//类变量


    public static void main(String[] args) {
        成员变量 a = new 成员变量();
        成员变量 b = new 成员变量();

        a.age = 1;
        a.name = "a";
        System.out.println(b.age + "," + b.name);
    }


}
