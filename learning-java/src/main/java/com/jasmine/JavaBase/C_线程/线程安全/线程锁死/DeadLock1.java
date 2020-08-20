package com.jasmine.JavaBase.C_线程.线程安全.线程锁死;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : jasmineXz
 */

class lockTest implements Runnable{

    private Lock lock1;
    private Lock lock2;
    private String name;

    public lockTest(String name,Lock lock1,Lock lock2){
        this.name = name;
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        try {
            lock1.lock();
            lock1.tryLock();
            lock1.lockInterruptibly();
            TimeUnit.MILLISECONDS.sleep(10);//更好的触发死锁
            lock2.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
            lock2.unlock();
            System.out.println(Thread.currentThread().getName()+"正常结束!");
        }
    }
}

public class DeadLock1 {
    public static void main(String[] args) throws InterruptedException {

        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        Thread t1 = new Thread(new lockTest("lock1",lock1,lock2));
        Thread t2 = new Thread(new lockTest("lock2",lock2,lock1));

        t1.start();
        t2.start();

        System.out.println("主线程开始等待t1和t2卡死");
        TimeUnit.MILLISECONDS.sleep(5000);//更好的触发死锁
        System.out.println("等待完毕");
        t1.interrupt();//如果不终止此线程,则卡死将永远持续下去


    }
}
