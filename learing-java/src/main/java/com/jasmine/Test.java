package com.jasmine;



public class Test {

    public synchronized static void main(String[] args) {
        int a = 2 << 6;
        System.out.println(a);
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(a-1));
        System.out.println(a * 0.75f);
    }
}