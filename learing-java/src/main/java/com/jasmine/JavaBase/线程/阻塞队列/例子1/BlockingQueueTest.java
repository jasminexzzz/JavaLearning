package com.jasmine.JavaBase.线程.阻塞队列.例子1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {

    public static void main(String[] args) throws Exception {
        // 定义一个长度为2的阻塞队列
        BlockingQueue<String> bq = new ArrayBlockingQueue<>(2);
        bq.put("Java"); // 与bq.add("Java"、bq.offer("Java")相同
        bq.put("Java"); // 与bq.add("Java"、bq.offer("Java")相同
        bq.put("Java"); // ① 阻塞线程。
    }

}
