package com.jasmine.java.base.C_线程.练习;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程交替进行
 * @author : jasmineXz
 */
public class 两个线程按顺序交替输出_Lock_1 implements Runnable{

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    private int a;

    private void add(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() +" "+ ++a);
            condition.signal();
            condition.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        for (int i = 0 ; i < 1000 ; i++){
            add();
        }
    }

    public static void main(String[] args) {
        两个线程按顺序交替输出_Lock_1 s = new 两个线程按顺序交替输出_Lock_1();
        Thread t1 = new Thread(s,"- - -");
        Thread t2 = new Thread(s,"+ + +");

        t1.start();
        t2.start();
    }
}
