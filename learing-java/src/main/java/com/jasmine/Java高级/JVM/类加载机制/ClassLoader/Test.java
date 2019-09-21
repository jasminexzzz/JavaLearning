package com.jasmine.Java高级.JVM.类加载机制.ClassLoader;

/**
 * @author : jasmineXz
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("sun.boot.class.path"));
//        System.out.println(System.getProperty("java.ext.dirs"));
//        System.out.println(System.getProperty("java.class.path"));
        ClassLoader cl = Test.class.getClassLoader();

        System.out.println("ClassLoader is:"+cl.toString());
        System.out.println("ClassLoader is:"+cl.getParent().toString());

    }
}
