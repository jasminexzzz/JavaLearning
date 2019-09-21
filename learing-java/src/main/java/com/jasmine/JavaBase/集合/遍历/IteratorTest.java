package com.jasmine.JavaBase.集合.遍历;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class IteratorTest {
    public static void main(String[] args) {
        Collection<String> books = new ArrayList<>();
        books.add("AAAA");
        books.add("BBBB");
        books.add("1111");

        Iterator<String> it = books.iterator();
        while (it.hasNext()){

            String book = it.next();

            System.out.println(book);

            if(book.equals("BBBB")){
                it.remove();
            }

        }
        System.out.println(books);
    }
}
