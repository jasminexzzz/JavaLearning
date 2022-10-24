package com.jasmine.A1_java.base.C_线程.锁.可重入锁;

@SuppressWarnings("all")
public class Count{

    LockTest lock = new LockTest();

    public void print() throws InterruptedException {
        System.out.println("print 加锁");
        lock.lock();
        doAdd();
        System.out.println("print 释放锁");
        lock.unlock();
    }

    public void doAdd() throws InterruptedException {
        System.out.println("doAdd 加锁");
        lock.lock();
        System.out.println("doAdd 释放锁");
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        Count c = new Count();
        c.print();
    }
}
