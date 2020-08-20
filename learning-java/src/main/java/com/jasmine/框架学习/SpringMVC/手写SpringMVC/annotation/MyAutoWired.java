package com.jasmine.框架学习.SpringMVC.手写SpringMVC.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutoWired {
    String value() default "";
}
