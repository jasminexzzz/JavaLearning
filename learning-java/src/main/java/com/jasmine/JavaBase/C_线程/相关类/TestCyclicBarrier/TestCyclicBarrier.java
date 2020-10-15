package com.jasmine.JavaBase.C_线程.相关类.TestCyclicBarrier;


import java.util.concurrent.*;

/**
 * 循环栅栏
 *
 * @author jasmineXz
 */
public class TestCyclicBarrier {
    private static final int THREAD_NUM = 5;

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        CyclicBarrier  cyclicBarrier = new CyclicBarrier(THREAD_NUM,() -> {
            System.out.println("最后到达的任务额外执行的内容");
        });

        for (int i = 0; i < THREAD_NUM; i++) {
            threadPool.execute(new RunDemo(cyclicBarrier));
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

    private CyclicBarrier cyclicBarrier;

    RunDemo(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 进入栅栏");
        try {
            cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName() + " 开始");
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
