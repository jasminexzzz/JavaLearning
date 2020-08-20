package com.jasmine.JavaBase.A_基础.多态;

/**
 * 多态的简单练习以及引用对象的理解
 */
public class EasyPoly {
    public static void main(String[] args) {
        X xx = new X();
        System.out.println(xx.showA());
        System.out.println(xx.showB());

        //只能调用重写和继承来的方法，对于自己独有的方法是无法调用的
        //xy是Y的实例
        X xy = new Y();
        System.out.println(xy.showA());
        System.out.println(xy.showB());
        //System.out.println(xy.BMethod());对于自己独有的方法是无法调用的
    }
}
class X{
    public String showA(){
        return ("X类的showA方法");
    }

    public String showB(){
        return ("X类的showB方法");
    }
}

class Y extends X{
    public String showA(){
        return ("Y类的showA方法");
    }

    public String BMethod(){
        return ("B自己独有的方法");
    }
}
