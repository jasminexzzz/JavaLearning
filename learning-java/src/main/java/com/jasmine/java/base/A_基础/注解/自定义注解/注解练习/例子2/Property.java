package com.jasmine.java.base.A_基础.注解.自定义注解.注解练习.例子2;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Property {
    String column();
    String type();
}
