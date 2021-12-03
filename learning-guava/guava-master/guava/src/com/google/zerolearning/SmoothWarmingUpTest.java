package com.google.zerolearning;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author wangyf
 * @since 2.0.0
 */
public class SmoothWarmingUpTest {

    public static void main(String[] args) {
        RateLimiter r = RateLimiter.create(5,10, TimeUnit.SECONDS);
        int p = 0;
        int b = 0;
        for (int j = 0; j < 60; j++) {
            System.out.println(String.format("%s : %s", j, r.acquire(1)));
        }
    }
}
