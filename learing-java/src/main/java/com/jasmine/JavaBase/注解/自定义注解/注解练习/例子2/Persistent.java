package com.jasmine.JavaBase.注解.自定义注解.注解练习.例子2;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Persistent {
    String table();
}
