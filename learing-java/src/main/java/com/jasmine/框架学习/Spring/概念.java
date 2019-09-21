package com.jasmine.框架学习.Spring;

public class 概念 {
    /**

     一. 控制反转 (IOC)
        1. 概念:
         我们首先先来了解一下控制二字,也就是在控制“正”转的情况下,在任何一个有请求作用的系统当中,至少需要有两个类互相配
         合工作,在一个入口类下使用new关键字创建另一个类的对象实例,这就好比在面向对象编程的思想下,“我“充当一个入口类,在这个
         入口类中,我每次吃饭的时候都要买一双一次性筷子（每一次使用都要new一次）,在这样的关系下,是”我“（即调用者）每次都要
         ”主动“去买一次性筷子（另一个类）,我对筷子说你老老实实的过来我的手上,是我控制了筷子,那好,在这种控制正转的关系下,
         放在现实生活当中,肯定是不现实的,而且人是懒惰的,他总会去创造出更加方便自己生活的想法,更确切的做法是,买一双普通的
         筷子（非一次性）,把他放在一个容器当中（在Spring中叫做IOC容器）,你需要使用的时候就对容器说:IOC我想要用筷子（向容器
         发出请求）,接着筷子就会”注入“到的手上,而在这个过程当中,你不再是控制方,反而演变成一名请求者（虽然本身还是调用者）,
         依赖于容器给予你资源,控制权坐落到了容器身上,于是这就是人们俗称的控制反转.

     二. 依赖注入 (DI)
        1). 概念:
            同样接着上面的例子,在控制反转的统一下,筷子是怎么来到我的手上（即我们是如何获得请求的类）,这就是一个依赖注入的
            过程.依赖注入其实是实现控制反转的一种方式.

     三. 面向切面(AOP)
     一. 什么是Spring
        1. Spring 是一个轻量级的开源框架.
        2. Spring 提倡以"最少侵入(无需继承框架提供的任何类)"的方式来管理应用中的代码,这意味着我们可以随时安装或者卸载 Spring.

     二. Spring能帮我们做什么
        1.Spring 能帮我们根据配置文件创建及组装对象之间的依赖关系.
        2.Spring 面向切面编程能帮助我们无耦合的实现日志记录,性能统计,安全控制.
        3.Spring 能非常简单的帮我们管理数据库事务.
        4.Spring 还提供了与第三方数据访问框架（如Hibernate、JPA）无缝集成,而且自己也提供了一套JDBC访问模板来方便数据库访问.
        5.Spring 还提供与第三方Web（如Struts1/2、JSF）框架无缝集成,而且自己也提供了一套Spring MVC框架,来方便web层搭建.
        6.Spring 能方便的与Java EE（如Java Mail、任务调度）整合,与更多技术整合（比如缓存框架）.

     三. Spring的优点
        1. 方便解耦,简化开发 （高内聚低耦合）
           Spring就是一个大工厂（容器）,可以将所有对象创建和依赖关系维护,交给Spring管理
           spring工厂是用于生成bean
        2. AOP编程的支持
           Spring提供面向切面编程,可以方便的实现对程序进行权限拦截、运行监控等功能
        3. 声明式事务的支持
           只需要通过配置就可以完成对事务的管理,而无需手动编程
        4. 方便程序的测试
           Spring对Junit4支持,可以通过注解方便的测试Spring程序
        5. 方便集成各种优秀框架
           Spring不排斥各种优秀的开源框架,其内部提供了对各种优秀框架（如:Struts、Hibernate、MyBatis、Quartz等）的直接支持
        6. 降低JavaEE API的使用难度
           Spring 对JavaEE开发中非常难用的一些API（JDBC、JavaMail、远程调用等）,都提供了封装,使这些API应用难度大大降低ser9]


     五. spring核心组件
        1. Bean
               Bean是Spring管理的基本单位,在基于Spring的Java EE应用中,所有的组件都被当成Bean处理,包括数据源、Hibernate的SessionFactory、事务管理器等.
           在Spring中,Bean的是一个非常广义的概念,任何的Java对象、Java组件都被当成Bean处理.
               而且应用中的所有组件,都处于Spring的管理下,都被Spring以Bean的方式管理,Spring负责创建Bean实例,并管理他们的生命周期.Bean在Spring容器中运行,
           无须感受Spring容器的存在,一样可以接受Spring的依赖注入,包括Bean属性的注入,协作者的注入、依赖关系的注入等.
               Spring容器负责创建Bean实例,所以需要知道每个Bean的实现类,Java程序面向接口编程,无须关心Bean实例的实现类；但是Spring容器必须能够精确知道每个Bean实例
           的实现类,因此Spring配置文件必须精确配置Bean实例的实现类.

        2. BeanFactory
           BeanFactory负责配置、创建、管理Bean

        3. ApplicationContext(Spring上下文)
           ApplicationContext是BeanFactory的子接口

           1). FileSystemXmlApplicationContext :
               以基于文件系统的XML配置文件创建ApplicationContext实例.

           2). ClassPathXmlApplicationContext  :
               以类加载路径下的XML配置文件创建的ApplicationContext实例.

           3). AnnotationConfigApplicationContext :
               基于Java的配置类加载Spring的应用上下文.避免使用application.xml进行配置.在使用spring框架进行服务端开发时,个人感觉注解配置在便捷性,
               和操作上都优于是使用XML进行配置
1
2

     */
}
