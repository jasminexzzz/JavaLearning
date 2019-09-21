package com.jasmine.JavaBase.线程.练习;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : jasmineXz
 */
public class Join的用法 implements Runnable{

    public AtomicInteger a = new AtomicInteger();

    @Override
    public void run() {
        for (int i = 0 ; i < 100 ; i ++){
            System.out.println(Thread.currentThread().getName() + " " + a.incrementAndGet());
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        Join的用法 j = new Join的用法();
        Thread t1 = new Thread(j,"线程1");
        Thread t2 = new Thread(j,"线程2");
        t1.start();
        /*
         * 通常不这样用
         */
        t1.join();
        t2.start();
        /*
         * 通常放在这里使用
         * 这样主线程可以获得多个线程运行后的结果,而不是直接执行下去.
         */
//        t1.join();
        System.out.println("=====>" + j.a);
    }

}
