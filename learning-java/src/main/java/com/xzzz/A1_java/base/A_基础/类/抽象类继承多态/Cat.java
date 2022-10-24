package com.xzzz.A1_java.base.A_基础.类.抽象类继承多态;

public class Cat extends Animal{

    public Cat(){}
    public Cat(String AninamlName){
        super(AninamlName);
    }

    @Override
    public String walk() {
        return "猫轻轻的走路";
    }

    @Override
    public String eat() {
        return "猫吃东西";
    }

    @Override
    public String sleep() {
        return "猫爱睡觉";
    }

    public String scratch() {
        return "猫爱挠东西";
    }
}
