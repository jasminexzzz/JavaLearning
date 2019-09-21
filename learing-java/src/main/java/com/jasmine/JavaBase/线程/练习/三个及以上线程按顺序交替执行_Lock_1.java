package com.jasmine.JavaBase.线程.练习;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : jasmineXz
 */
public class 三个及以上线程按顺序交替执行_Lock_1{

    static Lock lock = new ReentrantLock();
    static Condition c1 = lock.newCondition();
    static Condition c2 = lock.newCondition();
    static Condition c3 = lock.newCondition();

    private static int a;
    private static void add(){
        System.out.println(Thread.currentThread().getName() +" "+ ++a);
    }

    private static class t1 implements Runnable{
        @Override
        public void run() {
            try {
                for (int i = 0 ; i < 10 ; i++){
                    lock.lock();
                    c2.signal();
                    add();
                    c1.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class t2 implements Runnable{
        @Override
        public void run() {
            try {
                for (int i = 0 ; i < 10 ; i++){
                    lock.lock();
                    c3.signal();
                    add();
                    c2.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class t3 implements Runnable{
        @Override
        public void run() {
            try {
                for (int i = 0 ; i < 10 ; i++){
                    lock.lock();
                    c1.signal();
                    add();
                    c3.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }


    public static void main(String[] args) {
        Thread t1 = new Thread(new t1(),"111");
        Thread t2 = new Thread(new t2(),"222");
        Thread t3 = new Thread(new t3(),"333");
        t1.start();
        t2.start();
        t3.start();
    }
}

