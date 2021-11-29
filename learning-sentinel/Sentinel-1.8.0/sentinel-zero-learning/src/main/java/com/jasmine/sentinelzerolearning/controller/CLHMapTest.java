package com.jasmine.sentinelzerolearning.controller;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.util.Arrays;

/**
 * ConcurrentLinkedHashMap Test
 *
 * @author wangyf
 * @since 2.0.0
 */
public class CLHMapTest {

    public static void main(String[] args) {
        ConcurrentLinkedHashMap<String, Integer> map;
        map = new ConcurrentLinkedHashMap.Builder<String, Integer>()
                .concurrencyLevel(16)
                .maximumWeightedCapacity(2) // map的最大容量:2,超过2个时,将最后一次使用的淘汰掉
                .weigher(Weighers.singleton())
                .build();


        map.put("a", 1);
        map.put("b", 1);

        System.out.println(Arrays.toString(map.keySet().toArray()));

        map.put("a", 2);
        map.put("c", 1);

        System.out.println(Arrays.toString(map.keySet().toArray()));

    }
}
