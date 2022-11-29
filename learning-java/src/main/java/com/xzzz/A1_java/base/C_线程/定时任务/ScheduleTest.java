package com.xzzz.A1_java.base.C_线程.定时任务;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 动态变更间隔的定时任务
 */
@Slf4j
public class ScheduleTest {

    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    public static void main(String[] args) {
         func1();
//        func2(1);
    }

    /**
     * 方式1
     */
    private static void func1() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("方式1");
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 方式2
     * @param delay 延迟时间
     */
    private static void func2(int delay) {
        scheduledExecutorService.schedule(new Task(), delay, TimeUnit.SECONDS);
    }

    public static class Task implements Runnable {
        @Override
        public void run() {
            log.info("方式2");
            func2(1);
        }
    }

}
