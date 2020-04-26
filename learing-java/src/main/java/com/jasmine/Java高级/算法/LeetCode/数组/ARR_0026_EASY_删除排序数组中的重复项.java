package com.jasmine.Java高级.算法.LeetCode.数组;


/**
 * <code>
 * 方法：双指针法
 * 算法
 * 作者：LeetCode
 * 链接：https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/solution/shan-chu-pai-xu-shu-zu-zhong-de-zhong-fu-xiang-by-/
 */
public class ARR_0026_EASY_删除排序数组中的重复项 {


    /**
     * 0 | 0,[0],1,1,1,2,2,3,3,4 | false | 0
     * 0 | 0,0,[1],1,1,2,2,3,3,4 | true  | 1
     * 1 | 0,0,1,[1],1,2,2,3,3,4 | false | 1
     * 1 | 0,0,1,1,[1],2,2,3,3,4 | false | 1
     * 1 | 0,0,1,1,1,[2],2,3,3,4 | true  | 2
     * 2 | 0,0,1,1,1,2,[2],3,3,4 | false | 2
     * 2 | 0,0,1,1,1,2,2,[3],3,4 | true  | 3
     * 2 | 0,0,1,1,1,2,2,3,[3],4 | false | 3
     * 2 | 0,0,1,1,1,2,2,3,3,[4] | true  | 4
     *
     * return 4
     */
    public static int removeDuplicates(int[] nums) {
        int length = 0;
        for (int i = 1 ; i < nums.length ; i++) {
            if(nums[i] != nums[length]) {
                length ++;
                nums[length] = nums[i];
            }
        }
        return length + 1;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{0,0,1,1,1,2,2,3,3,4};
        int length = ARR_0026_EASY_删除排序数组中的重复项.removeDuplicates(nums);
        System.out.println(length + "\n======");
        for (int i = 0 ; i < length ; i++){
            System.out.println(nums[i]);
        }
    }
}
