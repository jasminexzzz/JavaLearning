package com.google.zerolearning;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author wangyf
 * @since 2.0.0
 */
public class SmoothWarmingUpTest {

    public static void main(String[] args) {
//        warmupSingle();
        warmup();
//        bursty();
    }

    private static void warmupSingle() {
        int permits = 3;
        long beginS = System.currentTimeMillis();
        RateLimiter r = RateLimiter.create(1,10, TimeUnit.SECONDS);
        System.out.println(">>>>>>>>>> 开始 <<<<<<<<<<\n\n");
        printLine();
        System.out.println(String.format("请求:%s, 经过:%s, 总时长:%s", 3,
                r.acquire(3), (System.currentTimeMillis() - beginS) / 1000.0));
        printLine();
        System.out.println(String.format("请求:%s, 经过:%s, 总时长:%s", 4,
                r.acquire(1), (System.currentTimeMillis() - beginS) / 1000.0));
    }


    private static void warmup() {
        int permits = 1;
        long beginS = System.currentTimeMillis();
        RateLimiter r = RateLimiter.create(1,10, TimeUnit.SECONDS);
        System.out.println(">>>>>>>>>> 开始 <<<<<<<<<<");
        for (int j = 1; j <= 10; j += permits) {
            printLine();
            System.out.println(
                String.format("请求:%s, 经过:%s, 总时长:%s",
                    j,
                    r.acquire(permits),
                    (System.currentTimeMillis() - beginS) / 1000.0));
        }

        long sleep = 4000;
        System.out.println(String.format("\n\n休眠%s毫秒\n\n", sleep));
        sleep(sleep);

        for (int j = 1; j <= 10; j += permits) {
            printLine();
            System.out.println(
                String.format("请求:%s, 经过:%s, 总时长:%s",
                    j,
                    r.acquire(permits),
                    (System.currentTimeMillis() - beginS) / 1000.0));
        }
    }

    private static void bursty() {
        int permits = 1;
        RateLimiter r = RateLimiter.create(1);
        for (int j = 0; j < 60; j+=permits) {
            printLine();
            System.out.println(String.format("[%s: %s]", j, r.acquire(permits)));
        }
    }

    private static void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printLine() {
        System.out.println("\n--------------------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------" +
                "--------------------------------------------------------------------------------");
    }

}
