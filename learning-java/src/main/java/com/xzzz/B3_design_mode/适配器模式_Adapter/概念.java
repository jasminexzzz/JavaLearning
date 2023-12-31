package com.xzzz.B3_design_mode.适配器模式_Adapter;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     适配器 (Adapter) :
     一. 什么是适配器模式
        适配器是一种很简单的设计模式,相较于其他设计模式而言
        适配器就是一种适配中间件，它存在于不匹配的二者之间，用于连接二者，将不匹配变得匹配.
        简单点理解就是平常所见的转接头，转换器之类的存在。
        说白了,就是创建一个和两者都有关系的类,来作为连接

     二. 类适配器
     @see com.xzzz.B3_design_mode.适配器模式_Adapter.类适配器.ZTest
        1. 举个例子
            我们当前有:
            类B1 implements 接口B
                            接口C

            此时有个需求,需要实现接口C,那么很简单
            类C1 implements 接口C
            那么我在实现接口C的方法时,发现在B1中有了相同的实现,那么
            类C1 extends B1 implements 接口C
            即可调用B1的方法.

     三. 对象适配器
     @see com.xzzz.B3_design_mode.适配器模式_Adapter.对象适配器.ZTest
        对象适配器和类适配器基本相同,只是对象适配器中将需要继承的类变为了传入对象类调用.

     四. 接口适配器
     @see com.xzzz.B3_design_mode.适配器模式_Adapter.接口适配器.ZTest
        接口适配器在各大框架中的使用特别频繁
        通常刚开始看源码的同学总会有疑问,为什么框架总是设计一个接口,然后有抽象类实现一部分,然后某个类A继承抽象类实现某些方法,类B继承抽象类实现某些方法.
        例如 例子1:
            接口A
            抽象类B implements 接口A 实现了一部分方法,其余全部是{return null;}
            抽象类C implements 接口A 实现了一部分方法,其余全部是{return null;}

            类B1 extends 抽象类B
            类B2 extends 抽象类B
            类C1 extends 抽象类C

        这样做的意义是什么呢?
        直接 例子2:
            B1 implements 接口A
            B2 implements 接口A
            C1 implements 接口A
        不是也一样可以实现功能吗?反正我们调用是调用B1,创建也是 A a = new B1();
        使用时完全没有变化.
        这么说其实没错,在功能实现上虽然相同,但例子1无疑更好的体现了面向对象的多态,继承等特性.
        例如调用{@link com.xzzz.B3_design_mode.适配器模式_Adapter.接口适配器.Ashili}时,我只需要a和d两个方法.
        若是该类直接实现接口A,那么abcdef这几个方法全都在该类中,从美观,扩展等方面来说,不是都显得很笨吗.

    所以设计模式的目的就是为了解耦,易扩展,高可用.也许捎带着有美观的作用.

    学习那么多的设计模式,很少有新技术,大多是利用语言的基础语法来进行更好的架构设计.
     */

}
