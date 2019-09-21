package com.jasmine.JavaBase.A_基础.注解.自定义注解.注解练习.例子3;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {

    public static void main(String[] args) {
        Filter filter = new Filter();
        filter.setNum(1);

        Class clazz = filter.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields){
            System.out.println(f.getName());
            System.out.println("name:"+f.getAnnotation(FieldAnno.class).name()+"，  data:"+f.getAnnotation(FieldAnno.class).data());
            System.out.println("*********");
        }

        Method[] methods = clazz.getMethods();
        for (Method m : methods){
            if(m.getAnnotation(FieldAnno.class) != null){
                System.out.println(m.getAnnotation(FieldAnno.class).name());
            }
        }
    }
}
