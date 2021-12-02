package com.jasmine.java.base.C_线程.线程安全.线程锁_Lock.例子2;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
@SuppressWarnings("all")
public class Test {

    private int a = 0;

    private Lock lock = new ReentrantLock();    //注意这个地方

    public static void main(String[] args)  {
        final Test test = new Test();

        new Thread(){
            public void run() {
                for(int i = 0 ; i < 10 ; i++){
                    test.insert(Thread.currentThread());
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();

        new Thread(){
            public void run() {
                for(int i = 0 ; i < 10 ; i++){
                    test.insert(Thread.currentThread());
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }

    public void insert(Thread thread) {
        lock.lock();
        try {
//            System.out.println(thread.getName()+"得到了锁");
            a ++;
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
//            System.out.println(thread.getName()+"释放了锁");
            System.out.println(thread.getName()+"释放了锁："+a);
            lock.unlock();
        }
    }
}
