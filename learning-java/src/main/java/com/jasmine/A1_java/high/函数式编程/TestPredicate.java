package com.jasmine.A1_java.high.函数式编程;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 谓词接口, 其实就是一个判断
 * @author : jasmineXz
 */
public class TestPredicate {
    private final static List<Integer> ints = Stream.of(1,2,3,4,5,6,7).collect(Collectors.toList());

    // 判断是否大于5
    private final static Predicate<Integer> hasGreater5 = i -> i > 5;
    // 判断是否小于10
    private final static Predicate<Integer> hasless10 = i -> i < 10;
    // 判断是否小于3
    private final static Predicate<Integer> hasless3 = i -> i < 3;



    public static void main(String[] args) {
        // 流中判断
        List<Integer> result = ints.stream().filter(hasGreater5).collect(Collectors.toList());
        result.forEach(System.out::println);

        System.out.println("========================================");

        System.out.println("11 大于 5 : " + hasGreater5.test(11));          // true
        System.out.println("11 小于 5 : " + hasGreater5.negate().test(11)); // false

        System.out.println("========================================");

        System.out.println("11 大于5并且小于10 : " + hasGreater5.and(hasless10).test(11)); // false

        System.out.println("========================================");

        System.out.println("3 大于5或者小于3  : " + hasGreater5.or(hasless3).test(2)); // true

    }

}
