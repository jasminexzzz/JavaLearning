package com.jasmine.java.base.B_集合.collection;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionTest {
    public static void main(String[] args) {
        Collection c = new ArrayList();
        c.add(3);
        Iterator it = c.iterator();
        System.out.println(c);
    }
}
