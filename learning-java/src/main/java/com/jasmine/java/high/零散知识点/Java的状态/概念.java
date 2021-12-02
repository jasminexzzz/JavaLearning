package com.jasmine.java.high.零散知识点.Java的状态;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     网上很多解释如下：
     1、有状态就是有数据存储功能。有状态对象(Stateful Bean)，就是有实例变量的对象，可以保存数据，是非线程安全的。在不同方法调用间不保留任何状态。

     2、无状态就是一次操作，不能保存数据。无状态对象(Stateless Bean)，就是没有实例变量的对象.不能保存数据，是不变类，是线程安全的。

     个人理解：
     说一说什么叫状态？
     百度百科中的说明为“物质状态，是指物质系统所处的状况，由一组物理量来表征”，
     在java编程的世界里可以理解为“对象的状态，是指当前对象中成员变量所存储的数据情况”，状态就是随着时间而变化的数据，这些数据是已经被存储下来的数据。

     所谓对象的“无状态”表现形式个人觉得应该这样理解比较好：“一次操作，不需要将操作的数据或操作过程中产生的数据存储下来，直接返回操作结果即可，而且每次的操作互不影响”。

     举例说明（根据网上很多关于有无状态说明的博客内容）
     看看Spring中所谓的有状态(Stateful)和无状态(Stateless) Bean

     1. 默认情况下，从Spring bean工厂所取得的实例为singleton（scope属性为singleton）,容器只存在一个共 享的bean实例，Spring在Controller层就是默认采用singleton模式来实例化bean对象。

     如果在Controller层的bean对象中定义了成员变量，并且创建了变量对应的getter和setter方法，那么可以说这个对象具备了有状态对象的特征 ：“数据存储功能”, 这样做在多线程环境下不安全，所以一般不建议在Controller层创建成员变量。如果想要在Controller层中的Bean中定义成员变量用来存储数据，那么建议设置Spring采用Prototype模式实例化Bean。

     2. 在Service层、Dao层用Spring也默认采用singleton模式实例化对象，虽然Service类也有dao这样的属性，但dao这些类都是没有状态信息的，也就是相当于不变(immutable)类，所以Service层的Bean也都属于无状态对象。

     3. 无状态的Bean适合用不变模式，技术就是单例模式，这样可以共享实例，提高性能。有状态的Bean，多线程环境下不安全，那么适合用Prototype原型模式。Prototype: 每次对bean的请求都会创建一个新的bean实例。
     有状态的bean使用prototype作用域，无状态的bean则应该使用singleton作用域。
     ---------------------
     作者：小时候的阳光
     来源：CSDN
     原文：https://blog.csdn.net/gzt19881123/article/details/78201676
     版权声明：本文为博主原创文章，转载请附上博文链接！
     */
}
