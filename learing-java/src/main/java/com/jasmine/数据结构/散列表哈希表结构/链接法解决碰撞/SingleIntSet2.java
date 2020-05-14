package com.jasmine.数据结构.散列表哈希表结构.链接法解决碰撞;


public class SingleIntSet2
{
    private Object[] values = new Object[10];

    /**
     * 对数据进行处理
     * @param value
     * @return
     */
    private int H(int value) throws Exception {
        if(9 < value && value < 20)
            return value - 10;
        else
            throw new Exception("该数组只能保存10 - 19的数字");
    }

    public void add(int item) throws Exception {
        values[H(item)] = item;
    }

    public void remove(int item) throws Exception {
        values[H(item)] = null;
    }

    public boolean contains(int item) throws Exception {
        if (values[H(item)] == null)
            return false;
        else
            return (int)values[H(item)] == item;
    }

    public static void main(String[] args)
    {
        SingleIntSet2 set = new SingleIntSet2();

        try {
            set.add(13);
            set.add(17);
            System.out.println(set.contains(13)); // 输出 true
            System.out.println(set.contains(15)); // 输出 false
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
