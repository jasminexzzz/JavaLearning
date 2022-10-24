package com.xzzz.sbreactor.stream;

import com.xzzz.common.core.util.StackTraceUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * java Stream 工具使用案例与说明
 * @author wangyf
 */
public class StreamTest {

    private static final List<User> testList = User.init();

    public static void main(String[] args) {
        map();
        flatMap();
    }

    /**
     * map 可以获取某个字段的集合
     */
    public static void map() {
        line();
        List<Integer> ids = testList.stream().map(User::getId).collect(Collectors.toList());
        ids.forEach(System.out::println);
    }

    /**
     * flatMap 可以将多个集合
     */
    public static void flatMap() {
        line();
        List<String> addrs = testList.stream().flatMap(user -> user.getAddr().stream()).collect(Collectors.toList());
        addrs.forEach(System.out::println);
    }

    private static void line() {
        System.out.println(String.format("─────────────── %s ──────────────", StackTraceUtil.lastMethodName()));
    }
}


