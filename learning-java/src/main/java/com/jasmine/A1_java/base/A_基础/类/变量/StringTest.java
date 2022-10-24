package com.jasmine.A1_java.base.A_基础.类.变量;

public class StringTest {
    public static void main(String[] args) {
        /**
         * JAVA会使用常量池来管理曾经用过的字符串直接量
         * 例如执行String a == "JAVA";常量池中就会缓存一个字符串"JAVA"
         * 如果程序再次执行String a == "JAVA";系统将会让b直接指向常量池中的"JAVA"字符串，因此a==b会返回true
         */


        String s1 = "S1";
        String s2 = "S1";

        //System.out.println(s1 == s2);


        String s3 = new String("S3");
        String s4 = new String("S3");

        //System.out.println(s3 == s4);
        //System.out.println(s3.equals(s4));

        Integer a = new Integer(1);
        Integer b = new Integer(1);
        System.out.println(a == b);
        System.out.println(a.equals(b));
    }
}
