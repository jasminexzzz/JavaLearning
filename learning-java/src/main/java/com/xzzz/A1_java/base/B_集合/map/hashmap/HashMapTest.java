package com.xzzz.A1_java.base.B_集合.map.hashmap;

import java.util.HashMap;
import java.util.Map;

public class HashMapTest {

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static void main(String[] args) {
        Map<Object,String> hashMap = new HashMap ();

//        for(int i = 1 ; i < 10 ; i++)
//            hashMap.put(i, i*100);
        hashMap.put(49, "二");
        hashMap.put("1", "19986");



        System.out.println("通过for循环来遍历 ");
        /*
            这个其实jdk中有封装,在1.8时map提供了foreach方法,就是如此写法
        for(Map.Entry<Integer,String>  entry : hashMap.entrySet()){
            System.out.println(entry.getKey() +":" +hash(entry.getKey()));
        }
        */

//        hashMap.forEach(
//            (k,v) -> System.out.println(k + "=" + v)
//        );

//        System.out.println("\n通过Map.entrySet使用iterator遍历key和value: ");
//        Iterator it = hashMap.entrySet().iterator();
//        while(it.hasNext()) {
//            System.out.println(it.next());
//        }


        for(Map.Entry<Object,String>  entry : hashMap.entrySet()){
            System.out.println(entry.getKey() +":" +hash(entry.getKey()));
        }
    }
}
