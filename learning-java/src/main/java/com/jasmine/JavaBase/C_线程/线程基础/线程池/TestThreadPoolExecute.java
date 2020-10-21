package com.jasmine.JavaBase.C_线程.线程基础.线程池;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 没返回值
 * @author : jasmineXz
 */
public class TestThreadPoolExecute {

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        /**
         * @param corePoolSize    : 核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
         * @param maximumPoolSize : 线程池最大能容忍的线程数
         * @param keepAliveTime   : 线程存活时间
         * @param unit            : 参数keepAliveTime的时间单位
         * @param workQueue       : 任务缓存队列，用来存放等待执行的任务
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            2,
            200,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3)
        );

        for(int i = 0 ; i < 3 ; i++) {
            RunDemo runDemo = new RunDemo(i);
            executor.execute(runDemo);
//            System.out.println(
//                    "线程池中线程数目：" + executor.getPoolSize()+
//                    "，队列中等待执行的任务数目：" + executor.getQueue().size()+
//                    "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
        }

        Thread.sleep(10000);
        System.out.println("线程池中执行完毕");
//        executor.shutdown();
//        while (true) {
//            if (executor.isTerminated()) {
//                long time = System.currentTimeMillis() - start;
//                System.out.println("程序结束了，总耗时：" + time + " ms(毫秒)！\n");
//                break;
//            }
//        }
    }
}

class RunDemo implements Runnable{
    private int taskNum;

    RunDemo(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("task ["+taskNum+"] 正在执行");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task ["+taskNum+"] 执行完毕");
    }
}
