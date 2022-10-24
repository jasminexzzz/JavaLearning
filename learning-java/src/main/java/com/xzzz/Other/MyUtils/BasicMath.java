package com.xzzz.Other.MyUtils;

public class BasicMath {

    // 质数表
    public static final int[] PRIMES = {
            3, 7, 11, 17, 23, 29, 37, 47, 59, 71, 89, 107, 131, 163, 197, 239, 293, 353, 431, 521, 631, 761, 919,
            1103, 1327, 1597, 1931, 2333, 2801, 3371, 4049, 4861, 5839, 7013, 8419, 10103, 12143, 14591,
            17519, 21023, 25229, 30293, 36353, 43627, 52361, 62851, 75431, 90523, 108631, 130363, 156437,
            187751, 225307, 270371, 324449, 389357, 467237, 560689, 672827, 807403, 968897, 1162687, 1395263,
            1674319, 2009191, 2411033, 2893249, 3471899, 4166287, 4999559, 5999471, 7199369};



    /**
     * 判断 candidate 是否是质数
     * @param candidate
     * @return
     */
    private static boolean isPrime(int candidate) {
        if ((candidate & 1) != 0){ // 是奇数
            int limit = (int)Math.sqrt(candidate);
            for (int divisor = 3; divisor <= limit; divisor += 2){ // divisor = 3、5、7...candidate的平方根
                if ((candidate % divisor) == 0)
                    return false;
            }
            return true;
        }
        return (candidate == 2); // 除了2，其它偶是全都不是质数
    }



    /**
     * 如果 min 是质数，返回 min；否则返回比 min 稍大的那个质数
     * @param min 参数,min 是质数，返回 min；否则返回比 min 稍大的那个质数.
     * @return
     */
    public static int getPrime(int min) {
        // 从质数表中查找比 min 稍大的质数
        for (int i = 0; i < PRIMES.length; i++) {
            int prime = PRIMES[i];
            if (prime >= min) return prime;
        }

        // min 超过了质数表的范围时，探查 min 之后的每一个奇数，直到发现下一个质数
        for (int i = (min | 1); i < Integer.MAX_VALUE; i += 2) {
            if (isPrime(i))
                return i;
        }
        return min;
    }
}
