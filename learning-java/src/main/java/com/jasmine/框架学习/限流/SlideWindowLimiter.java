package com.jasmine.框架学习.限流;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 滑动时间窗口限流工具
 * 本限流工具只适用于单机版，如果想要做全局限流，可以按本程序的思想，用redis的List结构去实现
 *
 * @author : jasmineXz
 */
public class SlideWindowLimiter {

    /**
     * 队列id和队列的映射关系，队列里面存储的是每一次通过时候的时间戳，这样可以使得程序里有多个限流队列
     * 通常是request为key, 限流器对象为value, 在拦截器或过滤器中进行判断
     */
    private volatile static Map<String, LinkedList<Long>> MAP = new ConcurrentHashMap<>();
    private static final Executor taskPool = Executors.newCachedThreadPool();
    private static final Lock reentrantLock = new ReentrantLock();

    private SlideWindowLimiter() {}

    public static void main(String[] args) {
        for (int i = 0 ; i < 3; i++ ) {
            taskPool.execute(new TestTask());
        }
    }

    /**
     * 滑动时间窗口限流算法
     * 在指定时间窗口，指定限制次数内，是否允许通过
     * 需要保证链表中的元素是按照从新到旧排列的, 即链表头为最新请求,链表尾为最旧请求
     *      [new] -> [mid] -> [old]
     *
     * @param limiterId  队列id
     * @param qps        限制次数
     * @param timeWindow 时间窗口大小
     * @return 是否允许通过
     */
    public static boolean limit(String limiterId, int qps, long timeWindow) {
        // 获取当前时间
        long nowTime = System.currentTimeMillis();
        // 根据队列id，取出对应的限流队列，若没有则创建
        LinkedList<Long> limiter = MAP.computeIfAbsent(limiterId, k -> new LinkedList<>());

        try {
            reentrantLock.lock();
            // 如果限流器未满, 则允许进入, 并且将当前时间戳放入链表头位置
            if (limiter.size() < qps) {
                limiter.addFirst(nowTime);
                return true;
            }

            // 限流已满(达到最大QPS), 则获取链表中最早时间戳, 即链表尾部
            Long earliestTime = limiter.getLast();
            if (nowTime - earliestTime <= timeWindow) {
                /*
                 如果: 当前时间戳 - 最早添加的时间戳 <= 窗口大小
                 说明这次请求早被限流的时间窗口内, 则阻止请求
                 */
                return false;
            } else {
                /*
                 如果: 当前时间戳 - 最早添加的时间戳 > 窗口大小
                 说明这次请求在时间窗口外, 则将本次请求添加入链表头, 并将链表尾部的删除
                 */
                limiter.removeLast();
                limiter.addFirst(nowTime);
                return true;
            }
        } finally {
            reentrantLock.unlock();
        }
    }
}

class TestTask implements Runnable {
    @Override
    public void run() {
        for (int i = 1 ; i <= 15 ; i++ ) {
            // 进入限流器
            System.out.println(Thread.currentThread().getName() + " : " + LocalTime.now().toString() + " : " +
                    SlideWindowLimiter.limit("ListId",10,1000L)
            );
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}
