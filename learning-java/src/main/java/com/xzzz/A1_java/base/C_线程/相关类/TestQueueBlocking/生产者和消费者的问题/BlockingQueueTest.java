package com.xzzz.A1_java.base.C_线程.相关类.TestQueueBlocking.生产者和消费者的问题;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author : jasmineXz
 */

public class BlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {
        // 声明一个容量为10的缓存队列
        BlockingQueue queue = new LinkedBlockingQueue(10);

        //new了三个生产者和一个消费者
        Producer producer1 = new Producer(queue);
//        Producer producer2 = new Producer(queue);
//        Producer producer3 = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        // 借助Executors创建线程池
        ExecutorService service = Executors.newCachedThreadPool();
        // 启动线程
        service.execute(producer1);
//        service.execute(producer2);
//        service.execute(producer3);
        service.execute(consumer);

        // 执行10s
//        Thread.sleep(10 * 1000);
//        producer1.stop();
//        producer2.stop();
//        producer3.stop();

//        Thread.sleep(2000);
        // 退出Executor
//        service.shutdown();
    }
}
