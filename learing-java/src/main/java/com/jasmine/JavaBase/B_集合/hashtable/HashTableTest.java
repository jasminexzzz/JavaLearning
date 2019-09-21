package com.jasmine.JavaBase.B_集合.hashtable;

import java.util.Hashtable;
import java.util.Iterator;

public class HashTableTest {
    public static void main(String[] args) {
        Hashtable ht = new Hashtable();
        ht.put(1, "1");
        ht.put(2, "1");

        Iterator it = ht.entrySet().iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }
    }
}
