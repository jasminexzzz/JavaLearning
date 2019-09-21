package com.jasmine.Java高级.数据结构.散列表哈希表结构.链接法解决碰撞;

/**
 * O(1) 复杂度的容器 SingleIntSet ，它只能存储 0～9 这10个数字
 */
public class SingleIntSet
{
    private Object[] values = new Object[10];

    public void add(int item)
    {
        values[item] = item;
    }

    public void remove(int item)
    {
        values[item] = null;
    }
    public boolean contains(int item)
    {
        if (values[item] == null)
            return false;
        else
            return (int)values[item] == item;
    }

    public static void main(String[] args)
    {
        SingleIntSet set = new SingleIntSet();
        set.add(3);
        set.add(7);
        System.out.println(set.contains(3)); // 输出 true
        System.out.println(set.contains(5)); // 输出 false
    }
}