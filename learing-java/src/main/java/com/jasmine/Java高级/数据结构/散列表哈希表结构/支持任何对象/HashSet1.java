package com.jasmine.Java高级.数据结构.散列表哈希表结构.支持任何对象;

import com.jasmine.Other.MyUtils.BasicMath;

/**
 * 可以存放任何对象
 */
@SuppressWarnings("all")
public class HashSet1
{

    private Bucket[] buckets;
    private Object[] values;

    private static final int DELETED = -1;

    private int getHashCode(Object key) {
        return key.hashCode();
    }

    public HashSet1(int capacity) {
        int size = BasicMath.getPrime(capacity);
        buckets = new Bucket[size];
    }


    public Object[] getBuckets() {
        return buckets;
    }



    int H1(int value) {
        return value % buckets.length;
    }

    int H2(int value) {
        return 1 + (value % (buckets.length - 1));
    }

    int DH(int value, int i) {
        return (H1(value) + i * H2(value)) % buckets.length;
    }

    /*
    1.添加时构造一个bucket,里面存放<保存元素>以及<保存元素的hashcode>
    2.开始用双重散列算法查找该hashcode所该在的位置
        算法:((hash值%容器长度) + (查询次数 * (1+(hash值%(容器长度-1))) )) % 容器长度
    2.如果这个
        位置为空
        这个位置的内容为空
        这个位置被删过
      则插入,否则查找下一个数组位置.
     */
    public void add(Object item) throws Exception {
        int i = 0; // 已经探查过的槽的数量
        Bucket bucket = new Bucket (item, getHashCode(item));
        do {
            int j = DH(bucket.k, i); // 想要探查的地址
            if (buckets[j] == null || buckets[j].Key == null || buckets[j].k == DELETED){
                buckets[j] = bucket;
                return;
            } else {
                i += 1;
            }
        } while (i <= buckets.length);
        throw new Exception("集合溢出");
    }


    /*
    1.获取查找元素的hash值
    2.查询该值初使位置
    3.如果初始位置为空,或找遍了所有位置直到找到一个Null,证明本元素没插入过,其他元素也没有占用他本应该占用的位置
    4.如果这个位置找到了一个值,且hash值相同,对比equals也相同,则包含该元素
     */
    public boolean Contains(Object item)
    {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        int hashCode = getHashCode(item);
        do {
            j = DH(hashCode, i);
            if (buckets[j].Key == null)
                return false;


            if (buckets[j].k == hashCode && buckets[j].Key.equals(item))
                return true;
            else
                i += 1;
        } while (i <= buckets.length);
        return false;
    }

    /*
    1.获取查找元素的hash值
    2.查询该值初使位置
    3.如果初始位置为空,或找遍了所有位置直到找到一个Null,证明本元素没插入过,其他元素也没有占用他本应该占用的位置
    4.如果这个位置找到了一个值,且hash值相同,对比equals也相同,则将他重置为-1
     */
    public void Remove(Object item) {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        int hashCode = getHashCode(item);
        do {
            j = DH(hashCode, i);
            if (buckets[j].Key == null)
                return;

            if (buckets[j].k == hashCode && buckets[j].Key.equals(item)) {
                buckets[j].k = DELETED;
                return;
            } else {
                i += 1;
            }
        } while (i <= buckets.length);
    }

    public static void main(String[] args) {
        HashSet1 hs = new HashSet1(10);
        try {
            hs.add("去去去");


        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0 ; i < hs.getBuckets().length ; i ++){
            if(null == hs.getBuckets()[i]){
                System.out.println("null");
            }else{
                System.out.println(((Bucket)hs.getBuckets()[i]).Key.toString());
            }
        }

    }

    class Bucket{
        public Object Key;//元素内容
        public int k;   // 元素的hash值


        public Bucket(Object key, int k){
            this.Key = key;
            this.k = k;
        }
    }
}

