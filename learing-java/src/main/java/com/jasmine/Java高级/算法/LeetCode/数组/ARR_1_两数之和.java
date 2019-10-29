package com.jasmine.Java高级.算法.LeetCode.数组;

import java.util.Arrays;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/two-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class ARR_1_两数之和 {

    public static void main(String[] args) {
        int[] nums = {3, 3, 2, 7};
        int target = 9;
        System.out.println(Arrays.toString(twoSum(nums,target)));
    }


    public static int[] twoSum(int[] nums, int target) {
        for(int i = 0 ; i < nums.length - 1; i ++){
            int one = nums[i];
            for (int j = i ; j < nums.length; j++){
                int two = nums[j];
                if ((one + two) == target){
                    return new int[]{i,j};
                }
            }
        }
        return new int[]{0,0};
    }
}
