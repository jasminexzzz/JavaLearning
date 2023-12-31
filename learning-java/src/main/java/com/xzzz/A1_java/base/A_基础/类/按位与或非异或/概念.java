package com.xzzz.A1_java.base.A_基础.类.按位与或非异或;

public class 概念 {

    /**

    一. &为按位与运算
        参加运算的两个数据，按二进制位进行“与”运算.  (注意,二进制运算实际均为补码运算)
        运算规则：0&0=0;  0&1=0;   1&0=0;    1&1=1;
        即：两位同时为“1”，结果才为“1”，否则为0

        例如：
        3 & 5          即
        0000 0011
        0000 0101
        ───────────────────────────────
        00000001  因此，3&5的值得1
        ----------------------------------------------------------------------------------
        15 & 0x7FFFFFFF 即
        01111111111111111111111111111111
        &&&&
        00000000000000000000000000001111
        00000000000000000000000000001111 补码
        ───────────────────────────────────
        00000000000000000000000000001111 补码 因此，15 & 0x7FFFFFFF 的值得15
        ----------------------------------------------------------------------------------
        -17 & 0x7FFFFFFF 即
        01111111111111111111111111111111
        &&&&&
        10000000000000000000000000010001
        11111111111111111111111111101110 反码
        11111111111111111111111111101111 补码
        ───────────────────────────────────
        01111111111111111111111111101111 即 2147483631,可用System.out.println(0x7fffffff & -17)来检测手工计算是否准确
        因此，-17 & 0x7FFFFFFF 的值得2147483631



    二. | 为按位或运算
        参加运算的两个数据，按二进制位进行“与”运算,注意,二进制运算实际均为补码运算
        运算规则：0|0=0;  0|1=1;   1|0=1;    1|1=1;
              即：两位同时为“1”，结果才为“1”，否则为0
        例如：
        -15 & 0x80000000 即
        10000000000000000000000000000000
        ||||
        10000000000000000000000000001111
        11111111111111111111111111110001 补码
        ────────────────────────────────
        11111111111111111111111111110001 补码
        11111111111111111111111111110000 反码
        10000000000000000000000000001111 源码 因此，-15 & 0x80000000 的值得 -15
        ----------------------------------------------------------------------------------
        15 & 0x80000000 即
        10000000000000000000000000000000
        ||||
        00000000000000000000000000001111
        ────────────────────────────────
        10000000000000000000000000001111 计算完是补码
        10000000000000000000000000001110 反码
        11111111111111111111111111110001 源码 因此，15 & 0x80000000 的值得-2147483663

    三. 位异或运算（^）

        运算规则是：两个数转为二进制，然后从高位开始比较，如果相同则为0，不相同则为1。

        比如：8^11.

        8转为二进制是1000，11转为二进制是1011.从高位开始比较得到的是：0011.然后二进制转为十进制，就是Integer.parseInt("0011",2)=3;
     */
}
