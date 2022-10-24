package com.jasmine.B1_data_structure.散列表哈希表结构.开放寻址法解决碰撞;

@SuppressWarnings("all")
public class IntSet3
{
    private Object[] values = new Object[10];
    private static final int DELETED = -1;

    public Object[] getValues() {
        return values;
    }

    private int H(int value) {
        return value % 10;
    }

    private int LH(int value, int i) {
        System.out.print(
                "数值: " + value +
                ", 循环次数: " + i +
                ", 余数: " + ((H(value) + i + i * i) % 10));
        return (H(value) + i + i * i) % 10;
    }

    public void add(int item) throws Exception {
        int i = 0; // 已经探查过的槽的数量
        System.out.println("******");
        do {
            int j = LH(item, i); // 想要探查的地址
            //如果这个地址为Null或曾经被删除而重置成了-1,则将元素插入到此位置
            if (values[j] == null || (int)values[j] == DELETED) {
                System.out.println(", 插入位置:" + j);
                values[j] = item;
                return;
            } else {
                System.out.println();
                i += 1;
            }
        } while (i <= 10);
        System.out.println("******");
        throw new Exception("集合溢出");
    }

    /**
     * 查询元素是否存在于数组中
     * @param item 查询的值
     * @return
     */
    public boolean contains(int item)
    {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        do {
            //该元素在数组中的存放的最初始位置
            j = LH(item, i);
            //如果最初位置不存在任何值,则认为数组中不存在该值
            if (values[j] == null)
                return false;

            //如果存在其他值但不为查询的值,则查询下一个位置
            if ((int)values[j] == item)
                return true;
            else
                i += 1;
        } while (i <= 10);
        return false;
    }

    public void Remove(int item) {
        int i = 0; // 已经探查过的槽的数量
        int j = 0; // 想要探查的地址
        do {
            j = LH(item, i);
            //如果这个数值对应的地址为空,则认为没有这个数值存在于数组中,认为删除成功
            if (values[j] == null)
                return;

            //如果这个数组中存在该元素,则把该位置重置成-1,来认为已经删除了该元素
            if ((int)values[j] == item) {
                values[j] = DELETED;
                return;
            }
            else {
                i += 1;
            }
        } while (i <= 10);
    }

    public static void main(String[] args) {
        IntSet3 set = new IntSet3();
        try {
            set.add(1);
            set.add(1);
            set.add(2);
            set.add(3);
            set.add(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0 ; i < set.getValues().length ; i ++){
            System.out.println(set.getValues()[i]);
        }
    }
}
