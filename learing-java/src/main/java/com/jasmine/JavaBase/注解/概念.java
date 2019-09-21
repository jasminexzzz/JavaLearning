package com.jasmine.JavaBase.注解;

public class 概念 {
    /*
    一、元注解
        1. Retention (用于自定义注解)
           1). 注解的使用:
               @Retention(RetentionPolicy.RUNTIME)

           2). 注解解释:
               的作用是定义被它所注解的注解保留多久

           3). 注解参数:
               (1). SOURCE:  被编译器忽略
               (2). CLASS:   注解将会被保留在Class文件中，但在运行时并不会被JVM保留。这是默认行为，所有没有用Retention注解的注
                             解，都会采用这种策略。
               (3). RUNTIME: 保留至运行时。所以我们可以通过反射去获取注解信息。

************************************************************************************************************************
        2. Target (用于自定义注解)
           1). 注解的使用:
               @Target(ElementType.METHOD)

           2). 注解解释:
               说明了Annotation所修饰的对象范围

           3). 注解参数:
               (1).CONSTRUCTOR:    用于描述构造器
               (2).FIELD:          用于描述域
               (3).LOCAL_VARIABLE: 用于描述局部变量
               (4).METHOD:         用于描述方法
               (5).PACKAGE:        用于描述包
               (6).PARAMETER:      用于描述参数
               (7).TYPE:           用于描述类、接口(包括注解类型) 或enum声明

************************************************************************************************************************
        3. @Document(用于自定义注解)
           1). 注解的使用:
               @Document

           2). 注解解释:
               将注解包含在Javadoc中

************************************************************************************************************************
        4. Inherited(用于自定义注解)
           1). 注解的使用:
               @Inherited

           2). 注解解释:
               允许子类继承父类中的注解

************************************************************************************************************************
        5. SuppressWarnings
           1). 注解的使用:
               @SuppressWarnings("all")

           2). 注解解释:
               给编译器一条指令，告诉它对被注解的代码元素内部的某些警告保持静默。

           3). 注解参数:
               (1).deprecation:   使用了不赞成使用的类或方法时的警告
               (2).unchecked:     执行了未检查的转换时的警告，例如当使用集合时没有用泛型 (Generics) 来指定集合保存的类型。
               (3).fallthrough:   当 Switch 程序块直接通往下一种情况而没有 Break 时的警告。
               (4).path:          在类路径、源文件路径等中有不存在的路径时的警告。
               (5).serial:        当在可序列化的类上缺少 serialVersionUID 定义时的警告。
               (6).finally:       任何 finally 子句不能正常完成时的警告。
               (7).all:           关于以上所有情况的警告。

************************************************************************************************************************
        6. Override
           1). 注解的使用:
               @Override

           2). 注解解释:
               表示当前的方法定义将覆盖超类中的方法。

************************************************************************************************************************
        7. Deprecated
           1). 注解的使用:
               @Deprecated

           2). 注解解释:
               使用了注解为它的元素编译器将发出警告，因为注解@Deprecated是不赞成使用的代码，被弃用的代码。

************************************************************************************************************************

     */
}
