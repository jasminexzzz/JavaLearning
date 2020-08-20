package com.jasmine.JavaBase.B_集合.遍历;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class IteratorForEach {
    public static void main(String[] args) {
        Collection<String> books = new HashSet<>();
        books.add("AAAA");
        books.add("BBBB");
        books.add("1111");

        Iterator it = books.iterator();
        it.forEachRemaining(obj -> System.out.println(obj));

    }
}
