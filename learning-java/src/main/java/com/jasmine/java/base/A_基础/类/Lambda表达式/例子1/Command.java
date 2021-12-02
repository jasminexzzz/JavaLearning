package com.jasmine.java.base.A_基础.类.Lambda表达式.例子1;


/**
 * Lambda表达式
 * 1）Lambda表达式的目标类型必须是“函数式接口(functional interface)”，函数式接口代表只包含一个抽象方法的接口
 *    函数式接口可以包含多个默认方法，类方法，但只能声明一个抽象方法
 *    Java8专门为函数式接口提供了@FunctionalInterface注解，该注解通常放在接口定义前面，该注解对程序功能没有任何作用
 *    它用于告诉编译器执行更严格检查——检查该接口必须是函数式接口，否则编译器就会报错
 * 2）如果采用匿名内部类语法来创建函数式接口的实例，则只需要实现一个抽象方法，在这种情况下即可采用Lambda表达式来创建对象
 *    java8有大量的函数式接口，如runnable,ActionListener接口
 *
 * 3）lambda表达式中无法访问局部变量，只能访问数组或实例变量
 *
 */
public interface Command {
    void process(int[] target);
}
