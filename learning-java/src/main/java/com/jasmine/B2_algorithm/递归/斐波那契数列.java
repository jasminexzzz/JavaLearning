package com.jasmine.B2_algorithm.递归;

/**
 * 斐波那契数列
 * 即 : 1, 1, 2, 3, 5, 8, 13, 21, 34
 * 即 :
 *      第1项 f(1) = 1
 *      第2项 f(2) = 1
 *      第n项 f(n) = f(n-1) + f(n-2)
 */
public class 斐波那契数列 {

    static int f(int n){
        if(n <= 2){
            return 1;
        }
        return f(n-1) + f(n - 2);
    }

    public static void main(String[] args) {
        System.out.println(f(8));
    }
}
