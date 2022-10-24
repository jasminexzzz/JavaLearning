package com.jasmine.B2_algorithm.LeetCode.数组;

/**
 * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
 * 你可以假设数组中无重复元素。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/search-insert-position/
 */
public class ARR_0035_EASY_搜索插入位置 {


    /**
     * 暴力解法 O(n)
     */
    public static int searchInsert1(int[] nums, int target) {
        for (int i = 0 ; i < nums.length ; i++) {
            if (target <= nums[i]) {
                return i;
            }
        }
        return nums.length;
    }

    /**
     * 二分法 O(lng2)
     *
     * demo : 1,2,3,4,5,6 | 5
     *
     * [1],2,3,4,5,[6] | left:0  right:5  mid:2 |
     *      nums[mid] = target |  return
     *      nums[mid] < target |  left + 1  <执行>
     *      nums[mid] > target | right - 1
     * ────────────────────────────────────────────────
     * 1,2,3,[4],5,[6] | left:0  right:5  mid:2 |
     *      nums[mid] = target |  return    <执行>
     *      nums[mid] < target |  left + 1
     *      nums[mid] > target | right - 1
     */
    public static int searchInsert2(int[] nums, int target) {
        int
        left = 0,
        right = nums.length - 1; // 注意边界问题
        // 注意
        while(left <= right) {
            // 注意
            int mid = left + (right - left) / 2;
            // 注意
            if(nums[mid] == target) {
                // 相关逻辑
                return mid;
            } else if(nums[mid] < target) {
                // 注意
                left = mid + 1;
            } else {
                // 注意
                right = mid - 1;
            }
        }
        // 相关返回值
        return 0;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {1,2,3,4,5,6};
        int target = 5;
        int num = searchInsert2(nums,target);
        System.out.println(num);
    }
}
