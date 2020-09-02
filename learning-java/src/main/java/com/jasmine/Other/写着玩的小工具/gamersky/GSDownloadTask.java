package com.jasmine.Other.写着玩的小工具.gamersky;

/**
 * 下载线程
 */
public class GSDownloadTask extends GSDownloadQueue implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " 线程启动");
        // 队列消费无限循环
        while (true) {
            take();
        }
    }
}