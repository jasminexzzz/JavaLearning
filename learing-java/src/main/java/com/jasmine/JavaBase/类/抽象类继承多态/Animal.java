package com.jasmine.JavaBase.类.抽象类继承多态;

/**
 * 抽象类Animal
 */
public abstract class Animal {
    protected String nose;//鼻子
    protected String name;


    public Animal(){}
    public Animal(String AninamlName){
        this.nose = AninamlName+"的鼻子";
        this.name = AninamlName;
    }

    public abstract String walk();
    public abstract String eat();
    public abstract String sleep();

    public void getColor(){
        System.out.println("动物的颜色都是红色的");
    }
}
