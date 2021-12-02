package com.jasmine.java.high.函数式编程;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义过滤器
 *
 * @author jasmineXz
 */
public class TestCustomFilter {

    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);
        ints.add(6);
        filter(ints,integer -> integer > 3).forEach(System.out::println);
    }


    @FunctionalInterface
    interface Func<T> {
        boolean filter (T t);
    }

    public static <T> List<T> filter(List<? extends T> list, Func<? super T> func) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (func.filter(t)) {
                result.add(t);
            }
        }
        return result;
    }
}
