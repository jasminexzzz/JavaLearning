package com.jasmine.A1_java.base.A_基础.反射;

public class 概念 {
    /*
     一、反射获取Class对象
         1. Class类的forName(String clazzName)静态方法,参数必须为全限定包名;
            例如: 如Class.forName("com.jasmine.JavaBase.反射.ClassTest")

         2. 某个类的class属性来获取类对应的Class对象
            例如: ClassTest.class

         3. 某个对象的getClass()方法
            例如: ClassTest ct = new ClassTest();
                  ct.getClass();

     二、利用反射来创建对象
         1. Class对象的newInstance()方法,要求Class对象有无参构造器,执行newInstance()方法就是调用无参构造器来创建对象实例.
         2. 先使用Class对象获取指定的Constructor对象,再调用Constructor的newInstance()方法.

     */
}
