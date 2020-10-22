package com.jasmine.JavaBase.C_线程.线程基础.线程池.TestCompletableFuture;


import java.util.concurrent.*;

/**
 * @author : jasmineXz
 */
public class TestCF {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 提供方
        CompletableFuture<Integer> supplier1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });
        CompletableFuture<Integer> supplier2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        });

//        CompletableFuture future1 = CompletableFuture.allOf(supplier1,supplier2);
//        // 等待全部结束
//        future1.get();
//        System.out.println("结束,取决于最长的一次请求");


        CompletableFuture future2 = CompletableFuture.anyOf(supplier1,supplier2);
        // 等待任意结束
        System.out.println(future2.get());
        System.out.println("结束,取决于最短的一次请求");
    }
}
