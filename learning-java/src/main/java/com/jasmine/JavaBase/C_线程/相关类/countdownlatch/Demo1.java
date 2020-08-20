package com.jasmine.JavaBase.C_线程.相关类.countdownlatch;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jasmineXz
 */
public class Demo1 {
    private static final int THREAD_NUM = 2;

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

        for (int i = 0; i < THREAD_NUM; i++) {
            try {
                threadPool.execute(new RunDemo(countDownLatch));
                if (i == 1) {
                    Thread.sleep(1000);
                }
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    private CountDownLatch countDownLatch;

    public RunDemo(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "准备就绪");
        try {
            countDownLatch.await();
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
