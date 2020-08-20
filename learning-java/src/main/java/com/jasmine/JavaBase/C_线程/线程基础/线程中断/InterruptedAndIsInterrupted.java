package com.jasmine.JavaBase.C_线程.线程基础.线程中断;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jasmineXz
 */
public class InterruptedAndIsInterrupted {

    private static final Logger log = LoggerFactory.getLogger(InterruptedAndIsInterrupted.class);

    public static void main(String[] args) throws InterruptedException {
        RunDemo t1 = new RunDemo("t1");
        Thread t2 = new Thread(t1,"t2");
        t2.start();
        Thread.sleep(20);
        t2.interrupt();
        // System.out.println(t2.isInterrupted()); // ture
    }
}


class RunDemo extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RunDemo.class);

    public RunDemo(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 500; i++) {
            // 中断后输出true
            System.out.println(String.format("%s:%s,%s",Thread.currentThread().getName(),i,this.isInterrupted()));
        }
    }
}
