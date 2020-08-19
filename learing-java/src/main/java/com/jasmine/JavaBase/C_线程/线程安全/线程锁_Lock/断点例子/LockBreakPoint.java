package com.jasmine.JavaBase.C_线程.线程安全.线程锁_Lock.断点例子;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 此例子方便查看AQS断点和LOCK锁实现
 * @author jasmineXz
 */
public class LockBreakPoint {


    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Runnable r = new RunDemo(lock);
        new Thread(r,"T1").start();
        new Thread(r,"T2").start();

    }
}

class RunDemo implements Runnable {

    static long one_m = 1000 * 60;
    static long one_h = 1000 * 60 * 60;

    Lock lock;

    public RunDemo(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "获得锁");
            Thread.sleep(one_h);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放锁");
            lock.unlock();
        }
    }
}
