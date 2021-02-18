package com.jasmine.Other.MyUtils;


import java.util.Random;
import java.util.function.Predicate;

/**
 * 邀请码生成器
 * 支持靓号生成
 * @author : wangyf
 */
@SuppressWarnings("all")
public class InvCodeUtil {

    /**
     * 不包含0(零), 容易和字母O混淆
     */
    private static final char[] CODE_ITEM = {
        'A', 'B', 'C', 'D', 'E',
        'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O',
        'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z',

        '1', '2', '3', '4', '5',
        '6', '7', '8', '9', // '0'
    };

    /**
     * 邀请码长度, 推荐在 8~10 位之间
     *
     * 长度不同产生的碰撞概率如下 (生成的邀请码越多, 碰撞概率越大)
     *   邀请码位数  生成数     重复数
     *   6位邀请码 | 300万 | 大约2500个以内
     *   7位邀请码 | 300万 | 大约80个以内
     *   8位邀请码 | 300万 | 5个以内
     */
    private static final int INV_CODE_LENGTH = 8;

    /**
     * CODE_ITEM 数组的长度, 用于确定随机数的区间
     * 如 RANDOM_RANGE = 35, 则会生成 0~34 的随机数
     */
    private static final int RANDOM_RANGE = CODE_ITEM.length;

    /**
     * 生成普通邀请码
     * @return 邀请码
     */
    public static String getInvCode(){
        return getInvCode(false);
    }

    /**
     * 生成随机靓号邀请码
     * @return 邀请码
     */
    public static String getGoodInvCode(){
        return getInvCode(true);
    }

    /**
     * 生成邀请码
     * @param isGood 是否靓号
     * @return 邀请码
     */
    public static String getInvCode(boolean isGood){
        if (isGood) {
            int randomGood = new Random().nextInt(GoodCode.values().length - 1);
            return getInvCode(GoodCode.values()[randomGood]);
        } else {
            // 随机邀请码
            return getInvCode(GoodCode.RANDOM);
        }
    }

    /**
     * 按模板生成邀请码
     * @param goodCode 靓号模板
     * @return 邀请码
     */
    public static String getInvCode(GoodCode goodCode){
        int[] arr = genRandomArray(goodCode);
        StringBuilder invCode = new StringBuilder();
        for (int value : arr) {
            invCode.append(CODE_ITEM[value]);
        }
        return invCode.toString();
    }

