package com.xzzz.A1_java.base.C_线程.相关类.TestQueueBlocking.例子1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer extends Thread {

    private BlockingQueue<String> bq;

    Producer(BlockingQueue<String> bq) {
        this.bq = bq;
    }

    public void run() {

        String[] strArr = new String[]{"aaa", "bbb", "ccc"};

        for (int i = 0 ; i < 5 ; i++ ) {
            System.out.println(getName() + " 准备向队列中插入");
            try {
                Thread.sleep(200);
                // 尝试放入元素，如果队列已满，线程被阻塞
                bq.put(strArr[i]);
            } catch (Exception ex){ex.printStackTrace();}
            System.out.println(getName() + "插入完成：" + bq);
        }
    }
}

class Consumer extends Thread {

    private BlockingQueue<String> bq;

    Consumer(BlockingQueue<String> bq) {
        this.bq = bq;
    }
    public void run() {

        while(true) {
            System.out.println("******** 队列准备取出！");
            try {
                Thread.sleep(5000);
                // 尝试取出元素，如果队列已空，线程被阻塞
                System.out.println(getName() + "取出完成：" + bq.take().toString());
            } catch (Exception ex){ex.printStackTrace();}

        }
    }
}
public class BlockingQueueTest2 {

    public static void main(String[] args) {
        // 创建一个容量为1的BlockingQueue
        BlockingQueue<String> bq = new ArrayBlockingQueue<>(3);
        // 启动3条生产者线程
        new Producer(bq).start();
        new Producer(bq).start();
        new Producer(bq).start();
        // 启动一条消费者线程
        new Consumer(bq).start();
    }

}
