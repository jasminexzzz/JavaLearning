package com.jasmine.JavaBase.注解.自定义注解.注解练习.例子3;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAnno {
    String name();
    String data();
}
