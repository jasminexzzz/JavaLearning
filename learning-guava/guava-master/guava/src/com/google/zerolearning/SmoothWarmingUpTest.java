package com.google.zerolearning;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author wangyf
 * @since 2.0.0
 */
public class SmoothWarmingUpTest {

    public static void main(String[] args) {
        warmup();
//        bursty();
    }


    private static void warmup() {
        int permits = 1;
        long beginS = System.currentTimeMillis();
        RateLimiter r = RateLimiter.create(1,10, TimeUnit.SECONDS);
        for (int j = 0; j < 10; j += permits) {
            System.out.println("\n--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------");
            System.out.println(String.format("[%s秒: %s: %s]", (System.currentTimeMillis() - beginS) / 1000, j, r.acquire(permits)));
        }

        long sleep = 4000;
        System.out.println(String.format("\n\n休眠%s毫秒\n\n", sleep));
        sleep(sleep);

        for (int j = 0; j < 10; j += permits) {
            System.out.println("\n--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------");
            System.out.println(String.format("[%s秒: %s: %s]", (System.currentTimeMillis() - beginS) / 1000, j, r.acquire(permits)));
        }
    }

    private static void bursty() {
        int permits = 1;
        RateLimiter r = RateLimiter.create(1);
        for (int j = 0; j < 60; j+=permits) {
            System.out.println("\n--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------");
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

}
