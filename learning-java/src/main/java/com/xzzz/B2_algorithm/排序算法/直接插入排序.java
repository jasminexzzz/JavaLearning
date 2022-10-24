package com.xzzz.B2_algorithm.排序算法;

import java.util.Arrays;

/**
 * 从第二个数开始,拿这个数和前面的数字相比较
 * 如果前面的数比自己小,则他俩的位置互换.
 * 否则这个就是位置就是自己

    9 5 6 3 1
    5 : 5比9小 ,变成5 9
    6 : 6比9小 ,变成5 6 9   ,6比5大 ,变成5 6 9
    3 : 3比9小 ,变成5 6 3 9 ,3比6小 ,变成5 3 6 9 ,3比5小,变成3 5 6 9


 */
public class 直接插入排序 {
    public static void main(String[] args) {
        int a[] = {9,5,6,3,1};

        int len = a.length;//单独把数组长度拿出来，提高效率
        int insertNum;//要插入的数

        for(int i = 1 ; i < len ; i++){//因为第一次不用，所以从1开始
            insertNum = a[i];
            int j = i - 1;//序列元素个数
            while(j >= 0 && a[j] > insertNum){//从后往前循环，将大于insertNum的数向后移动
                a[j + 1] = a[j];//元素向后移动
                j--;
            }
            a[j + 1] = insertNum;//找到位置，插入当前元素
        }

        System.out.println(Arrays.toString(a));
    }
}
