package com.jasmine.A1_java.base.A_基础.类.参数;

public class 参数 {

    public void test(int a ,String...books) {
        System.out.println(a);
        for (String str : books) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) {
        参数 a = new 参数();
        /**
         * test中的a 和 books为形参，1,"111","222"为实参
         */

        a.test(1, "111", "222");
    }
}
