package com.jasmine.JavaBase.A_基础.类.变量;

public class Test {
    /*public static void main(String[] args) {
        成员变量 a = new 成员变量();
        System.out.println(a.name);
        //值得注意的是这种调用方法是不可取的，虽然java允许，但可读性差，静态方法通常使用类来调用，而不是实例
        System.out.println(a.age);

        a.name = "王云飞";
        a.age = 11;


        System.out.println(a.name);
        System.out.println(a.age);


        成员变量 a1 = new 成员变量();

        System.out.println(a1.age);
    }*/

    public static void main(String[] args) {
        局部变量 a = new 局部变量();
        a.getName();
    }
}
