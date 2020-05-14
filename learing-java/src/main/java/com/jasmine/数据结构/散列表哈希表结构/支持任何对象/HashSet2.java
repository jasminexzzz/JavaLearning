package com.jasmine.数据结构.散列表哈希表结构.支持任何对象;


import com.jasmine.Other.MyUtils.BasicMath;

@SuppressWarnings("all")
public class HashSet2
{

    private Bucket[] buckets;
    private int GetHashCode(Object key) {
        return key.hashCode() & 0x7FFFFFFF;
    }

    // 将 bucket 标记为已删除
    private void MarkDeleted(Bucket bucket) {
        bucket.Key = buckets;
        bucket.k = 0;
    }

    // 判断 bucket 是否是空槽或者是已被删除的槽
    private boolean IsEmputyOrDeleted(Bucket bucket) {
        return bucket == null || bucket.Key == null || bucket.Key.equals(buckets);
    }

    public HashSet2(int capacity) {
        int size = BasicMath.getPrime(capacity);
        buckets = new Bucket[size];
    }

    public Bucket[] getBuckets() {
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




    public void add(Object item) throws Exception {
        int i = 0; // 已经探查过的槽的数量
        Bucket bucket = new Bucket (item,GetHashCode(item));
        do {
            int j = DH(bucket.k, i); // 想要探查的地址
            if (IsEmputyOrDeleted(buckets[j])) {
                buckets[j] = bucket;
                return;
            } else {
                i += 1;
            }
        } while (i <= buckets.length);
        throw new Exception("集合溢出");
    }

    public void remove(Object item) {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        int hashCode = GetHashCode(item);
        do {
            j = DH(hashCode, i);
            if (buckets[j].Key == null)
                return;

            if (buckets[j].k == hashCode && buckets[j].Key.equals(item)) {
                MarkDeleted(buckets[j]);
                return;
            } else {
                i += 1;
            }
        } while (i <= buckets.length);
    }

    public boolean Contains(Object item) {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        int hashCode = GetHashCode(item);
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


    public static void main(String[] args) {
        HashSet2 hs = new HashSet2(10);
        try {
            hs.add("1");

            hs.remove("1");

//            System.out.println(hs.getBuckets());

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

