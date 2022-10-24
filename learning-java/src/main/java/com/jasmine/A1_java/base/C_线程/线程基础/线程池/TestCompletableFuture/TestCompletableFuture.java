package com.jasmine.A1_java.base.C_线程.线程基础.线程池.TestCompletableFuture;

import java.util.concurrent.*;

/**
 * @author : jasmineXz
 */
public class TestCompletableFuture {

    private final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            3,
            3,
            200,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3)
    );


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CFutureRunDemo c1 = new CFutureRunDemo("c1");
        CompletableFuture cf1 = CompletableFuture.runAsync(c1,executor);
        System.out.println(cf1.get());

        System.out.println("=======================");
        CompletableFuture<String> future =
            CompletableFuture
            // 异步执行
            .supplyAsync(()-> {
                System.out.println(Thread.currentThread().getName());
                return "hello";
            },executor)
            // 上一步结束后执行
            .thenApplyAsync((e) -> {
                System.out.println(Thread.currentThread().getName());
                return e + " world";
            },executor)
            .thenApplyAsync((e) -> {
                System.out.println(Thread.currentThread().getName());
                return e + " java!";
            },executor)
            .whenCompleteAsync((v,e)-> {
                System.out.println(Thread.currentThread().getName());
                System.out.println(v + e);
            },executor);
//        System.out.println(future.get());
    }
}

class CFutureCallDemo implements Callable<String> {

    private String param;

    CFutureCallDemo(String param) {
        this.param = param;
    }

    @Override
    public String call() {
        return "return " + param;
    }
}

class CFutureRunDemo implements Runnable {

    private String param;

    CFutureRunDemo(String param) {
        this.param = param;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " : " + param);
    }
}
