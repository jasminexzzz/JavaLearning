package com.jasmine.JavaBase.C_线程.相关类.TestSemaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 需要控制并发量的场景中
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
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " 释放许可");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
