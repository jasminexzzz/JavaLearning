package com.jasmine.A1_java.base.C_线程.线程基础.线程创建;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * runnable 只能抛出运行时异常, 且无法捕获
 *
 * @author jasmineXz
 */
public class TestRunnable implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(TestRunnable.class);

    @Override
    public void run() {
        log.info("子线程执行完毕");
        throw new IllegalArgumentException("123");
    }

    public static void main(String[] args) {
        try {
            new Thread(new TestRunnable()).start();
        } catch (Exception e) {
            log.error("捕获");
            e.printStackTrace();
        }
        log.info("主线程执行完毕");
    }
}
