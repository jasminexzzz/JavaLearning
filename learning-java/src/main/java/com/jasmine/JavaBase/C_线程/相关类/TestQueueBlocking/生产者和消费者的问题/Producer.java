package com.jasmine.JavaBase.C_线程.相关类.TestQueueBlocking.生产者和消费者的问题;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者线程
 * @author : jasmineXz
 */

public class Producer implements Runnable {

    private volatile boolean  isRunning = true;//是否在运行标志
    private BlockingQueue queue;//阻塞队列
    private static AtomicInteger count = new AtomicInteger();//自动更新的值
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    //构造函数
    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
//        String data = null;
        Random r = new Random();

        System.out.println("++++++++++ 启动生产者线程！");
        try {
            while (isRunning) {
//                System.out.println("+++++ 正在生产数据...");
                //随机睡眠某个时间,来模仿业务处理
                Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));//取0~DEFAULT_RANGE_FOR_SLEEP值的一个随机数

                int data = count.incrementAndGet();
                System.out.print("+++++ 放入：" + data);

                /* ******************************************************
                offer方法会判断队列是否已满,若满了则返回false
                设定的等待时间为2s，如果超过2s还没加进去返回true
                ********************************************************* */
//                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
//                    System.out.println("+++++ 放入数据失败：" + data);
//                }

                /* ******************************************************
                offer方法会判断队列是否已满,若满了则返回false
                设定的等待时间为2s，如果超过2s还没加进去返回true
                ********************************************************* */
                queue.put(data);
                System.out.println("     队列总数 : " + queue.size());

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("+++++ 退出生产者线程！");
        }
    }

    public void stop() {
        isRunning = false;
    }
}
