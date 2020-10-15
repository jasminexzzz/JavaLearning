package com.jasmine.JavaBase.C_线程.相关类.TestQueueBlocking.生产者和消费者的问题;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 消费者线程
 * @author : jasmineXz
 */

public class Consumer implements Runnable {

    private BlockingQueue queue;
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    //构造函数
    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        System.out.println("───── 启动消费者线程！");
        Random r = new Random();
        boolean isRunning = true;
        try {
            while (isRunning) {

                /* ******************************************************
                poll方法会判断队列是否有元素,若没有则等待设定时间,时间过后返回null
                有数据时直接从队列的队首取走，无数据时阻塞，在2s内有数据，取走，超过2s还没数据，返回失败
                ********************************************************* */
//                AtomicInteger data = queue.poll(2, TimeUnit.SECONDS);
//                if (null != data) {
//                    System.out.println("───── 取出：" + data);
//                    Thread.sleep(3000);//休眠10秒,等待队列放满
//                } else {
//                    // 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
//                    isRunning = false;
//                }


                /* ******************************************************
                获取队列的第一个元素,没有则等待
                有数据时直接从队列的队首取走，无数据时阻塞，在2s内有数据，取走，超过2s还没数据，返回失败
                ********************************************************* */
//                takeLast()
                int data = (int)queue.take();
                System.out.println("───── 取出：" + data);
                Thread.sleep(5000);//休眠3秒,等待队列放满

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("───── 退出消费者线程！");
        }
    }


}
