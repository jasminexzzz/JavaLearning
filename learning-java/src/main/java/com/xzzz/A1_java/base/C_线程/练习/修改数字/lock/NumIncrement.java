package com.xzzz.A1_java.base.C_线程.练习.修改数字.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程把数字加到100
 */
@SuppressWarnings("all")
public class NumIncrement implements Runnable {
    private int num;
    private final ReentrantLock lock = new ReentrantLock(true);//默认非公平锁,传入true创建公平锁

    private final Condition cond = lock.newCondition();

    private void sync(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + ":" + ++num);
        } finally {
            lock.unlock();
        }
    }

    private void notSync(){
        System.out.println("run --> "+Thread.currentThread().getName() + ":" + ++num);
    }

    @Override
    public void run() {
        for (int i = 1 ; i <= 500 ; i++ ) {
            sync();
        }
    }


    public static void main(String[] args) {
        NumIncrement st = new NumIncrement();     // ①
        // 通过new Thread(target , name)方法创建新线程
        new Thread(st , "新线程1").start();
        new Thread(st , "新线程2").start();
        new Thread(st , "新线程3").start();
        new Thread(st , "新线程4").start();
    }
}

