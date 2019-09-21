package com.jasmine.JavaBase.B_集合.collection.list.arraylist;

import java.util.*;

public class ArrayListTest {
    public static void main(String[] args) {


        List l = new ArrayList();
        l.add(111);
        l.add(222);

        List l1 = new ArrayList(l);

        System.out.println(l1.toString());


    }

}
