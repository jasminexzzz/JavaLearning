package com.jasmine.Java高级.工具类.布隆过滤器;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;

/**
 * @author jasmineXz
 */
public class BloomFilter {
    public static void main(String[] args) {
        BitMapBloomFilter filter = BloomFilterUtil.createBitMap(50000000);
        filter.add("111");
        filter.add("222");
        filter.add("333");

        System.out.println(filter.contains("111"));
    }
}
