package com.xzzz.A1_java.high.泛型通配符;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jasmineXz
 */
public class Test<T> {
    public static void main(String[] args) {
        List<Integer> strList = new ArrayList<>();
        strList.add(1);
        strList.add(2);
        // jdk8 使用lambda表达式
        List<Integer> intList = map(strList, s -> s * 10);

        intList.forEach(System.out::println);
    }

    /**
     * 定义一个接口,它接收一个类型,返回另一个类型.
     *
     * @param <T> 一个类型的方法参数
     * @param <R> 一个类型的返回
     */
    interface funcTR<T, R> {
        // 接收一个类型,返回另一个类型.
        R apply(T t);
    }

    /**
     * 定义mapping函数
     *
     * @param src<? extends R>  提供数据,这里是作为apply的返回值,因此这里使用(get)上边界通配符
     * @param mapper<? super T> 接收数据,这里作为 apply的传入参数,函数的具体实现
     * @return 返回值不要使用 通配符来定义
     */
    public static <R, T> List<R> map(List<? extends T> src, funcTR<? super T, ? extends R> mapper) {
        if (src == null)
            throw new IllegalArgumentException("List must not be not null");
        if (mapper == null)
            throw new IllegalArgumentException("map func must be not null");
        // coll 既需要接收数据(add),又需要提供数据(return),所以不使用通配符
        List<R> coll = new ArrayList<>();
        for (T t : src) {
            coll.add(mapper.apply(t));
        }
        return coll;
    }
}
