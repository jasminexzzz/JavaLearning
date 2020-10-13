package com.jasmine;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {

    private static final int COUNT_BITS = Integer.SIZE - 3;


    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                continue;
            }
            System.out.println(i);
        }
    }

}
