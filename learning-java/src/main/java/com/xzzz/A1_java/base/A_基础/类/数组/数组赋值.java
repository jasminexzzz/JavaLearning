package com.xzzz.A1_java.base.A_基础.类.数组;

public class 数组赋值 {
    public static void main(String[] args) {
        int[] a = {5,7,20};
        int[] b = new int[4];
        System.out.println(a.length);
        System.out.println(b.length);

        //下面可以看出，重新赋值并不是改变数组的长度，而是重新分配了引用变量对应的数据地址
        b = a;
        System.out.println(b.length);

        /*a = b;
        System.out.println(a.length);*/
    }
}
