package com.jasmine.B3_design_mode.代理模式_Proxy;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     一. 概念
        1.什么是代理模式
            提供了对目标对象另外的访问方式;即通过代理对象访问目标对象.这样做的好处是:可以在目标对象实现的基础上,增强额外的功能
            操作,即扩展目标对象的功能.

            说白了,就是不改你的代码,还要拓展你的功能.

        2.举个例子
            有个用户业务层UserService,实现了增删改查功能.
            这时候老板要求,新增的同时需要保存是谁进行的本次操作,在不想更改业务层的情况下,就需要用到代理

        3.来个图
            操作人 ──────> 目标对象
            操作人 ──────> 代理对象 ──────> 目标对象

     二. 静态代理
        因为代理对象需要与目标对象实现一样的接口,所以会有很多代理类,类太多.同时,一旦接口增加方法,目标对象与代理对象都要维护.
            @see com.jasmine.B3_design_mode.代理模式_Proxy.静态代理

        1. 简单说
            比如 A 调用 UserService
            变为 A 调用 B ,B中增加其他方法,然后B再调用UserService

        2. 来个图
            操作人 ──────> 代理对象 ┬──────> 调用前增强
                                   ├──────> 目标对象
                                   └──────> 调用后增强
     三. JDK动态代理
        @see com.jasmine.B3_design_mode.代理模式_Proxy.动态代理.例子三.ProxyFactory

        1. 动态代理需要用到的东西
            1). Proxy(代理,也就是代理类)
                (1). 概念: Proxy提供了用于创建动态代理类和代理对象的静态方法,他是所有动态代理类的父类.
                (2). 方法:
                    Ⅰ. static Class<?> getProxyClass(ClassLoader loader,Class<?>...interfaces)
                         动态创建一个动态代理类所对应的Class对象,该代理类将实现interfaces所指定的多个接口.
                    Ⅱ. static Object newProxyInstance(ClassLoader loader,Class<?>...interfaces,InvocationHandler h)
                         直接创建一个动态代理对象,该代理对象的实现类实现了Interfaces指定的系列接口,执行代理对象的每个方法都会被替换执
                         行InvocationHandler对象的invoke方法.
            2). InvocationHandler(调用处理器,可以理解为调用的方法)
                (1). 概念: InvocationHandler接口是proxy代理实例的调用处理程序实现的一个接口
                    每一个动态代理类的调用处理程序都必须实现InvocationHandler接口，并且每个代理类的实例都关联到了实现该接口的
                    动态代理类调用处理程序中，当我们通过动态代理对象调用一个方法时候，这个方法的调用就会被转发到实现
                    InvocationHandler接口类的invoke方法来调用
                (2). 方法:
                    Ⅰ. public Object invoke(Object proxy, Method method, Object[] args)throws Throwable
                         proxy:代理类代理的真实代理对象com.sun.proxy.$Proxy0
                         method:我们所要调用某个对象真实的方法的Method对象
                         args:指代代理对象方法传递的参数

        2. 用人话解释一下
            不看那些不是给人读的概念,用通俗易懂的方法解释下
            Proxy就是代理类.
            代理类总要返回一个对象,没对象我们就没办法调动任何方法.
            怎么对象从哪里来呢?调用newProxyInstance()方法.返回一个Object对象.你可以强转成任何目标对象.
            那么newProxyInstance()方法怎么用,一眼看去很懵逼,但其实很好懂,即使是刚学习Java的同学也可以根据经验来理解.

            JDK动态代理的用法
                public static Object newProxyInstance(
                    ClassLoader loader,
                    Class<?>[] interfaces,
                    InvocationHandler h
                )
                1). ClassLoader loader,
                    首先我们知道,你要调用一个类,总要把这个类加载起来吧,那代理类怎么加载呢?他总要知道我们的目标对象是怎么加载的吧.
                    所以,ClassLoader loader 就是目标对象的类加载方法.
                    一般通过Object.getClass().getClassLoader()获取.
                    @see com.jasmine.A1_java.high.JVM.类加载机制.概念

                2). Class<?>[] interfaces,
                    我们知道,JDK代理需要目标对象实现一个接口,而且Java是允许实现多个接口的,那么首先他是个数组.
                    那么interfaces用来干什么呢?当然是和ClassLoader组合在一起去来创建代理类.
                    知道类加载器,知道接口,可以创建一个和目标类一样的代理类也就不足为奇了.
                    一般通过Object.getClass().getInterfaces()获取.
                    至于怎么创建,可以查看{@link java.lang.reflect.WeakCache}

                3). InvocationHandler h
                    调用处理器就可以理解为代理类所调用的方法了,这个方法中我们就可以在调用目标方法前进行我们想要的拓展.
                    但InvocationHandler是个接口,我们知道接口只是定义一个规范,要求我们去实现的.
                    所以一般我们会写一个类来实现InvocationHandler接口,然后重写invoke方法.实际中代理类其实调用的是该方法.
                    所以我们就可以在该方法中进行拓展.

                    (1) public Object invoke(Object proxy, Method method, Object[] args)
                        Object   proxy   代理对象
                        Method   method  代理对象所调用的方法
                            method.invoke(Object obj, Object... args)
                                Object obj     目标对象  <这里为什么会出现目标对象??>
                                Object... args 代理对象所调用的方法的参数
                        Object[] args    代理对象所调用的方法的参数

                    由上可见,InvocationHandler.invoke还是需要使用目标对象,所以一般实现InvocationHandler的类都会创建一个Object对象来保存
                    目标对象.

                    等等.那包含了目标对象,这不就是和静态代理一样了吗?

            没错,动态代理和静态代理在设计模式思想上是一样的,就是用一个代理类来调用方法.
            但是动态代理进行了优化,包括:
            1). 动态代理不需要代理类和目标类实现相同的接口.
                    静态代理: 接口增加一个方法,目标类和代理类都要实现该接口.
                    动态代理: 只需要在目标类实现即可,可以让我们将注意力集中在业务中.
            2). 静态代理的代理类值服务于一种对象.
                    静态代理: 用户类有一个用户代理类.如果现在有一个角色类.那又要常见一个角色类代理.
                    动态代理: 不在乎是谁要创建代理类,我直接通过目标类的ClassLoader和interfaces自动创建代理类.

        3. 来个图
                                  操作人
                                    |
                                    |
                             自动创建代理对象
                                   所需
                        ┌───────────┼───────────┐
                        |           |           |
                    目标对象      目标对象   包含一个
                    的类加载      的接口     方法拓展InvocationHandler
                    器

    三. CGLIB动态代理





























     */
}
