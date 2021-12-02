package com.jasmine.java.base.B_集合.collection.list;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * arraylist 和 linked 新增查询效率对比
 */
abstract class Template{

    public abstract void test();

    public void template(String str){
        long start = System.currentTimeMillis();
        test();
        long end = System.currentTimeMillis();
        System.out.println(str + " : " + (end - start) + "毫秒");
    }
}


public class ArrayAndLinked{
    
    public static final int MAX_NUM = 200000;
    
    public static void main(String[] args){

        /*
         * 数组在0位置增
         */
        Template t1 = new Template(){
            @Override
            public void test(){
                List<String> list = new ArrayList<>();
                for(int i = 1; i < MAX_NUM ; i++){
                    list.add(0,"a");//测试ArrayList的增
                }
            }
        };

        /*
         * 链表在0位置增
         * 链表在中间位置增
         */
        Template t2 = new Template(){
            @Override
            public void test(){
                List<String> list = new LinkedList<>();
                for(int i = 1; i < MAX_NUM ; i++){
//                    list.add(0,"a"); //测试LinkedList的增,首位置
                    list.add(i >> 1,"a");
                }
            }
        };

        /*
         * 数组查
         */
        Template t3 = new Template(){
            @Override
            public void test(){
                List<String> list = new ArrayList<>();
                for(int i = 1; i < MAX_NUM ; i++){
                    list.add("a");
                }
                for(int i = 1; i< MAX_NUM ; i++){
                    //测试ArrayList的查
                    list.get(i >> 1);
                }
            }
        };

        /*
         * 链表的查
         */
        Template t4 = new Template(){
            @Override
            public void test(){
                List<String> list = new LinkedList<>();
                for(int i = 1; i < MAX_NUM ; i++){
                    list.add("a");
                }
                for(int i = 1; i< MAX_NUM ; i++){
                    //测试LinkedList的查
                    list.get(i / 2);
                }
            }
        };
        t1.template("ArrayList  的增");
//        t2.template("linkedlist 的增");
//        t3.template("ArrayList  的查");
//        t4.template("linkedlist 的查");


        /*
        100W插入查询时间
        ArrayList的增 : 103924
        LinkedList的增 : 在中间插入(i >> 1):1114445,在末尾插入:33
        ArrayList的查 : 24
        LinkedList的查 : 797246
         */
    }
}
