package com.jasmine.JavaBase.C_线程.线程基础.线程创建;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jasmineXz
 */
public class TestThread extends Thread {
    private static final Logger log = LoggerFactory.getLogger(TestThread.class);

    @Override
    public void run() {
        log.info("子线程执行完毕");
        throw new RuntimeException("123");
    }

    public static void main(String[] args) {
        TestThread t = new TestThread();
        t.start();
        log.info("主线程执行完毕");
    }
}
