package com.jasmine.A1_java.high.JVM.类加载机制.ClassLoader;

/**
 * @author : jasmineXz
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println(System.getProperty("sun.boot.class.path"));
//        System.out.println(System.getProperty("java.ext.dirs"));
//        System.out.println(System.getProperty("java.class.path"));
//        ClassLoader cl = Test.class.getClassLoader();
//
//        System.out.println("ClassLoader is:"+cl.toString());
//        System.out.println("ClassLoader is:"+cl.getParent().toString());

        classGetResource();
    }

    /**
     * 测试getResource
     */
    private static void classGetResource(){
        /*
        获取该类编译后的详细地址
        file:/E:/WorkSpace/MySelfProject/MyJavaLearing/learing-java/target/classes/com/jasmine/Java高级/JVM/类加载机制/ClassLoader/
        */
        System.out.println(Test.class.getResource(""));

        /*
        获取该项目classpath根目录
        file:/E:/WorkSpace/MySelfProject/MyJavaLearing/learing-java/target/classes/
         */
        System.out.println(Test.class.getResource("/"));


        /*
        获取该类的
         */
        System.out.println(Test.class.getClassLoader().getResource(""));


        /*
        Null
         */
        System.out.println(Test.class.getClassLoader().getResource("/"));
        System.out.println(Test.class.getClassLoader().getResource("META-INF/spring.factories"));
    }
}
