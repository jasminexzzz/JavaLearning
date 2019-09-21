package com.jasmine.JavaBase.类.抽象类继承多态;

public class Test {
    public static void main(String[] args) {
        /**
         * 不可：
         * 1）无法调用子类独有的方法
         * 2）可以调用父类的方法
         * 可以：
         * 1）调用子类重写的方法
         * 2）调用父类独有的方法，其实是继承为自己的方法
         */
        Animal acat = new Cat("猫");

        acat.getColor();
        //无法调用子类独有的方法
        //acat.scratch();

        Cat ccat = new Cat();
        ccat.getColor();
        System.out.println(ccat.scratch());

    }
}
