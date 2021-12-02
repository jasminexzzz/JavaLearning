package com.jasmine.java.base.A_基础.类.引用和对象.例子2;

public class Cat {
    public String name;

    public static void main(String[] args) {
        Cat c1 = new Cat();
        Cat c2 = c1;

        c1.name = "c1";
        System.out.println(c1.name);
        System.out.println(c2.name);

        c2.name = "aaa";
        System.out.println(c1.name);

    }
}
