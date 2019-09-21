package com.jasmine.JavaBase.A_基础.类.This关键字;

public class Dog {

    private String color = "黑色";

    public void jump(){
        System.out.println("跳一下");
    }



    public void run(){
        System.out.println("正在执行run方法");
        /**
         * 这里并不这样写，这意味着dog对象需要调用另一个Dog对象的jump方法，这是不科学的方法
         *  Dog d = new Dog();
         *  d.jump();
         */
        /**
         * 直接写成jump即可，但其实this是存在的，与this.jump是相同的
         */
        jump();

    }

    /**
     * this其实是指类中的变量，this总是指向调用该方法的对象，也就是调用changecolor方法的对象所属的color
     * @param color
     */
    public void changeColor(String color){
        System.out.println("参数color:"+color);
        System.out.println("全局变量this.color:"+this.color);
        this.color = color;
        System.out.println(this.color);
    }

}
