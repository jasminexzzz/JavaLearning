package com.xzzz.A1_java.high.函数式编程;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 消费者接口, 用于一些没有返回值的方法
 * @author : jasmineXz
 */
public class TestConsumer {
    private final static List<Integer> ints = Stream.of(1,2,3,4,5,6).collect(Collectors.toList());
    // 实现 accept 方法
    private final static Consumer<Integer> consumer = System.out::println;

    public static void main(String[] args) {
        List<Integer> result = new ArrayList<>();

        // 执行完 accept 后用相同的参数执行 andThen
        ints.forEach(consumer.andThen(i -> {
            if (i > 3) {
                result.add(i);
            }
        }));

        System.out.println("====================");

        result.forEach(consumer);
    }
}
