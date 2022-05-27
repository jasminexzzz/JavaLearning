package com.jasmine.sbreactor.reactor;

import reactor.core.publisher.Flux;

/**
 * @author wangyf
 */
public class FluxTest {


    public static void main(String[] args) {
        // 从1开始创建, 共创建6个元素 [1,2,3,4,5,6]
        Flux<Integer> ints = Flux.range(1, 6)
                .map(i -> {
                    if (i <= 5) {
                        return i;
                    }
                    throw new RuntimeException("只处理小于6的数据");
                });


        ints.subscribe(
            succe -> System.out.println("Succe: " + succe) // 处理正常
        );

        System.out.println("──────────────────────────────────────");

        ints.subscribe(
            succe -> System.out.println("Succe: " + succe), // 处理正常
            error -> System.err.println("Error: " + error)  // 处理异常, 有一个异常即终止流
        );

        System.out.println("──────────────────────────────────────");

        ints.subscribe(
            // 处理正常, 在 main 线程中
            succe -> {
                System.out.println(String.format("succ %s %s", succe, Thread.currentThread().getName()));
            },
            // 处理异常, 在 main 线程中
            error -> {
                System.out.println(String.format("succ %s %s", error, Thread.currentThread().getName()));
            },
            // 全部成功才执行, 在 main 线程中
            () -> {
                System.out.println(String.format("已全部处理完成 %s", Thread.currentThread().getName()));
            }
        );

        System.out.println("──────────────────────────────────────");

        ints.subscribe(
                // 处理正常, 在 main 线程中
                succe -> {
                    System.out.println(String.format("succ %s %s", succe, Thread.currentThread().getName()));
                },
                // 处理异常, 在 main 线程中
                error -> {
                    System.out.println(String.format("succ %s %s", error, Thread.currentThread().getName()));
                },
                // 全部成功才执行, 在 main 线程中
                () -> {
                    System.out.println(String.format("已全部处理完成 %s", Thread.currentThread().getName()));
                },
                // 只订阅两个, 但其他元素仍然会处理, 但成功的不会进入到 successConsumer 中
                subscription -> {
                    System.out.println("只订阅2个");
                    subscription.request(2);
                }
        );
    }
}
