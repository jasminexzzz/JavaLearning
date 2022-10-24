package com.xzzz.B1_data_structure.散列表哈希表结构.链接法解决碰撞;

import java.util.LinkedList;

@SuppressWarnings("all")
public class SingleIntSet4 {
    private Object[] values = new Object[10];

    /*
    普通算法

    private int H(int value){
        if (value >= 0 && value <= 9)
            return value;
        else
            return value - 10;
    }*/

    /*
    除法散列法计算
     */
    private int H(int value)
    {
        return value % 10;
    }

    public void add(int item) {
        if (values[H(item)] == null){
            LinkedList ls = new LinkedList<Integer>();
            //添加到链表头
            ls.addFirst(item);
            //将链表放入数组
            values[H(item)] = ls;
        }else{
            //获取到链表,并将该元素添加到链表最后
            LinkedList<Integer> ls = (LinkedList<Integer>) values[H(item)];
            ls.addLast(item);
        }
    }

    public void remove(int item){
        LinkedList<Integer> ls =  (LinkedList<Integer>) values[H(item)];
        ls.remove(item);
    }

    public boolean contains(int item){
        if (values[H(item)] == null){
            return false;
        } else {
            LinkedList<Integer> ls =  (LinkedList<Integer>) values[H(item)];
            //ls中是否包含item,contains:检查是否包含该元素
            return ls.contains(item);
        }
    }

    public static void main(String[] args)
    {
        SingleIntSet4 set = new SingleIntSet4();

        try {
            set.add(3);
            set.add(17);
            System.out.println(set.contains(3));  // 输出 true
            System.out.println(set.contains(17)); // 输出 true
            System.out.println(set.contains(13)); // 输出 false

            set.add(13);
            System.out.println(set.contains(13)); // 输出 true
            System.out.println(set.contains(3));  // 输出 true


            System.out.println(set.H(3));  // 输出 true
            System.out.println(set.H(13)); // 输出 true
            System.out.println(set.H(83)); // 输出 true
            System.out.println(set.H(17)); // 输出 true

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}