package com.xzzz.A1_java.high.函数式编程;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 供应商, 返回一个值
 * @author : jasmineXz
 */
public class TestSupplier {
    private final static List<Integer> ints = Stream.of(1,2,3,4,5,6).collect(Collectors.toList());
    // 返回一个随机值
    private final static Supplier<Integer> randomSupplier = () -> new Random(1).nextInt();

    public static void main(String[] args) {
        System.out.println(randomSupplier.get());
        System.out.println("===================");
        // 获取第一个值
        System.out.println(ints.stream().findFirst().get());
        System.out.println("===================");
        ints.clear();
        // 如果不存在,则调用supplier.get()获取值
        System.out.println(ints.stream().findFirst().orElseGet(randomSupplier));
    }
}
