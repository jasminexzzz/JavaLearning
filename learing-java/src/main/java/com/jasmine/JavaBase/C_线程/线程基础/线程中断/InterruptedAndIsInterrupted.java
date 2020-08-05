package com.jasmine.JavaBase.C_线程.线程基础.线程中断;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jasmineXz
 */
public class InterruptedAndIsInterrupted {
    private static final Logger log = LoggerFactory.getLogger(InterruptedAndIsInterrupted.class);



    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new RunDemo());
        t1.start();
        Thread.sleep(4000);
        t1.interrupt();
//        Thread.sleep(1000);

//        System.out.println(Thread.interrupted());
//        System.out.println(Thread.interrupted());
//        System.out.println(Thread.interrupted());

//        System.out.println("--------------");
//        System.out.println(t1.isInterrupted());
    }
}


class RunDemo implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RunDemo.class);

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
//            if (Thread.currentThread().isInterrupted()) {
//                break;
//            }
            try {
                System.out.println(String.format("%s:%s",Thread.currentThread().getName(),Thread.currentThread().isInterrupted()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 抛出中断异常
                log.error(Thread.currentThread().getName() + "被中断 :" + Thread.currentThread().isInterrupted());
//                e.printStackTrace();
//                break;
            }
        }
    }
}
