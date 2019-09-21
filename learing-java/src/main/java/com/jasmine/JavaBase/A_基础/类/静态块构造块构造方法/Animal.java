package com.jasmine.JavaBase.A_基础.类.静态块构造块构造方法;

/**
 *
 * 因为编译器会把构造代码块插入到不含this();的构造函数中的super();后面。
 * super()是调用父类构造函数，先有父亲，再有儿子，所以在super();之后。
 * this()是调用自身构造函数，为了保证构造代码块在类初始化时只执行一次，所以只会插入到不含this();的构造函数中。
 */
public class Animal {

    public Animal() {
        System.out.println("Animal 构造函数");
    }

    {
        System.out.println("Animal 构造代码块");
    }

}

class Cat extends Animal {

    public Cat() {
        System.out.println("Cat 构造函数");
    }

    {
        System.out.println("Cat 构造代码块");
    }

    public static void main(String[] args) {
        new Cat();
    }

}
