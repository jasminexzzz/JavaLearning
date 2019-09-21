package com.jasmine.设计模式.单例模式_Singleton.静态内部类;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : jasmineXz
 */
public class Singleton {

    // 私有内部类，按需加载，用时加载，也就是延迟加载
    private static class Holder {
        private static Singleton singleton = new Singleton();
    }

    private Singleton() {

    }

    public static Singleton getInstance() {
        return Holder.singleton;
    }

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        for (int i = 0; i< 20; i++) {
            threadPool.execute(
                    () -> System.out.println(Thread.currentThread().getName()+"  :"+ Singleton.getInstance()));
        }
        threadPool.shutdown();
    }
}

