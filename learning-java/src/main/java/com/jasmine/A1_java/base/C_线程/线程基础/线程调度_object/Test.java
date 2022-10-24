package com.jasmine.A1_java.base.C_线程.线程基础.线程调度_object;


/**
 *
 *
 * @author jasmineXz
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        Test t = new Test();

        /*
         * 创建3个线程,循环到第五次时等待唤醒
         */
        for (int j = 0 ; j < 3 ; j++) {
            new Thread(() -> {
                for (int i = 1; i <= 10; i++) {
                    t.test(i);
                }
            }).start();
        }

        Thread.sleep(4000);
        Thread.yield();
        System.out.println(Thread.currentThread().getName() + "唤醒子线程");

        // 只能唤醒一个线程
        synchronized (t) {
            t.notify();
        }

        // 可以唤醒全部线程
        synchronized (t) {
            t.notifyAll();
        }
    }

    synchronized void test (int i)  {
        try {
            System.out.println(Thread.currentThread().getName() + "子线程:" + i);
            if (i == 5) {
                System.out.println(Thread.currentThread().getName() + "子线程等待");
                wait();
                // wait(3000); // 阻塞3秒后进入就绪状态
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
