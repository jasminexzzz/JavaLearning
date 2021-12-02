package com.jasmine.java.base.C_线程.相关类.TestCountDownLatch;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jasmineXz
 */
public class TestCountDownLatch {
    private static final int THREAD_NUM = 5;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

        for (int i = 0; i < THREAD_NUM; i++) {
            threadPool.execute(new RunDemo(countDownLatch));
        }
        // 可能线程还未执行就执行了countDown,所以此处稍微等一下
        Thread.sleep(1000);
        for (int i = 0 ; i < THREAD_NUM ; i++) {
            countDownLatch.countDown();
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

    RunDemo(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 准备就绪");
        try {
            countDownLatch.await();
            System.out.println(Thread.currentThread().getName() + " 开始");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
