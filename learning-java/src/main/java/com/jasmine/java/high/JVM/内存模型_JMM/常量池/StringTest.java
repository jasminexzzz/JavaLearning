package com.jasmine.java.high.JVM.内存模型_JMM.常量池;

/**
 * String常量池
 * String相加
 *
 * @author : jasmineXz
 */
@SuppressWarnings("all")
public class StringTest {

    /**
     * 两个String相加
     *
     */
    public static void strPlusStr(){
        String a = "a";//编译时在常量池创建 a
        String b = "b";//编译时在常量池创建 b
        String ab = "ab";
        String apb = a + b;
        String nab = new String("ab");

        System.out.println("ab  : " + ab);
        System.out.println("apb : " + apb);
        System.out.println("nab : " + nab);

        System.out.println("ab == apb      : " + (ab == apb));
        System.out.println("ab.equals(apb) : "+ab.equals(apb));
        System.out.println("ab == nab      : "+ (ab == nab));
        System.out.println("************************************");
    }

    /**
     * 两个String相加
     */
    public static void strPlusFianlStr(){
        final String a = "a";//
        final String b = "b";//
        String ab = "ab";
        String apb = a + b;// 对于final修饰的变量,编译时会被优化为 "ab"

        System.out.println("ab  : " + ab);

        System.out.println("ab == apb      : " + (ab == apb));
        System.out.println("************************************");
    }

    /**
     * 两个new String是否相等
     *
     */
    public static void nstrPlusNlStr(){
        String na1 = new String("a");
        String na2 = new String("a");

        System.out.println("na1 == na2      : " + (na1 == na2));
        System.out.println("************************************");

        String na3 = na1;
        System.out.println("na1 == na3      : " + (na1 == na3));
        System.out.println("************************************");
    }

    /** 两个Int相加 */
    public static void intPlusInt(){
        Integer a = 1;
        Integer b = 2;
        Integer ab = 3;
        Integer apb = 3;

        System.out.println("ab  : " + ab);
        System.out.println("apb : " + apb);

        System.out.println("ab == apb      : " + (ab == apb));
        System.out.println("ab.equals(apb) : "+ab.equals(apb));
        System.out.println("************************************");
    }

    /** 两个Double相加 */
    public static void doublePlusDouble(){
        Double a = 1d;
        Double b = 2d;
        Double ab = 3d;
        Double apb = 3d;

        System.out.println("ab  : " + ab);
        System.out.println("apb : " + apb);

        System.out.println("ab == apb      : " + (ab == apb));
        System.out.println("ab.equals(apb) : "+ab.equals(apb));
        System.out.println("************************************");
    }


    public static void doubleAddress(){
        double a1 = 1d;
        double a2 = 1d;

        final double a3 = 1d;
        final double a4 = 1d;

        System.out.println(a1 == a2);
        System.out.println(a1 == a3);
        System.out.println(a3 == a4);

    }


    public static void main(String[] args) {
        StringTest.strPlusFianlStr();
//        StringTest.doublePlusDouble();


    }
}
