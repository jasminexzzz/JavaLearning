package com.jasmine.B1_data_structure.散列表哈希表结构.链接法解决碰撞;

import com.jasmine.Other.MyUtils.BasicMath;

import java.util.LinkedList;

/**
 * 除法散列法计算一个散列表的容量,以最大程度减少碰撞
 */
@SuppressWarnings("all")
public class IntSet2
{
    private Object[] values;

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    //构造数组,传入容量
    public IntSet2(int capacity) {
        int size = BasicMath.getPrime(capacity);
        values = new Object[size];
    }



    private int H(int value)
    {
        return value % values.length;
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
        IntSet2 set = new IntSet2(30);
        System.out.println(set.getValues().length);
    }
}