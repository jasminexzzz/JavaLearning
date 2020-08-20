package com.jasmine.JavaBase.C_线程.练习.修改数字.线程池;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile不能解决线程安全问题,对于i++,++i这类的可以使用AtomicInteger
 * @author : jasmineXz
 */
@SuppressWarnings("all")
public class NumPlus implements Runnable {
    private AtomicInteger num = new AtomicInteger(0);

    private void sync(){
        System.out.println(Thread.currentThread().getName() + ":" + num.incrementAndGet());
    }

    // run方法同样是线程执行体
    @Override
    public void run() {
        for (int i = 1 ; i <= 40 ; i++ ) {
            sync();
        }
    }


    public static void main(String[] args) {
        NumPlus n = new NumPlus();
        ExecutorService pool = Executors.newFixedThreadPool(6);
        pool.submit(n);
        pool.submit(n);
        pool.submit(n);
        pool.submit(n);

        pool.shutdown();
    }
}