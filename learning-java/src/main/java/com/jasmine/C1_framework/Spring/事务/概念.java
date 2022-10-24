package com.jasmine.C1_framework.Spring.事务;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**
     @link https://segmentfault.com/a/1190000013341344
     @link https://github.com/TmTse/transaction-test


     一. 事务传播
        什么是事务传播行为?
        事务传播行为用来描述由某一个事务传播行为修饰的方法被嵌套进另一个方法的时事务如何传播.也就是嵌套的方法之间事务如何处理.

        如下列代码:
        public void methodA(){
        methodB();
            //doSomething
        }

        @Transaction(Propagation=XXX)
        public void methodB(){
            //doSomething
        }

    2. Spring中七种事务传播行为
        事务传播行为类型	              说明
      ================================================================================================================================
        PROPAGATION_REQUIRED	    : 如果当前没有事务,就新建一个事务,如果已经存在一个事务中,加入到这个事务中.这是最常见的选择.
            NoT_R : 没有注解
            T_R   : @Transaction(Propagation=PROPAGATION_REQUIRED)
            场景1 : 外围方法没有开启事务
                NoT_R Method_A ─┐
                                ├─ T_R Method_a
                                ├─ T_R Method_b
                                └─ throw Exception 不影响a,b事务的进行

                NoT_R Method_A ─┐
                                ├─ T_R Method_a
                                └─ T_R Method_b throw Exception 不影响a事务,b事务回滚
