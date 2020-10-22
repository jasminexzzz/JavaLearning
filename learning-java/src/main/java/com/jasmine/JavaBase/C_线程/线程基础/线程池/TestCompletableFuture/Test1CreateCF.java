package com.jasmine.JavaBase.C_线程.线程基础.线程池.TestCompletableFuture;

import com.jasmine.Other.MyUtils.OkHttpUtil;
import com.jasmine.框架学习.Log4J2.test;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 *
 * 1. CompletableFuture 创建
 *
 * @author : jasmineXz
 */
public class Test1CreateCF {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /* =====================================================================================
         * 直接调用计算方法,没有返回值
         * ===================================================================================== */
        System.out.println("==================== 直接调用 ====================");
        CompletableFuture f = new CompletableFuture();
        f.complete(100 + 1);
        System.out.println(f.get());


        /* =====================================================================================
         * 直接创建一个计算完成的cf
         * ===================================================================================== */
        System.out.println("==================== 直接创建 ====================");
        CompletableFuture f2 = CompletableFuture.completedFuture(100 + 1);
        System.out.println(f2.get());


        /* =====================================================================================
         * 运行runnable,没有返回值, 方法不指定线程池则使用默认 ForkJoinPool.commonPool
         * ===================================================================================== */
        System.out.println("==================== 直接创建 ====================");
        CompletableFuture f3 = CompletableFuture.runAsync(new RunDemo());
        System.out.println(f3.get() + "\n"); // 无返回值的创建


        /* =====================================================================================
         * 传入一个供应接口实现, 返回该接口的结果, 方法不指定线程池则使用默认 ForkJoinPool.commonPool
         * 供应接口可以返回各种东西,例如从其他地方查询数据等
         * ===================================================================================== */
        Supplier<Game> supplier = (() -> new Game("GTA5",100));
        CompletableFuture<Game> f4 = CompletableFuture.supplyAsync(supplier);
        System.out.println(f4.get().getName() + "\n");


        Supplier<String> supplierIp = new ThisIp();
        CompletableFuture<String> f5 = CompletableFuture.supplyAsync(supplierIp);
        // ...此时处理本服务逻辑,也可以查询其他接口
        System.out.println(f5.get());
        // ...获取到返回后继续处理后序逻辑
        /* =====================================================================================
         * 抛出异常
         * ===================================================================================== */
        System.out.println("==================== 抛出异常 ====================");

        CompletableFuture f1 = new CompletableFuture();
        f1.completeExceptionally(new Exception("错误信息"));
        try {
            f1.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("抛出了异常:" + e.getMessage());
        }
    }

    static class RunDemo implements Runnable {
        @Override
        public void run() {
            System.out.println(String.format("线程 [%s] 运行 runnable",Thread.currentThread().getName()));
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
        }
    }

    static class ThisIp implements Supplier<String> {
        @Override
        public String get() {
            String url = "http://ip.ws.126.net/ipquery";
            try {
                Response resp = OkHttpUtil.get(url);
                return Objects.requireNonNull(resp.body()).string();
            } catch (Exception e) {
                return "";
            }
        }
    }

    static class Game {
        String name;
        int price;

        public Game() {
        }

        public Game(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

}

