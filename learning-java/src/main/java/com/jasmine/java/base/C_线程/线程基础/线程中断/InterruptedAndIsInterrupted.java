package com.jasmine.java.base.C_线程.线程基础.线程中断;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jasmineXz
 */
public class InterruptedAndIsInterrupted {

    private static final Logger log = LoggerFactory.getLogger(InterruptedAndIsInterrupted.class);

    public static void main(String[] args) throws InterruptedException {
        RunDemo t1 = new RunDemo("t1");
        log.warn("===> 启动前状态: {}", t1.isInterrupted()); // false
        t1.start();
        Thread.sleep(20);
        log.warn("===> 打断前状态: {}", t1.isInterrupted()); // false
        t1.interrupt(); // 打断线程
        // Thread.sleep(500);
        log.warn("===> 打断后主线程状态: {}",t1.isInterrupted()); // 查看线程状态
    }
}


class RunDemo extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RunDemo.class);

    RunDemo(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 500000; i++) {
            // 中断后退出
            if (this.isInterrupted()) {
                log.error("进入打断判断: {}",this.isInterrupted()); // true
                break;
            }
            log.info("===> {} t1运行时状态: {}",i,this.isInterrupted()); // false
        }
    }
}