    /**
     * 生成随机数数组, 作为 code_item 的下标
     * @param goodCode 靓号模板
     * @return 随机数组成的数组
     */
    private static int[] genRandomArray (GoodCode goodCode) {
        if (INV_CODE_LENGTH < 5) {
            throw new RuntimeException("邀请码长度不得小于5");
        }
        int[] arr = new int[INV_CODE_LENGTH];
        switch (goodCode) {
            /**
             * 全部相同,这种靓号的个数与 CODE_ITEM 的长度相同
             */
            case AAA: {
                arr = getCodeArray(null);break;
            }
            /**
             * 尾部不同, 偶数包含左右平分的情况
             */
            case AAB: {
                int tail = INV_CODE_LENGTH / 2;
                int randomTail = new Random().nextInt(tail) + 1;
                arr = getCodeArray((i) -> INV_CODE_LENGTH - i <= randomTail);
                break;
            }
            /**
             * 头部不同, 偶数包含左右平分的情况
             */
            case ABB: {
                int head = INV_CODE_LENGTH / 2;
                int randomHead = new Random().nextInt(head) + 1;
                arr = getCodeArray((i) -> i >= randomHead);
                break;
            }
            /**
             * 左右不同, 奇数包含头部不同的情况,奇数的情况下即为 [ABB]
             */
            case AB:  {
                arr = getCodeArray((i) -> i > INV_CODE_LENGTH / 2 - 1);break;
            }
            /**
             * 头尾与中间不同, 如果邀请码长度为偶数, 为了防止出现 [AB] 的情况, 头尾长度需要-1
             */
            case ABA: {
                int headTail = INV_CODE_LENGTH / 2;
                if (headTail % 2 == 0 && INV_CODE_LENGTH % 2 == 0) headTail--; // 排除掉 [AAABBB] 这种左右平均的组合
                int randomheadTail = new Random().nextInt(headTail) + 1;
                arr = getCodeArray((i) -> randomheadTail <= i && i < INV_CODE_LENGTH - randomheadTail);
                break;
            }
            /** 交叉不同
             * 1. 计算交叉组合的数量,交叉组合的数量,交叉组合至少需要3组,所以:
             *    组合数 = 邀请码长度 / 2
             *    如长度为5 ,则有 2 种情况:
             *      1. ABABA
             *      2. AABBA
             *         AAABB - 不包含,这种组合为2组,属于[AAB] 的情况
             *    如长度为13,则有 6 种情况:
             *      1. ABABABABABABA
             *      2. AABBAABBAABBA
             *      3. AAABBBAAABBBA
             *      4. AAAABBBBAAAAB
             *      5. AAAAABBBBBAAA
             *      6. AAAAAABBBBBBA - 6个一组
             *         AAAAAAABBBBBB - 不包含,这种组合为2组,属于[AAB] 的情况
             *
             * 2. 遍历下标数组, 如果下标可以整除每组的个数, 说明当前下标的邀请码到了变更的时候,即如下变更
             * <code>
             *    if (i % actionGroup == 0) {
             *        if (T == B) {
             *            T = A;
             *        } else {
             *            T = B;
             *        }
             *    }
             * <code/>
             *
             *
             */
            case ABAB: {
                Random r = new Random();
                // 交叉组合的数量,交叉组合至少需要2个交叉,所以 / 2
                int groupBum = INV_CODE_LENGTH / 2;
                // 排除掉 [AAABBB] 这种左右平均的组合
                if (groupBum % 2 == 0 && INV_CODE_LENGTH % 2 == 0) groupBum--;
                int A = r.nextInt(RANDOM_RANGE);
                int B = reGen(A,r);
                int T = A;
                int actionGroup = r.nextInt(groupBum) + 1;
                // 循环邀请码数组下标的位数与当前分组的个数整除, 则说明需要更换数组中的值
                for (int i = 0; i < INV_CODE_LENGTH; i++) {
                    // 如果下标
                    if (i % actionGroup == 0) {
                        if (T == B) {
                            T = A;
                        } else {
                            T = B;
                        }
                    }
                    arr[i] = T;
                }
                break;
            }
            /**
             * 顺序递增
             */
            case ABC: { // 递增
                Random r1 = new Random();
                int A = r1.nextInt(RANDOM_RANGE);
                for (int i = 0; i < INV_CODE_LENGTH; i++) {
                    int index = A + i;
                    // 随机数递增后需要重置为0
                    if (index > RANDOM_RANGE - 1) {
                        index = (index - RANDOM_RANGE);
                    }
                    arr[i] = index;
                }
                break;
            }
            /**
             * 纯随机生成
             */
            case RANDOM: {
                Random r2 = new Random();
                for (int i = 0; i < INV_CODE_LENGTH; i++) { arr[i] = r2.nextInt(RANDOM_RANGE); }
                break;
            }
        }

        return arr;
    }

    /**
     * 判断以函数方式传入,减少重复代码
     * @param predicate
     * @return 内容为 CODE_ITEM 的数组
     */
    private static int[] getCodeArray(Predicate<Integer> predicate) {
        int[] arr = new int[INV_CODE_LENGTH];
        Random r = new Random();
        int A = r.nextInt(RANDOM_RANGE);
        int B = reGen(A,r);
        for (int i = 0; i < INV_CODE_LENGTH; i++) {
            arr[i] = A;
            if (predicate != null) {
                if (predicate.test(i)) {
                    arr[i] = B;
                }
            }
        }
        return arr;
    }

    /**
     * 生成一个与A不同的随机数,若相同重新生成
     * @param a A
     * @param r Random
     * @return B
     */
    private static int reGen (int a,Random r) {
        int b = r.nextInt(RANDOM_RANGE);
        if (b == a) {
            return reGen(a,r);
        }
        return b;
    }

    public static void main(String[] args) {
        for (int i = 0 ; i < 10000; i++) {
            System.out.println(getInvCode(true));
        }
    }

    enum GoodCode {
        AAA,
        AAB,
        ABB,
        AB,
        ABA,
        ABAB,
        ABC,
        // 非靓号
        RANDOM
        ;
    }

}
