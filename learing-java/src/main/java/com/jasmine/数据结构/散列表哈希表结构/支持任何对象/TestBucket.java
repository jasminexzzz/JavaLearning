package com.jasmine.数据结构.散列表哈希表结构.支持任何对象;

public class TestBucket {

    public int hash;
    public int _k;

    //标记该位置是否发生过碰撞 ture:已碰撞;false:未碰撞
    public boolean isCollided;


    //查看除去二进制第一位正负标识位后的数

    public int get_k() {
        return _k & (int)0x7FFFFFFF;
    }

    public void set_k(int hash) {
            /*
            将 _k 除了最高的碰撞位之外的其它位全部设为0
            &= : _k = _k & 874

            这段代码的意思是
            _k值可能有0和-2147483648两个
            也就是二进制的
            00000000000000000000000000000000
            或
            10000000000000000000000000000000

            如果_k为正数则为0,为负数则为-2147483648
             */
        _k &= 0x80000000;
        // 保持 _k 的最高的碰撞位不变，将 value 的值放到 _k 的后面几位中去
        this._k |= hash;
    }

    public boolean getIsCollided() {
        return (_k & (int)0x80000000) != 0;// _k 的最高位如果为1表示发生过碰撞
    }

    // 将槽标记为发生过碰撞,把_k变为负数
    public void markCollided() {
        //0x80000000 = -2147483648 = 10000000000000000000000000000000
        /*
            将 _k 的最高位设为1
            若_k为正数,则结果为最大负数+该正数
            若_k为负数,则结果为负数本身
            反正结果都是负数,也就是说把_k搞成了一个负数
         */
        _k |= (int)0x80000000;
    }

    public static void main(String[] args) {
        System.out.println(1 & (int)0x80000000);

        TestBucket b = new TestBucket();
        b.hash = "1".hashCode();

        System.out.println("hash的值: " + b.hash);
        System.out.println("b的值: " + b._k);
        b.markCollided();
        System.out.println("碰撞后b的值: " + b._k);
        b.getIsCollided();
        b.set_k(b.hash);
        System.out.println("碰撞后b的值: " + b._k);


    }
}
