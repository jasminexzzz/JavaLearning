package com.xzzz.A1_java.base.A_基础.注解.自定义注解.重复注解;


import java.lang.annotation.*;

// 指定该注解信息会保留到运行时
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// 表示该注解是重复注解
@Repeatable(Tags.class)
public @interface Tag
{
    // 为该注解定义2个成员变量
    String name() default "重复注解";
    int age();
}
