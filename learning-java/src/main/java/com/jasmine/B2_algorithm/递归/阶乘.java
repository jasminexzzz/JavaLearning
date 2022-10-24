package com.jasmine.B2_algorithm.递归;

/**
 * 算N的阶乘
 * @see com.jasmine.Other.基础数学知识.算法.阶乘
 */
public class 阶乘 {

    /**
     *
     * @param n 从1开始几个数的阶乘 例如2个数的阶乘就是2,3个数的阶乘就是6
     * @return
     */
    static int f(int n){
        if(n <= 2){
            return n;
        }
        return f(n - 1) * n;
    }

    public static void main(String[] args) {
        System.out.println(f(6));
    }
}
