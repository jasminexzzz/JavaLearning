package com.xzzz.B1_data_structure.散列表哈希表结构.链接法解决碰撞;


@SuppressWarnings("all")
public class SingleIntSet3
{
    private Object[] values = new Object[10];

    /**
     * 对数据进行处理
     * @param value
     * @return
     */
    private int H(int value) throws Exception {
        if (value >= 0 && value <= 9)
            return value;
        else
            return value - 10;
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
        SingleIntSet3 set = new SingleIntSet3();

        try {
            set.add(3);
            set.add(17);
            System.out.println(set.contains(3));  // 输出 true
            System.out.println(set.contains(17)); // 输出 true
            System.out.println(set.contains(13)); // 输出 false

            set.add(13);
            System.out.println(set.contains(13)); // 输出 true
            System.out.println(set.contains(3)); // 输出 false,但是应该输出 true 才对！

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
