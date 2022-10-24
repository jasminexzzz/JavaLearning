package com.jasmine.A1_java.base.C_线程.锁.不可重入锁;

@SuppressWarnings("all")
public class Count{

    Lock lock = new Lock();

    public void print() throws InterruptedException {
        lock.lock();
        System.out.println("print 加锁");
        doAdd();
        lock.unlock();
        System.out.println("print 释放锁");
    }

    public void doAdd() throws InterruptedException {
        //当前线程执行print()方法首先获取lock，接下来执行doAdd()方法就无法执行doAdd()中的逻辑，必须先释放锁。
        lock.lock();
        System.out.println("doAdd 加锁");
        //do something
        lock.unlock();
        System.out.println("doAdd 释放锁");
    }

    public static void main(String[] args) throws InterruptedException {
        Count c = new Count();
        c.print();
    }
}
