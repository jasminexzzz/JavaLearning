package com.jasmine.JavaBase.线程.练习.修改数字.VolatileAndAtomic;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile不能解决线程安全问题,对于i++,++i这类的可以使用AtomicInteger
 * @author : jasmineXz
 */
@SuppressWarnings("all")
public class NumPlus implements Runnable {
//    private volatile int num;
    private AtomicInteger num = new AtomicInteger(0);

    private void sync(){
//        System.out.println(Thread.currentThread().getName() + ":" + ++num);
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
        for (int i = 0; i < 20;  i++) {
            if (i == 5){
                NumPlus st = new NumPlus();     // ①
                // 通过new Thread(target , name)方法创建新线程
                new Thread(st , "新线程1").start();
                new Thread(st , "新线程2").start();
            }
        }
    }
}