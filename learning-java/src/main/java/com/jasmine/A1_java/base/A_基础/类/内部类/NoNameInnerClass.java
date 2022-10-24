package com.jasmine.A1_java.base.A_基础.类.内部类;

/**
 * java8改进的匿名内部类
 *  1）必须继承一个父类或实现一个接口，但最多只能继承一个或实现一个
 *  2）不能是抽象类，因为创建匿名内部类时会立即创建该类的对象
 *  3）由于匿名内部类没有类名，所以无法定义构造器
 *  4）当创建匿名内部类时，必须实现接口或抽象父类里的所有抽象方法，也可以重写父类方法
 *
 *  5）
 *     被局部内部类，匿名内部类访问的局部变量必须使用final修饰，但java8开始取消了，如果访问局部变量，该变量不需要使用final修饰
 *     但系统会自动将局部变量加final修饰，这个功能成为effectively final，
 *     意识是对于被匿名内部类访问的局部变量，可以用也可以不用final修饰，但必须按照有修饰来用，也就是最后一次赋值后，以后不能重新赋值
 */
public class NoNameInnerClass {

    public void test(NoNameInterFace n){
        System.out.println(n.getName());
    }

    public static void main(String[] args) {
        NoNameInnerClass a = new NoNameInnerClass();
        a.test(new NoNameInterFace()
               {
                   @Override
                   public String getName() {
                       return "我是NoNameInterFace的方法实现";
                   }
               }
        );
    }
}
