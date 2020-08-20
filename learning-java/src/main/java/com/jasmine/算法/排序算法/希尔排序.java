package com.jasmine.算法.排序算法;

import java.util.Arrays;

/**
 * 直接插入排序的升级
 *
 */
public class 希尔排序 {

    public static void main(String[] args) {
        int a[] = {9,8,7,6,5,4,3,2,1};
        //子序列个数,未分组时一个元素代表了一个子序列
        int subSeqNum = a.length;

        //一半向下取整,直到子序列为1,也就是整个数组,如果子序列为0则结束循环,代表没有数据需要处理
        while(subSeqNum != 0){
            /*
            处理子序列个数

            第一次/2其实就是2个一组
            下标 0 1 2 3 4 5 6 7 8
            元素 9,8,7,6,5,4,3,2,1

            分组后
            下标 0 4 8  1 5  2 6  3 7
            元素 9 5 1, 8 4, 7 3, 6 2

            第二次/2就是4个一组
            下标 0 1 2 3 4 5 6 7 8
            元素 1 4 3 2 5 8 7 6 9

            分组后
            下标 0 2 4 6 8,1 3 5 7
            元素 1 3 5 7 9,4 2 8 6
            */
            subSeqNum = subSeqNum / 2;

            //从前往后遍历,循环次数为小组个数
            for(int i = 0 ; i < subSeqNum ; i++){

                //按组循环
                for(int j = i + subSeqNum ; j < a.length ; j += subSeqNum){//元素从第二个开始
                    //第一次0
                    int temp = a[j];//分组序列中当前元素
                    int k = j - subSeqNum;//分组序列中当前元素的上一个
                    //第一次5,

                    /*
                    从前往后遍历
                    其实就是按组进行直接插入排序
                     */
                    while(k >= 0 && a[k] > temp){
                        a[k + subSeqNum] = a[k];
                        k -= subSeqNum;//向后移动subSeqNum位
                    }

                    a[k + subSeqNum] = temp;
                }

            }

        }

        System.out.println(Arrays.toString(a));
    }

}
