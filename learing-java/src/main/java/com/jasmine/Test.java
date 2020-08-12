package com.jasmine;


import java.util.HashMap;

public class Test {

    static final int MAXIMUM_CAPACITY = 1 << 30;


    public static void main(String[] args) {

//        HashMap<String,Object> map = new HashMap<>(50);
//        map.put("1","1");
        System.out.println(tableSizeFor(50));

    }

    public static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

}