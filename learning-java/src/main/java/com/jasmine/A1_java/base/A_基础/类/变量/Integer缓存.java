package com.jasmine.A1_java.base.A_基础.类.变量;

public class Integer缓存 {
    public static void main(String[] args) {
        Integer i1 = 6;
        Integer i2 = 6;
        System.out.println((i1==i2));//true


        Integer i3 = new Integer(6);
        Integer i4 = new Integer(6);

        System.out.println((6==i3));
        System.out.println((128==i3));
        System.out.println((i4==128));

        System.out.println((i3==i4)+" "+i3.hashCode()+" "+i4.hashCode());
        /**
         *
         * a.当数值范围为-128~127时：如果两个new出来Integer对象，即使值相同，通过“==”比较结果为false，但两个对象直接赋值，则通过“==”比较结果为“true，这一点与String非常相似。
         * b.当数值不在-128~127时，无论通过哪种方式，即使两个对象的值相等，通过“==”比较，其结果为false；
         * c.当一个Integer对象直接与一个int基本数据类型通过“==”比较，其结果与第一点相同；
         * d.Integer对象的hash值为数值本身；
         */
    }

}

