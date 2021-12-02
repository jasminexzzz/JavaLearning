package com.jasmine.java.base.C_线程.练习.信号量限流;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现一个流控程序。
 * 控制客户端每秒调用某个远程服务不超过N次
 * 客户端是会多线程并发调用
 *
 * @author : jasmineXz
 */
public class Test {

    private final static int MAX_QPS = 10;

    private final static Semaphore semaphore = new Semaphore(0);
    private final static AtomicInteger UNIT_CALL_COUNT = new AtomicInteger(0);
    private final static AtomicInteger TOTAL_TASK_COUNT = new AtomicInteger(0);

    public static void main(String... args) throws Exception {

        /* ================================================================================
         * 定时线程,用于清除信号量计数
         * ================================================================================ */
        ScheduledExecutorService poolSchedule = Executors.newScheduledThreadPool(1);
        poolSchedule.scheduleAtFixedRate(() -> {
            /*
             获取并返回立即可用的所有permits个数，并将可用permits置为0
             如果不重置,在信号量不没有全部被使用的情况下release会使许可变多,
             因为release归还许可其实是叠加的,如果剩余5个许可没有被使用,
             此时又释放了10个许可,那么信号量池中其实剩余15个许可
             */
            semaphore.drainPermits();
            // 重新设置为10
            semaphore.release(MAX_QPS);
            System.out.println("请求数: " + UNIT_CALL_COUNT.getAndSet(0));
        }, 0, 1000, TimeUnit.MILLISECONDS);

        long startTime = System.currentTimeMillis();

        /* ================================================================================
         * 任务线程,用于执行任务
         * ================================================================================ */
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            if (i == 5) {
                Thread.sleep(1000); // 模拟请求量不饱和的周期
            }
            pool.submit(() -> {
                semaphore.acquireUninterruptibly(1); // 获取信号量
                remoteCall();
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
        System.out.println(
                String.format("结束: 请求总数=%d TOTAL_TIME=%d",
                        TOTAL_TASK_COUNT.get(), (System.currentTimeMillis() - startTime)));

        poolSchedule.shutdown();
    }


    private static void remoteCall() {
        UNIT_CALL_COUNT.incrementAndGet();
        TOTAL_TASK_COUNT.incrementAndGet();
    }

}
