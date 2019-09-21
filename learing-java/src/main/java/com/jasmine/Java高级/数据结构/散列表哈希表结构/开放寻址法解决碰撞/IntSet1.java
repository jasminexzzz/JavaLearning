package com.jasmine.Java高级.数据结构.散列表哈希表结构.开放寻址法解决碰撞;



@SuppressWarnings("all")
public class IntSet1 {
    private Object[] values = new Object[10];

    /**
     *
     * @param value
     * @return
     */
    private int H(int value) {
        return value % 10;
    }

    public Object[] getValues() {
        return values;
    }

    /**
     *
     * @param value 添加进数组的值,value如果已经在数组中
     * @param i 探查的次数
     * @return 返回value的值所在的数组的下标并 + i,然后取余数
     */
    private int LH(int value, int i) {
        return (H(value) + i) % 10;
    }

    /**
     * 添加进数组
     * @param item
     * @throws Exception
     */
    public void add(int item) throws Exception {
        int i = 0; // 已经探查过的槽的数量
        do {
            //开始在数组查询item是否存在
            int j = LH(item, i); // 想要探查的地址
            //如果这个值所在的数组中为空,则将item添加到数组中
            if (values[j] == null) {
                values[j] = item;
                return;
            }else{
                i += 1;
            }
        } while (i <= 10);
        throw new Exception("集合溢出");
    }

    public boolean contains(int item) {
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
        // 有点不太好办
    }

    public static void main(String[] args) {
        IntSet1 set = new IntSet1();
        try {
            set.add(4);
            set.add(5);
            set.add(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0 ; i < set.getValues().length ; i ++){
            System.out.println(set.getValues()[i]);
        }

        System.out.println("\n"+set.contains(3));
    }


}