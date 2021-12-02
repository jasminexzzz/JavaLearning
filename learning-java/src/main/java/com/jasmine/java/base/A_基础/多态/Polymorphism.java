package com.jasmine.java.base.A_基础.多态;

/**
 * 多态的例子
 * 名词：
 * 	超类：也叫父类
 * 定义：
 * 	方法的重写Overriding和重载Overloading是Java多态性的不同表现,
 * 	重写Overriding是父类与子类之间多态性的一种表现，
 * 	重载Overloading是一个类中Java多态性的一种表现.
 * 	继承后的调用顺序：this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)
 * 	自己的show方法，父类的show方法，自己的show方法且参数为父类，父类的show方法且参数为父类，若父类仍旧没有，则参数改传父类的父类
 */
public class Polymorphism {

    public static void main(String[] args)
    {
	//正常的创建对象，和继承的子类没有关系
        A a1 = new A();
        /*
         * 只会有B重写了A的方法和A中的方法，且调用的方法为A中的方法
         */
        A a2 = new B();
        //正常的创建对象，如果重写父类方法则调用的是重写的方法，如果没有重写的只是单纯继承
        B b = new B();
        C c = new C();
        D d = new D();

        //1.输出为A类的A方法：因为B继承自A，所以传入B其实和传入A一样，除非A中也有形参为B的方法
        System.out.println("1 ---> "+a1.show(b));
        
        //2.输出为A类的A方法：因为C继承自B，且B继承自A，所以传入C其实和传入A一样，除非A中也有形参为C的方法
        System.out.println("2 ---> "+a1.show(c));
        
        //3.输出为A类的D方法：因为D继承自B，且B继承自A，同时A中含有D方法，所以传入D时会进入到A的D方法
        System.out.println("3 ---> "+a1.show(d));
        
        /* 4.
         * a2.show(b)，a2是一个引用变量，类型为A，则this为a2，b是B的一个实例
         * 于是它到类A里面找show(B obj)方法，没有找到
         * 于是到A的super(父类)找，而A没有父类，因此转到第三优先级this.show((super)O)
         * this仍然是a2，这里O为B，(super)O即(super)B即A，因此它到类A里面找show(A obj)的方法
         * 类A有这个方法，但是由于a2引用的是类B的一个对象，B覆盖了A的show(A obj)方法，因此最终锁定到类B的show(A obj)
         * 输出为"B and A”。
         */
        System.out.println("4 ---> "+a2.show(b));
        System.out.println("5 ---> "+a2.show(c));
        System.out.println("6 ---> "+a2.show(d));
        System.out.println("7 ---> "+b.show(b));
        System.out.println("8 ---> "+b.show(c));
        System.out.println("9 ---> "+b.show(d));
    }
}

class A{
    public String show(D obj){return ("A类的D方法");}
    public String show(A obj){return ("A类的A方法");}
}

class B extends A{
    public String show(B obj){return ("B类的B方法");}
    public String show(A obj){return("B类的A方法");}
}
class C extends B{}
class D extends B{}
