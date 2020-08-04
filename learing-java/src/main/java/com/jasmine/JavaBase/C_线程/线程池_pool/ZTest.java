package com.jasmine.JavaBase.C_线程.线程池_pool;

/**
 * @author : jasmineXz
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ZTest {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,  //核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
                10, //线程池最大能容忍的线程数
                200, //线程存活时间
                TimeUnit.MILLISECONDS, //参数keepAliveTime的时间单位
                new ArrayBlockingQueue<Runnable>(5) //任务缓存队列，用来存放等待执行的任务
        );

        for(int i = 0 ; i < 15 ; i++) {
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
//            System.out.println(
//                    "线程池中线程数目：" + executor.getPoolSize()+
//                    "，队列中等待执行的任务数目：" + executor.getQueue().size()+
//                    "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
        }
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                long time = System.currentTimeMillis() - start;
                System.out.println("程序结束了，总耗时：" + time + " ms(毫秒)！\n");
                break;
            }
        }
    }
}
