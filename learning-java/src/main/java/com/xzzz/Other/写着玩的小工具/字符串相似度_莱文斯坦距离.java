package com.xzzz.Other.写着玩的小工具;

/**
 * @author wangyf
 */
public class 字符串相似度_莱文斯坦距离 {

    public static float levenshtein(String str1, String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][]dif = new int[len1 + 1][ len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] =a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] =a;
        }
        //计算两个字符是否一样，计算左上的值
        char [] ch1 = str1.toCharArray();
        char [] ch2 = str2.toCharArray();
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (ch1[i - 1] == ch2[j - 1]) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                int temp1 = dif[i - 1][j - 1]+temp;
                int temp2 = dif[i][j - 1]+1;
                int temp3 = dif[i - 1][j]+1;
                int arr [] =  new int[]{temp1,temp2, temp3};

                dif[i][j] =min(arr);
            }
        }
        //计算相似度
        float similarity = 1 - (float) dif[len1][len2] /Math.max(str1.length(), str2.length());
        return similarity;
    }

    //得到最小值
    private static int min(int[]arr) {
        int min = arr[0];
        for( int i :arr){
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        System.out.println(levenshtein("计算两个字符是否一样", "计算是否一样两个字符"));
    }
}
