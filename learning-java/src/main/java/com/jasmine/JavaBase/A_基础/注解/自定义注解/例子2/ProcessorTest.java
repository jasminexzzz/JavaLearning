package com.jasmine.JavaBase.A_基础.注解.自定义注解.例子2;


import java.lang.reflect.Method;

public class ProcessorTest {

    public static void process(String clazz)throws ClassNotFoundException{
        int passed = 0;
        int failed = 0;
        for(Method m : Class.forName(clazz).getMethods()){
            //如果该方法使用了@Testable注解
            if(m.isAnnotationPresent(Testable.class)){
                try {
                    m.invoke(null);
                    passed++;
                } catch (Exception e) {
                    System.out.println(e.getCause());
                    failed++;
                }
            }
        }
        System.out.println("共运行了"+(passed+failed)+"，成功"+passed+"，失败"+failed);
    }
}
