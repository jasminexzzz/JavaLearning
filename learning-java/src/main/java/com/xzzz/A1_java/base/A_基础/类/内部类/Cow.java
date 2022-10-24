package com.xzzz.A1_java.base.A_基础.类.内部类;


/**
 * 内部类使用总结
 * 1.在外部类中使用内部类
 *  静态成员（静态方法和静态代码块）无法使用非静态内部类，静态成员不能访问非静态成员
 * 2.在外部类意外使用非静态内部类
 *  1）不能使用Private修饰
 *  2）省略访问控制符，只能被与外部类处于同一个包中的其他类所访问
 *  3）使用protected修饰的内部类，可被外部类同包，和外部类的子类访问
 *  4）public修饰的内部类可以被访问，
 *     例如：
 *     OuterClass.InnerClass varName = new OuterClass().new InnerClass();
 *     Cow.CowLeg a = new Cow().new CowLeg();
 *     非静态内部类in类的构造器必须使用外部类的对象来调用
 * 3.在外部类意外使用静态内部类
 *  1）因为静态内部类是外部类类相关的，所以无需创建外部类的对象即可创建内部类实例
 *     例如：
 *     OuterClass.InnerClass varName = new OuterClass.InnerClass();
 *
 *  2）继承静态内部类也很简单
 *     public class className extends OuterClass.InnerClass {}
 *
 *  3）所以当程序需要使用内部类时，应该优先考虑使用静态内部类
 *
 * 4.局部内部类
 *  1）内部类定义在方法内，则为局部内部类，尽在该方法内有效。
 *  2）不能使用访问控制符和static修饰符
 *
 * 5.java8改进的匿名内部类
 *  1）必须继承一个父类或实现一个接口，但最多只能继承一个或实现一个
 *  2）不能是抽象类，因为创建匿名内部类时会立即创建该类的对象
 *  3）由于匿名内部类没有类名，所以无法定义构造器
 */
public class Cow {

    private double weight;
    public Cow(){}
    public Cow(double weight){
        this.weight = weight;
    }

    public class CowLeg{
        public double length;
        private String color;

        public CowLeg(){}
        public CowLeg(double length,String color){
            this.length = length;
            this.color = color;
        }

        public void info(){
            System.out.println("高:" + length + "颜色:" + color);
            System.out.println("宽:" + weight);
        }

    }

    public void test(){
        CowLeg cl = new CowLeg(1,"黑色");
        cl.info();
    }

    public static void main(String[] args) {
        Cow c = new Cow(1000);
        c.test();
    }
}
