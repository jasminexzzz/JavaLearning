package com.jasmine.JavaBase.A_基础.类.Lambda表达式.例子3;


public class Test {
    public static void main(String[] args) {
        /**
         * 这两个是等价的
         */
        Thread t = new Thread( new Runnable () {
            @Override
            public void run() {
                System.out.println("This is from an anonymous class.");
            }
        } );

        /**
         * 这个不需要显示的将Lambda表达式转换成一个Runnable，因为JAVA换根据上下文自动判断
         * 例如:
         * stream().filter(Lambda表达式会自动认为是一个Predicate)
         */
        Thread gaoDuanDaQiShangDangCi = new Thread( () -> {
            System.out.println("This is from an anonymous method (lambda exp).");
        } );
    }
}
