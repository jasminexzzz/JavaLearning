package com.jasmine.JavaBase.A_基础.类.Lambda表达式.例子4;

public class Process {

    public void pro(String name,int age,LamIF lam){
        System.out.println("1.名字是：" + name + "，年龄是:" + age);
        lam.output(name,age);
    }

    public static void main(String[] args) {
        Process p = new Process();
        p.pro("小杨",10,
            (name1,age1)->{
                System.out.println("2.名字是：" + name1 + "，年龄是:" + age1);
            }
        );
    }
}
