package com.xzzz.B3_design_mode.单例模式_Singleton;


/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     一.概念
        单例模式确保某个类只有一个实例
     二.单例模式特点：
        1. 单例类只能有一个实例。
        2. 单例类必须自己创建自己的唯一实例。
        3. 单例类必须给所有其他对象提供这一实例。

     三.实现单例模式
        1. 饿汉式(线程安全)
            也就是: private static Singleton single = new Singleton();
            @see com.xzzz.B3_design_mode.单例模式_Singleton.饿汉式.Singleton
        2. 懒汉式(默认情况下线程不安全)
            也就是: 在第一次调用getInstance时创建,后续进行判断
            @see com.xzzz.B3_design_mode.单例模式_Singleton.懒汉式.Singleton
        3. 静态内部类(线程安全)
            @see com.xzzz.B3_design_mode.单例模式_Singleton.静态内部类.Singleton

     */
}
