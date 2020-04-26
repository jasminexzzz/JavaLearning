package com.jasmine.Java高级.算法.LeetCode.数组;

import java.util.Arrays;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/two-sum
 */
public class ARR_0001_EASY_两数之和 {

    public static void main(String[] args) {
        int[] nums = {3, 2, 4};
        int target = 6;
        System.out.println(Arrays.toString(twoSum(nums,target)));
    }


    public static int[] twoSum(int[] nums, int target) {
        for(int i = 0 ; i < nums.length; i ++){
            for (int j = i + 1; j < nums.length; j ++){
                if ((nums[i] + nums[j]) == target){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{0,0};
    }
}
