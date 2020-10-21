package com.jasmine.JavaBase.C_线程.线程基础.线程池;

import java.util.concurrent.*;

/**
 * 有返回值
 * @author : jasmineXz
 */
public class TestThreadPoolSubmit {

    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            3,
            3,
            200,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3)
    );

    public static void main(String[] args) {
        CallDemo c1 = new CallDemo("c1");
        CallDemo c2 = new CallDemo("c1");
        CallDemo c3 = new CallDemo("c1");

        Future fature1 = executor.submit(c1);
        Future fature2 = executor.submit(c1);
        Future fature3 = executor.submit(c1);

        try {
            fature1.get();
            fature2.get();
            fature3.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}


class CallDemo implements Callable<String> {

    private String param;

    public CallDemo(String param) {
        this.param = param;
    }

    @Override
    public String call() {
        return "return " + param;
    }
}
