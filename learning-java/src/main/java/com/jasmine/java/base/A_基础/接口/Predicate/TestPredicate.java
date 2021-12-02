package com.jasmine.java.base.A_基础.接口.Predicate;

import java.util.function.Predicate;

/**
 *
 在Java 8中，Predicate是一个函数式接口，可以被应用于lambda表达式和方法引用。


@FunctionalInterface
public interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}



    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }


    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? Objects::isNull
                : object -> targetRef.equals(object);
    }
}


 * Predicate功能判断输入的对象是否符合某个条件
 */
public class TestPredicate {
    public static void main(String[] args) {
        Predicate<Integer> boolValue = x -> x > 5;
        System.out.println(boolValue.test(1));//false
        System.out.println(boolValue.test(6));//true
    }
}

