package com.jasmine.JavaBase.注解.自定义注解.注解练习.例子1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AllAnnotationProcess{
    public static void process(Object obj) throws Exception{
        Class clazz = obj.getClass();

        // 获取类上的注解
        Annotation[] annos = clazz.getAnnotations();
        for (Annotation anno : annos) {
            System.out.println(clazz.getSimpleName().concat(".").concat(anno.annotationType().getSimpleName()));
        }


        Field[] f = clazz.getDeclaredFields();
        for(Field field : f){
            if(field.getAnnotation(NoNotString.class) == null){
                throw new Exception("有字段没有被NoNotString修饰!");
            }
            System.out.println(field.getAnnotation(NoNotString.class).annotationType().getSimpleName());
        }
    }
}
