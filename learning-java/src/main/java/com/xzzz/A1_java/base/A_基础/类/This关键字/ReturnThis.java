package com.xzzz.A1_java.base.A_基础.类.This关键字;

/**
 * 当this 作为对象的默认引用时，程序可以像访问普通引用变量一样访问这个this引用
 */
public class ReturnThis {
    public int age;
    public ReturnThis grow(){
        age++;
        return this;
    }

    public static void main(String[] args) {
        ReturnThis re = new ReturnThis();
        re.grow().grow().grow();
        System.out.println(re.age);
    }
}
