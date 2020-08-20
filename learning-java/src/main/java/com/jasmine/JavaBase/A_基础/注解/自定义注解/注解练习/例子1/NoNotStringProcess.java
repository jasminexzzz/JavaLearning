package com.jasmine.JavaBase.A_基础.注解.自定义注解.注解练习.例子1;

import java.lang.reflect.Field;

public class NoNotStringProcess {

    public static void process(Object obj)throws Exception{
        Class clazz = obj.getClass();
        Field[] f = clazz.getDeclaredFields();
        for(Field field : f){
            System.out.println(field.getName());
            System.out.println(field.get(obj));
//            System.out.println(field.getGenericType().getTypeName());
            System.out.println("*****************");



            if(!(field.getGenericType().getTypeName().equals("java.lang.String"))){
                throw new Exception("有所注解的字段类型不为String");
            }
        }
    }

}
