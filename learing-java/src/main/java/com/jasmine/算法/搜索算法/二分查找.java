package com.jasmine.算法.搜索算法;

/**
 * 一个对有序数组或集合的查找方法
 * 也叫折半查找
 * @author : jasmineXz
 */
public class 二分查找 {



    public static void main(String[] args) {
        int[] a = {1,3,5,6,8,10,12,15,18,21};

        System.out.println(binarySearch(a,15));
    }

    /**
     * 查找元素所在数组的位置.
     * @param t
     * @param key
     */
    public static int binarySearch(int[] t,int key){
        int low = 0;
        int high = t.length -1;
        while(low <= high){
            /*
            防止过大,不使用/2的方法
            可尝试测试
            int a = 2147483647;
            int b = 3;
            System.out.println(a + b / 2);
            System.out.println(a + b >>> 1);
             */
            int mid = (low + high) >>> 1;
            if(t[mid] < key) {
                low = mid + 1;
            } else if(t[mid] > key){
                high = mid - 1;
            } else{
                return mid;
            }
        }
        return -1;
    }
}
