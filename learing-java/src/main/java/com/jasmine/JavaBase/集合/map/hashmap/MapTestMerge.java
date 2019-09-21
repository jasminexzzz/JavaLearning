package com.jasmine.JavaBase.集合.map.hashmap;

import java.util.HashMap;
import java.util.Map;

public class MapTestMerge {
    public static void main(String[] args) {
        Map map = new HashMap();
        // 成对放入多个key-value对
        map.put(1 , "aaa");
        map.put(2 , "bbb");
        map.put(3 , "ccc");
        // 尝试替换key为"疯狂XML讲义"的value，由于原Map中没有对应的key，
        // 因此对Map没有改变，不会添加新的key-value对
        map.replace(4 , "ddd");
        System.out.println(map);

        // 使用原value与参数计算出来的结果覆盖原有的value
        map.merge(1 , "AAA" ,(oldVal , param) -> (String)oldVal + (String)param);
        System.out.println(map);

        // 当key为"2"对应的value为null（或不存在时），使用计算的结果作为新value
        map.computeIfAbsent("4444" , (key)->((String)key).length());
        // map中添加了 Java=4 这组key-value对
        System.out.println(map);

        // 当key为"4444"对应的value存在时，使用计算的结果作为新values
        map.computeIfPresent("4444", (key , value) -> (Integer)value * 100);
        System.out.println(map); // map中 Java=4 变成 Java=16
    }
}
