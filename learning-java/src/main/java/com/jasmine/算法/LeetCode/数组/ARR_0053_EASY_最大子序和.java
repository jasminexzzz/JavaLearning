package com.jasmine.算法.LeetCode.数组;

/**
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-subarray/
 */
public class ARR_0053_EASY_最大子序和 {


    public static int maxSubArray(int[] nums) {
        int ans = nums[0];
        int sum = 0;
        for(int num : nums) {
            if(sum > 0) {
                sum += num;
            } else {
                sum = num;
            }
            ans = Math.max(ans, sum);
        }
        return ans;
    }


    public static void main(String[] args) {
        int[] nums = new int[] {-2,1,-3,4,-1,2,1,-5,4};
        int num = maxSubArray(nums);
        System.out.println(num);
    }
}
