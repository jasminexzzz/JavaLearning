package com.jasmine.Java高级.函数式编程;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : jasmineXz
 */
public class TestFunction {
    private static final Logger log = LoggerFactory.getLogger(TestFunction.class);
    private final static List<Integer> ints = Stream.of(1,2,3,4,5,6,7).collect(Collectors.toList());

    // 返回字符串长度
    private final static Function<String,Integer> strLength = String::length;

    private final static Function<String,String> strFormart = s -> String.format("%8s",s);

    public static void main(String[] args) {

        System.out.println("输入字符串,返回长度:" + strLength.apply("123"));

        System.out.println("字符串前补齐空格:" + strFormart.apply("123"));

    }
}
