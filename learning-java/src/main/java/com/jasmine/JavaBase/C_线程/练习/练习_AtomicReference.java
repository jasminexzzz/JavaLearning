package com.jasmine.JavaBase.C_线程.练习;


import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : jasmineXz
 */
public class 练习_AtomicReference implements Runnable {
    String s = new String("0");

    public void add(){
        s = new String (Integer.parseInt(s)+1 + "");
        System.out.println(Thread.currentThread().getName() + " " + s);
    }

    @Override
    public void run() {
        for (int i = 0 ; i < 100 ; i++){
            add();
        }
    }


    public static void main(String[] args) {
        练习_AtomicReference lx = new 练习_AtomicReference();
//        Thread t1 = new Thread(lx,"线程1");
//        Thread t2 = new Thread(lx,"线程2");

        AtomicReference<练习_AtomicReference> a = new AtomicReference<>(lx);
        Thread t1 = new Thread(lx,"线程1");
        Thread t2 = new Thread(lx,"线程2");

        t1.start();
        t2.start();
    }

}
