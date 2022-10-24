package com.jasmine.A1_java.base.A_基础.注解.自定义注解.例子1;

import java.lang.annotation.Annotation;

public class A {

    public static void main(String[] args) {
        try {
            Annotation[] arr = Class.forName("com.jasmine.A1_java.base.A_基础.注解.自定义注解.例子1.MyTest").getMethod("info1").getAnnotations();
            for(Annotation a : arr){
                System.out.println(a);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
