package com.jasmine.java.base.B_集合.map.linkedhashmap;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTest {
    public static void main(String[] args) {
        Map<Integer,String> m = new LinkedHashMap(10,0.75f,true);
        m.put(1,"一" );
        m.put(2,"二" );
        m.put(3,"三" );


        m.get(2);

        for(Map.Entry<Integer,String> entry : m.entrySet()){
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

    }
}
