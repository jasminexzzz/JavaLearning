package com.jasmine.java.base.C_线程.线程安全.线程锁死;

/**
 * 该线程锁死的原因为互相调用时对方都在锁状态，所以互相都等待对方释放锁，这样成了死锁
 * A.foo()方法被锁时，其实锁的是this，也就是A的对象，而并不是foo方法，所以是无法调用对应的last()的
 */
class A{
    public synchronized void foo( B b ) throws InterruptedException {
        System.out.println("当前线程名: " + Thread.currentThread().getName() + " 进入了A实例的foo()方法" );     // ①
        Thread.sleep(200);
        System.out.println("当前线程名: " + Thread.currentThread().getName() + " 企图调用B实例的last()方法");    // ③
        b.last();
    }
    public synchronized void last() {
        System.out.println("进入了A类的last()方法内部");
    }
}

class B {
    public synchronized void bar( A a ) throws InterruptedException {
        System.out.println("当前线程名: " + Thread.currentThread().getName() + " 进入了B实例的bar()方法" );   // ②
        Thread.sleep(200);
        System.out.println("当前线程名: " + Thread.currentThread().getName() + " 企图调用A实例的last()方法");  // ④
        a.last();
    }
    public synchronized void last() {
        System.out.println("进入了B类的last()方法内部");
    }
}

public class DeadLock implements Runnable {
    A a = new A();
    B b = new B();

    public void init() {
        Thread.currentThread().setName("主线程");
        // 调用a对象的foo方法
        try {
            a.foo(b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("进入了主线程之后");
    }

    public void run() {
        Thread.currentThread().setName("副线程");
        // 调用b对象的bar方法
        try {
            b.bar(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("进入了副线程之后");
    }


    public static void main(String[] args) {
        DeadLock dl = new DeadLock();
        // 以dl为target启动新线程
        new Thread(dl).start();
        // 调用init()方法
        dl.init();
    }
}

