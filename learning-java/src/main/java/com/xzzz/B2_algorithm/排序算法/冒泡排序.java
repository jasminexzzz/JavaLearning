package com.xzzz.B2_algorithm.排序算法;

import java.util.Arrays;

public class 冒泡排序 {
    public static void main(String[] args) {
        int a[] = {9,8,7,6,5,4,3,2,1};

        int len = a.length;
        for(int i = 0 ; i < len ; i++){

            for(int j = 0 ; j < len - i - 1 ; j++){//注意第二重循环的条件
                if(a[j] > a[j+1]){
                    int temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }

        }

        System.out.println(Arrays.toString(a));
    }
}
