package com.jasmine.JavaBase.C_线程.相关类.TestSemaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 需要控制并发量的场景中
 *
 * availablePermits()              : 返回此Semaphore对象中当前可用的permits个数
 * drainPermits()                  : 获取并返回立即可用的所有permits个数，并将可用permits置为0
 *
 * void acquire()                  : 从信号量获取一个许可，如果无可用许可前 将一直阻塞等待，
 * void acquire(int permits)       : 获取指定数目的许可，如果无可用许可前  也将会一直阻塞等待
 * void acquireUninterruptibly()   : 使等待进入acquire()方法的线程，不允许被中断
 * boolean tryAcquire()            : 从信号量尝试获取一个许可，如果无可用许可，直接返回false，不会阻塞
 * boolean tryAcquire(int permits) : 尝试获取指定数目的许可，如果无可用许可直接返回false
 *
 * void release()                  : 释放一个许可，别忘了在finally中使用
 *                                   注意：多次调用该方法，会使信号量的许可数增加，达到动态扩展的效果，
 *                                   如：初始permits 为1， 调用了两次release，最大许可会改变为2
 *
 *
 * @author : jasmineXz
 */
public class TestSemaphore {
    private static final int THREAD_NUM = 10;

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < THREAD_NUM; i++) {
            threadPool.execute(new RunDemo(semaphore));
        }

        // 线程池不在接收新任务，但是还是会处理阻塞队列中的任务
        // 这里只会中断闲置线程（那些挂起在阻塞队列上的线程）
        threadPool.shutdown();
        while (true) {
            if (threadPool.isTerminated()) {
                System.out.println("线程全部执行完毕");
                break;
            }
        }
    }

}

class RunDemo implements Runnable {

    private Semaphore semaphore;

    RunDemo(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " 获取到许可");
            // 持有许可5秒后释放
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " 释放许可");
        }
    }
}
