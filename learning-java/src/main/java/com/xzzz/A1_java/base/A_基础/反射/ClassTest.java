package com.xzzz.A1_java.base.A_基础.反射;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

// 定义可重复注解
@Repeatable(Annos.class)
@interface Anno {}
@Retention(value= RetentionPolicy.RUNTIME)
@interface Annos {
    Anno[] value();
}
// 使用4个注解修饰该类
@SuppressWarnings(value="unchecked")
@Deprecated
// 使用重复注解修饰该类
@Anno
@Anno
public class ClassTest {
    // 为该类定义一个私有的构造器
    private ClassTest() {
    }
    // 定义一个有参数的构造器
    public ClassTest(String name) {
        System.out.println("执行有参数的构造器");
    }
    // 定义一个无参数的info方法
    public void info() {
        System.out.println("执行无参数的info方法");
    }
    // 定义一个有参数的info方法
    public void info(String str) {
        System.out.println("执行有参数的info方法"
                + "，其str参数值：" + str);
    }
    // 定义一个测试用的内部类
    class Inner {
    }

    public static void main(String[] args) throws Exception {
        // 下面代码可以获取ClassTest对应的Class
        Class<ClassTest> clazz = ClassTest.class;

        // 1.获取该Class对象所对应类的全部构造器
        Constructor[] ctors = clazz.getDeclaredConstructors();
        System.out.println("ClassTest的全部构造器如下：");
        for (Constructor c : ctors)
        {
            System.out.println(c);
        }

        // 2.获取该Class对象所对应类的全部public构造器
        Constructor[] publicCtors = clazz.getConstructors();
        System.out.println("ClassTest的全部public构造器如下：");
        for (Constructor c : publicCtors)
        {
            System.out.println(c);
        }

        // 3.获取该Class对象所对应类的全部public方法
        Method[] mtds = clazz.getMethods();
        System.out.println("ClassTest的全部public方法如下：");
        for (Method md : mtds)
        {
            System.out.println(md);
        }

        // 4.获取该Class对象所对应类的指定方法
        System.out.println("ClassTest里带一个字符串参数的info()方法为：" + clazz.getMethod("info" , String.class));
        // 5.获取该Class对象所对应类的上的全部注解
        Annotation[] anns = clazz.getAnnotations();
        System.out.println("ClassTest的全部Annotation如下：");
        for (Annotation an : anns)
        {
            System.out.println(an);
        }
        System.out.println("该Class元素上的@SuppressWarnings注解为：" + Arrays.toString(clazz.getAnnotationsByType(SuppressWarnings.class)));
        System.out.println("该Class元素上的@Anno注解为：" + Arrays.toString(clazz.getAnnotationsByType(Anno.class)));
        // 6.获取该Class对象所对应类的全部内部类
        Class<?>[] inners = clazz.getDeclaredClasses();
        System.out.println("ClassTest的全部内部类如下：");
        for (Class c : inners) {
            System.out.println(c);
        }
        // 使用Class.forName方法加载ClassTest的Inner内部类
        Class inClazz = Class.forName("com.xzzz.JavaBase.反射.ClassTest$Inner");
        // 7.通过getDeclaringClass()访问该类所在的外部类
        System.out.println("inClazz对应类的外部类为：" + inClazz.getDeclaringClass());
        System.out.println("ClassTest的包为：" + clazz.getPackage());
        System.out.println("ClassTest的父类为：" + clazz.getSuperclass());
    }
}

