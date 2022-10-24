package com.xzzz.A1_java.base.A_基础.类.数组;

public class 数组初始化 {

    //数组初始化时会给默认值 因为int类型默认为0 所以为0 若为String等对象 则为null
    public static void main(String[] args) {
        int[] a;
        a = new int[3];

        for (int i = 0 ; i < a.length ; i++){
            System.out.println(a[i]);
        }
    }
}
