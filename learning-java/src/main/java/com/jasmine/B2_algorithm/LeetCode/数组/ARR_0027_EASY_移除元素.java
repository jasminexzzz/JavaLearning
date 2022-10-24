package com.jasmine.B2_algorithm.LeetCode.数组;


/**
 *
 * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
 * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/remove-element
 */
public class ARR_0027_EASY_移除元素 {


    /**
     *
     * val = 3
     *
     * length = 0 | [3],2,2,3 | nums[0] != val : false | 不修改
     * length = 0 | 3,[2],2,3 | nums[1] != val : true  |   修改 : 2,[2],2,3  | 1
     * length = 1 | 2,2,[2],3 | nums[2] != val : true  |   修改 : 2,2,[2],3  | 2
     * length = 2 | 2,2,2,[3] | nums[3] != val : false | 不修改              | 2
     */
    public static int removeElement(int[] nums, int val) {
        int length = 0;
        for (int i = 0; i < nums.length ; i++) {
            if (nums[i] != val) {
                nums[length] = nums[i];
                length ++;
            }
        }
        return length;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {3,2,2,3};
        int val = 3;
        int num = removeElement(nums,val);
        System.out.println(num);
        System.out.println("==========");
        for (int i = 0 ; i < num ; i++) {
            System.out.println(nums[i]);
        }
    }
}

