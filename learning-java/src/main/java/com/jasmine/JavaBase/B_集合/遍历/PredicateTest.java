package com.jasmine.JavaBase.B_集合.遍历;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class PredicateTest {
    public static void main(String[] args) {
        Collection books = new ArrayList();
        books.add("AAAA");
        books.add("BBBB");
        books.add("CCCC");
        System.out.println(calAll(books,a -> ((String)a).contains("A")));


    }

    /**
     * @param books
     * @param p
     * @return
     */
    public static int calAll(Collection books, Predicate p){
        // Predicate函数式接口的主要作用就是提供一个test方法，接受一个参数返回一个布尔类型
        int total= 0;
        for (Object obj : books){
            if(p.test(obj)){
                total ++;
            }
        }
        return total;
    }
}
