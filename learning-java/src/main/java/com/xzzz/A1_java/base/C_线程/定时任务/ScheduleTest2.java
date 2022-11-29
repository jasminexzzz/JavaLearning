package com.xzzz.A1_java.base.C_线程.定时任务;

import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 另一定时任务
 */
public class ScheduleTest2 {
    public static void main(String[] args) throws IOException {
        ServiceThreadImpl serviceThread = new ServiceThreadImpl();
        serviceThread.start();
        try {
            // 等待13秒后, 主动唤醒定时任务一次, 然后观察定时任务会在唤醒后继续等待5秒
            Thread.sleep(13000);
            serviceThread.wakeup();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}

abstract class ServiceThread implements Runnable {
    private Thread thread;
    protected final CountDownLatch2 waitPoint = new CountDownLatch2(1);

    public void start() {
        this.thread = new Thread(this, "线程名称");
        this.thread.setDaemon(true);
        this.thread.start();
    }

    /**
     * 主动唤醒
     */
    public void wakeup() {
        System.out.println("定时任务被唤醒, 会立即执行, 并且再次等待一个完整的 interval");
        waitPoint.countDown();
    }

    /**
     * 等待执行
     * @param interval 下次执行等待的诗句
     */
    protected void waitForRunning(long interval) {
        waitPoint.reset();
        try {
            waitPoint.await(interval, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        } finally {
            this.onWaitEnd();
        }
    }

    protected void onWaitEnd() {
    }
}

class ServiceThreadImpl extends ServiceThread {

    @Override
    public void run() {
        while (true) {
            // 5秒执行1次
            waitForRunning(5000);
        }
    }

    @Override
    protected void onWaitEnd() {
        // 具体的业务逻辑
        System.out.println(DateUtil.now());
    }
}