'
            场景2 : 外围方法开启事务
                  T_R Method_A ─┐
                                ├─ T_R Method_a
                                ├─ T_R Method_b
                                └─ throw Exception {A,a,b} 三个事务合并为同一事务,A抛出异常 {A,a,b} 全部回滚

                  T_R Method_A ─┐
                                ├─ T_R Method_a throw Exception 事务为整体事务,a抛出异常 {A,a,b} 全部回滚
                                └─ T_R Method_b throw Exception 事务为整体事务,b抛出异常 {A,a,b} 全部回滚

                  T_R Method_A ─┐
                                ├─ T_R Method_a
                                ├─ try
                                ├─   T_R Method_b throw Exception
                                ├─ catch
                                └─   Exception 事务为整体事务,b抛出异常被外部捕获 {A,a,b} 全部回滚

            总结 : 嵌套的事务为一个整体.
      ================================================================================================================================
        PROPAGATION_REQUIRES_NEW	: 新建事务,如果当前存在事务,把当前事务挂起.

            从字面即可知道,new,每次都要一个新事务,该传播级别的特点是,每次都会新建一个事务,并且同时将上下文中的事务挂起,执行当前新建事务完成以
        后,上下文事务恢复再执行.
            这是一个很有用的传播级别,举一个应用场景：现在有一个发送100个红包的操作,在发送之前,要做一些系统的初始化、验证、数据记录操作,然后发
        送100封红包,然后再记录发送日志,发送日志要求100%的准确,如果日志不准确,那么整个父事务逻辑需要回滚.
            怎么处理整个业务需求呢?就是通过这个PROPAGATION_REQUIRES_NEW 级别的事务传播控制就可以完成.发送红包的子事务不会直接影响到父事务的
        提交和回滚.

            场景1 : 外围方法没有开启事务
                NoT_R Method_A ─┐
                                ├─ T_RN Method_a
                                ├─ T_RN Method_b
                                └─ throw Exception 不影响a,b事务的进行

                NoT_R Method_A ─┐
                                ├─ T_RN Method_a
                                └─ T_RN Method_b throw Exception 不影响a事务,b事务回滚

            场景2 : 外围方法开启事务
                  T_R Method_A ─┐
                                ├─ T_R  Method_a
                                ├─ T_RN Method_b
                                ├─ T_RN Method_c
                                └─ throw Exception 不影响b,c事务的进行,a事务回滚

                  T_R Method_A ─┐
                                ├─ T_R  Method_a
                                ├─ T_RN Method_b
                                └─ T_RN Method_c throw Exception c不影响b,但c抛出异常被A感知,所以 {a,c} 都回滚

                  T_R Method_A ─┐
                                ├─ T_R  Method_a
                                ├─ T_RN Method_b
                                ├─ try
                                ├─   T_RN Method_c throw Exception
                                ├─ catch
                                └─   Exception c不影响b,b抛出异常被外部捕获,c回滚,a不受影响

            总结 : 每次开启的事务都是新的事务,但抛出异常不被捕获依然会影响外层,被捕获则不会影响.
        ==============================================================================================================================
        PROPAGATION_NESTED	        : 如果当前存在事务,则在嵌套事务内执行.如果当前没有事务,则执行与PROPAGATION_REQUIRED类似的操作.

            场景1 : 外围方法没有开启事务
                  NoT Method_A ─┐
                                ├─ T_NT Method_a
                                ├─ T_NT Method_b
                                └─ throw Exception 不影响a,b事务的进行

                  NoT Method_A ─┐
                                ├─ T_NT Method_a
                                └─ T_NT Method_b throw Exception 不影响a事务,b事务回滚

            场景2 : 外围方法开启事务
                    T Method_A ─┐
                                ├─ T_NT Method_a
                                ├─ T_NT Method_b
                                └─ throw Exception A会影响a,b的事务,A回滚 {a,b} 都回滚

                    T Method_A ─┐
                                ├─ T_NT Method_a
                                └─ T_NT Method_b throw Exception b会影响a和A,整体都回滚.

                    T Method_A ─┐
                                ├─ T_NT Method_a
                                ├─ try
                                ├─   T_NT Method_b throw Exception
                                ├─ catch
                                └─   Exception b被捕获时,不会影响外部事务,只对b回滚.

            总结 : 内部是外部的子事务.
                   外部回滚,内部一定回滚.
                   内部回滚,外部接收就回滚,捕获就不回滚.

        ==============================================================================================================================
        PROPAGATION_SUPPORTS	    : 支持当前事务,如果当前没有事务,就以非事务方式执行.

            从字面意思就知道,supports,支持,该传播级别的特点是,如果上下文存在事务,则支持事务加入事务,如果没有事务,则使用非事务的方式执行.所以
        说,并非所有的包在transactionTemplate.execute中的代码都会有事务支持.这个通常是用来处理那些并非原子性的非核心业务逻辑操作.应用场景较少.

        ==============================================================================================================================
        PROPAGATION_MANDATORY	    : 使用当前的事务,如果当前没有事务,就抛出异常.

            该级别的事务要求上下文中必须要存在事务,否则就会抛出异常！配置该方式的传播级别是有效的控制上下文调用代码遗漏添加事务控制的保证手段.
        比如一段代码不能单独被调用执行,但是一旦被调用,就必须有事务包含的情况,就可以使用这个传播级别.

        ==============================================================================================================================
        PROPAGATION_NOT_SUPPORTED	: 以非事务方式执行操作,如果当前存在事务,就把当前事务挂起.

            这个也可以从字面得知,not supported ,不支持,当前级别的特点就是上下文中存在事务,则挂起事务,执行当前逻辑,结束后恢复上下文的事务.
            这个级别有什么好处?可以帮助你将事务极可能的缩小.我们知道一个事务越大,它存在的风险也就越多.所以在处理事务的过程中,要保证尽可能的缩
        小范围.比如一段代码,是每次逻辑操作都必须调用的,比如循环1000次的某个非核心业务逻辑操作.这样的代码如果包在事务中,势必造成事务太大,导致出
        现一些难以考虑周全的异常情况.所以这个事务这个级别的传播级别就派上用场了.用当前级别的事务模板抱起来就可以了.

        ==============================================================================================================================
        PROPAGATION_NEVER	        : 以非事务方式执行,如果当前存在事务,则抛出异常.

            该事务更严格,上面一个事务传播级别只是不支持而已,有事务就挂起,而PROPAGATION_NEVER传播级别要求上下文中不能存在事务,一旦有事务,就抛
        出runtime异常,强制停止执行！这个级别上辈子跟事务有仇.

        ==============================================================================================================================
















     */
}
