package com.jasmine.JavaBase.集合.遍历;

import java.util.Collection;
import java.util.HashSet;

public class CollectionForEach {
    public static void main(String[] args) {
        Collection books = new HashSet();
        books.add("AAAA");
        books.add("BBBB");
        books.add("1111");

        books.forEach(obj -> System.out.println(obj));

    }
}
