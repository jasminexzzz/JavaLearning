package Java框架;

import com.jasmine.Java高级.零散知识点.Java的状态.概念;

/**
 * @author : jasmineXz
 */
public class Spring {
    /**
     1. 什么是AOP
        面向切面编程,主要是管理系统层的业务,比如日志,权限,事物等.AOP是将封装好的对象剖开,找出其中对多个对象产生影响的公共行为,并将其封装为一
        个可重用的模块,这个模块被命名为切面(aspect),切面将那些与业务逻辑无关,却被业务模块共同调用的逻辑提取并封装起来,减少了系统中的重复代码
        ,降低了模块间的耦合度,同时提高了系统的可维护性.

     2. 什么是IOC
        IOC控制反转主要强调的是程序之间的关系是由容器控制的,容器控制对象,控制了对外部资源的获取.而反转即为,在传统的编程中都是由我们创建对象获取
        依赖对象,而在IOC中是容器帮我们创建对象并注入依赖对象,正是容器帮我们查找和注入对象,对象是被获取,所以叫反转.

     3. spring中bean的作用域
        singleton     ： Spring IoC容器中只会存在一个共享的Bean实例,无论有多少个Bean引用它,始终指向同一对象.Singleton作用域是Spring中的缺省
                         作用域.
        prototype     ： 每次通过Spring容器获取prototype定义的bean时,容器都将创建一个新的Bean实例,每个Bean实例都有自己的属性和状态,而
                         singleton全局只有一个对象.
        request       ： 在一次Http请求中,容器会返回该Bean的同一实例.而对不同的Http请求则会产生新的Bean,而且该bean仅在当前Http Request内有效.
        session       ： 在一次Http Session中,容器会返回该Bean的同一实例.而对不同的Session请求则会创建新的实例,该bean实例仅在当前Session内有
                         效.
        global Session： 在一个全局的Http Session中,容器会返回该Bean的同一个实例,仅在使用portlet context时有效.
     

     4. bean的生命周期
        实例化一个Bean，也就是我们通常说的new。
        按照Spring上下文对实例化的Bean进行配置，也就是IOC注入。
        如果这个Bean实现了BeanNameAware接口，会调用它实现的setBeanName(String beanId)方法，此处传递的是Spring配置文件中Bean的ID。
        如果这个Bean实现了BeanFactoryAware接口，会调用它实现的setBeanFactory()，传递的是Spring工厂本身(可以用这个方法获取到其他Bean)。
        如果这个Bean实现了ApplicationContextAware接口，会调用setApplicationContext(ApplicationContext)方法，传入Spring上下文。
        如果这个Bean关联了BeanPostProcessor接口，将会调用postProcessBeforeInitialization(Object obj, String s)方法，BeanPostProcessor经常
        被用作是Bean内容的更改，并且由于这个是在Bean初始化结束时调用After方法，也可用于内存或缓存技术。
        如果这个Bean在Spring配置文件中配置了init-method属性会自动调用其配置的初始化方法。如果这个Bean关联了BeanPostProcessor接口，将会调用
        postAfterInitialization(Object obj, String s)方法。
        当Bean不再需要时，会经过清理阶段，如果Bean实现了DisposableBean接口，会调用其实现的destroy方法。
        最后，如果这个Bean的Spring配置中配置了destroy-method属性，会自动调用其配置的销毁方法。

     5. BeanFactory 和 ApplicationContext
        BeanFactory是spring中比较原始的Factory。如XMLBeanFactory就是一种典型的BeanFactory。
        原始的BeanFactory无法支持spring的许多插件，如AOP功能、Web应用等。
        ApplicationContext接口,它由BeanFactory接口派生而来，因而提供BeanFactory所有的功能。ApplicationContext以一种更向面向框架的方式工作
        以及对上下文进行分层和实现继承，ApplicationContext包还提供了以下的功能：
        • MessageSource, 提供国际化的消息访问
        • 资源访问，如URL和文件
        • 事件传播
        • 载入多个(有继承关系)上下文 ，使得每一个上下文都专注于一个特定的层次，比如应用的web层
        BeanFactory是延时加载bean,只有获取getBean的时候才会实例化Bean,如果Bean的某一个属性没有注入,则只有调用时才能发现.
        ApplicationContext，它是在容器启动时，一次性创建了所有的Bean。


     7. spring中用到的设计模式
        1. 代理模式 : aop中广泛使用
        2. 单例模式 : spring配置的bean默认为单例模式
        3. 模板方法 : 解决代码重复的问题.
        4. 前端控制器模式 : Spring提供了DispatcherServlet来对请求进行分发.
        5. 工厂模式 : BeanFactory用来创建对象的实例。
                     BeanFactory是Spring中最底层的接口，提供了最简单的容器的功能，只提供了实例化对象和拿对象的功能。而ApplicationContext是
                     Spring的一个更高级的容器，提供了更多的有用的功能。

     8. spring bean的生命周期
        @see com.jasmine.框架学习.Spring.spring生命周期.jpg
        @see J2EE.java.com.jasmine.框架学习.Spring.注册bean.PersonForAnnotation
        @see J2EE.java.com.jasmine.框架学习.Spring.注册bean.PersonTest

     9.Spring支持的几种bean的作用域
        @see com.jasmine.框架学习.Spring.注解.概念

     10. Spring框架中的单例Beans是线程安全的么？
        不是.
        大多数bean,如controller,service,dao都是无状态的bean,所以单例下其实是线程安全的.
        @see 概念
        多线程需要开发者自己去维护,最浅显的解决办法就是将多态bean的作用域由"singleton"变更为"prototype".
        Spring对一些Bean中非线程安全状态采用ThreadLocal进行处理，解决线程安全问题。
        @see com.jasmine.JavaBase.线程.线程本地_ThreadLocal

















































     */
}
