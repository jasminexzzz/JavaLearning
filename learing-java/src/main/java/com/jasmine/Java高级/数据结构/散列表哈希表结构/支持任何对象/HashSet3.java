package com.jasmine.Java高级.数据结构.散列表哈希表结构.支持任何对象;

import com.jasmine.Other.MyUtils.BasicMath;

@SuppressWarnings("all")
public class HashSet3 {

    private Bucket[] buckets;

    public Bucket[] getBuckets() {
        return buckets;
    }

    private int GetHashCode(Object key) {
        return key.hashCode() & 0x7FFFFFFF;
    }

    // 将 bucket 标记为已删除
    private void MarkDeleted(Bucket bucket) {
        bucket.Key = buckets;
        bucket.hash = 0;
    }

    // 判断 bucket 是否是空槽或者是已被删除的槽
    private boolean IsEmputyOrDeleted(Bucket bucket) {
        return bucket == null || bucket.Key == null || bucket.Key.equals(buckets);
    }

    //创造存放bucket的数组
    public HashSet3(int capacity) {
        int size = BasicMath.getPrime(capacity);
        buckets = new Bucket[size];
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

    /**
     * 添加
     * @param item 在item中添加
     * @throws Exception
     */
    public void add(Object item) throws Exception {
        int i = 0; // 已经探查过的槽的数量
        Bucket bucket = new Bucket (item,GetHashCode(item));
        int k = bucket.hash;
        /**
         * 每次循环时
         * 循环到的下标为空,或被删除,则把新构造的bucket放入此处
         * 循环到的下标不为空,则把此处标记为已碰撞
         */
        do {
            int j = DH(k, i); // 想要探查的地址
            //如果这里为空,说明从来没有插入过任何元素,可以直接把bucket放入这里
            if(buckets[j] == null){
                buckets[j] = bucket;
                return;
            //如果是被删除过的,则碰撞标识不变,原来撞过就是撞过,原来没撞就是没撞
            }else if (buckets[j].Key == null || bucket.Key.equals(buckets)) {
                buckets[j].Key = item;
                buckets[j].hash = k; // 仍然保留 _k 的最高位不变
                return;
            //否则被碰撞,则需要把该位的
            } else {
                buckets[j].MarkCollided();
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

            if (buckets[j].hash == hashCode && buckets[j].Key.equals(item)) {
                MarkDeleted(buckets[j]);
                return;
            } else {
                i += 1;
            }
        } while (i <= buckets.length);
    }

    public boolean contains(Object item) {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        int hashCode = GetHashCode(item);
        do {
            j = DH(hashCode, i);
            if (buckets[j].Key == null)
                return false;

            if (buckets[j].hash == hashCode && buckets[j].Key.equals(item))
                return true;
            else
                i += 1;
        } while (i <= buckets.length);
        return false;
    }

    public static void main(String[] args) {
        HashSet3 hs = new HashSet3(10);
//        try {
//            hs.add("1");
//            hs.add("1");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        for(int i = 0 ; i < hs.getBuckets().length ; i ++){
//            if(null == hs.getBuckets()[i]){
//                System.out.println("null");
//            }else{
//                System.out.println(((Bucket)hs.getBuckets()[i]).Key.toString());
//            }
//        }

    }

    /**
     * 一个桶,构建一个该类型的数组,桶里存放自己的数据,数据的hash值,是否发生过碰撞的要素
     */
    class Bucket{

         //元素内容
        public Object Key;

        //元素的hash值,虽然元素的hash值可能为负值,但我们获取时其实会将他 & 0x7FFFFFFF,所以该值永远不会有负值
        public int hash;

        /*
            用来标记hash值的第一位是0还是1
            0:为未发生碰撞
            1:发生过了碰撞
        */
        private int _k;

        //标记该位置是否发生过碰撞 ture:已碰撞;false:未碰撞
        public boolean isCollided;


        public int get_k() {
            return _k;
        }

        public void set_k(int hash) {
            /*
            将 _k 除了最高的碰撞位之外的其它位全部设为0
            &= : _k = _k & 874

            这段代码的意思是
            _k值可能有0和-2147483648两个
            也就是二进制的
            00000000000000000000000000000000
            或
            10000000000000000000000000000000

            如果_k为正数则为0,为负数则为-2147483648
             */
            _k &= 0x80000000;
            // 保持 _k 的最高的碰撞位不变，将 value 的值放到 _k 的后面几位中去
            this.hash |= _k;
        }

        public boolean getIsCollided() {
            return (_k & 0x80000000) != 0;// _k 的最高位如果为1表示发生过碰撞
        }

        // 将槽标记为发生过碰撞
        public void MarkCollided() {
            _k |= 0x80000000; //  将 _k 的最高位设为1
        }

        public Bucket(Object key, int k){
            this.Key = key;
            this.hash = k;
        }

//        public Bucket(Object key, int hash,boolean isCollided){
//            this.Key = key;
//            this.hash = hash;
//            this.isCollided = isCollided;
//        }
    }
}

