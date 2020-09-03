package com.jasmine;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {



    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        int nThreads = Runtime.getRuntime().availableProcessors();
        System.out.println(nThreads);

    }

}