package com.jasmine.Other.写着玩的小工具;

/**
 * 随机选一个自己家的小猫
 * @author : jasmineXz
 */
public class CatChoice {
    static final int CAT_NUM = 5;//猫的只数
    static final int CYCLE_INDEX = 10000;//循环次数
    static final int [] catNum = new int[CAT_NUM];//从小到大分别代表小猫排名

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        choiceCat();
        tellResult();
        long end = System.currentTimeMillis();
        System.out.println("==> "+(end - start));
    }

    /**
     * 进行统计
     */
    public static void choiceCat(){
        for(int i = 0; i < CYCLE_INDEX ; i ++){
            catNum[getRandom()-1]++;
        }
    }

    /**
     * 输出结果
     */
    public static void tellResult(){
        if(catNum.length == 5){
            System.out.println("嘉文　　　：" + catNum[0]);
            System.out.println("妮蔻　　　：" + catNum[1]);
            System.out.println("艾瑞莉娅　：" + catNum[2]);
            System.out.println("维鲁斯　　：" + catNum[3]);
            System.out.println("锤石　　　：" + catNum[4]);
        }

        if(catNum.length == 3){
            System.out.println("维鲁斯　　　　　　：" + catNum[0]);
            System.out.println("艾瑞莉娅　　　　　：" + catNum[1]);
            System.out.println("维鲁斯和艾瑞莉娅　：" + catNum[2]);
        }


    }

    /**
     * 选择最多数
     */
    public static void maxName(){
        int maxName = 0;
        for(int i = 0,max = catNum.length; i < max; i++){
            if(catNum[i+1] > catNum[i]){
                maxName = i+1;
            }
        }
        System.out.println();
    }

    /**
     * 获取随机数
     */
    public static int getRandom(){
        return (int)(1 + Math.random() * (CAT_NUM-1+1));
    }
}
