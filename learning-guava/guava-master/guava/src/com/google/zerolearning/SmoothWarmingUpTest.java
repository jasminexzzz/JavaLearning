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
        RateLimiter r = RateLimiter.create(1,10, TimeUnit.SECONDS);
        for (int j = 0; j < 60; j+=permits) {
            System.out.println("\n--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------" +
                    "--------------------------------------------------------------------------------");
            System.out.println(String.format("[%s: %s]", j, r.acquire(permits)));
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

}
