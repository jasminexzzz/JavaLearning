package com.jasmine.Java高级.算法.排序算法;

import java.util.Arrays;

/**
 * @author : jasmineXz
 */
public class 快速排序1 {


    public static void quickSort(int [] arr,int left,int right) {
        //基准
        int pivot = 0;

        if(left < right) {
            pivot = partition(arr,left,right);
            System.out.println("基准为:"+pivot);
            quickSort(arr,left,pivot-1);
            quickSort(arr,pivot+1,right);
        }
    }


    /**
     * 一次结束,其实就是找到一个数组的中位数,让他左边的都比自己小,右边的都比自己打
     * @param arr
     * @param left
     * @param right
     * @return
     */
    private static int partition(int[] arr,int left,int right) {
        int key = arr[left];//基准元素
        //如果left 和 right 不相撞
        while(left < right) {
            //先从右边开始
            //左边小于右边,并且右边的数大于等于基准,则右边递减
            while(left < right && arr[right] >= key) {
                right--;
            }
            //右边和基准调换
            arr[left] = arr[right];
            //再从左边开始
            while(left < right && arr[left] <= key) {
                left++;
            }
            arr[right]=arr[left];
        }
        arr[left]=key;
        return left;
    }



    public static void main(String[] args) {
        int arr[]= {2,2,1,3,4,5,6};
        System.out.println("排序前："+ Arrays.toString(arr));
        quickSort(arr,0,arr.length-1);
        System.out.println("排序后：" + Arrays.toString(arr));

    }
}
