package com.xzzz.C1_framework.限流;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author : jasmineXz
 */
public class GuavaLimiter {
    static final RateLimiter rateLimiter = RateLimiter.create(60);

    public static void main(String[] args) {
        for (int i = 0; i < 40; i++) {
            System.out.println(rateLimiter.acquire());
        }

        System.out.println(rateLimiter.getRate());
    }
}
