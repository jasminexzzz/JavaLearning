package com.xzzz.A1_java.base.A_基础.注解.自定义注解.重复注解;


import java.lang.annotation.*;
// 指定该注解信息会保留到运行时
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tags
{
    // 定义value成员变量，该成员变量可接受多个@FkTag注解
    Tag[] value();
}
