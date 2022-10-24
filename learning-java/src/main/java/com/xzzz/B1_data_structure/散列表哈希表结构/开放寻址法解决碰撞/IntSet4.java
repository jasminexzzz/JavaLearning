package com.xzzz.B1_data_structure.散列表哈希表结构.开放寻址法解决碰撞;


import com.xzzz.Other.MyUtils.BasicMath;

@SuppressWarnings("all")
public class IntSet4
{
    private Object[] values;
    private static final int DELETED = -1;

    public IntSet4(int capacity)
    {
        int size = BasicMath.getPrime(capacity);
        values = new Object[size];
    }

    public Object[] getValues() {
        return values;
    }

    // 质数表

    int H1(int value) {
        return value % values.length;
    }

    int H2(int value) {
        return 1 + (value % (values.length - 1));
    }

    int DH(int value, int i) {
        return (H1(value) + i * H2(value)) % values.length;
    }

    public void add(int item) throws Exception {
        int i = 0; // 已经探查过的槽的数量
        do {
            // ((item % item.length) + i * (1 + (item % item.length-1))) % item.length
            int j = DH(item, i); // 想要探查的地址
            if (values[j] == null || (int)values[j] == DELETED) {
                values[j] = item;
                return;
            } else {
                i += 1;
            }
        } while (i <= values.length);
        throw new Exception("集合溢出");
    }
    public boolean Contains(int item) {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        do {
            j = DH(item, i);
            if (values[j] == null)
                return false;

            if ((int)values[j] == item)
                return true;
            else
                i += 1;
        } while (i <= values.length);
        return false;
    }

    public void Remove(int item) {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        do {
            j = DH(item, i);
            if (values[j] == null)
                return;

            if ((int)values[j] == item) {
                values[j] = DELETED;
                return;
            } else {
                i += 1;
            }
        } while (i <= values.length);
    }

    public static void main(String[] args) {
        IntSet4 set = new IntSet4(10);
        try {
            for(int i = 0 ; i < set.getValues().length ; i++)
                set.add(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0 ; i < set.getValues().length ; i ++){
            System.out.println(set.getValues()[i]);
        }
    }
}