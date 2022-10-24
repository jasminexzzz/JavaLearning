package com.jasmine.B3_design_mode.模板模式_Template.例子1;

/**
 * @author : jasmineXz
 */
public abstract class Drink {

    public final void start(){
        boilWater();
        brew();
        pourInCup();
        if(this.hook()){
            addCoundiments();
        }
    }


    /** 烧水 */
    public void boilWater(){
        System.out.println("煮开水");
    }

    /** 泡点东西 */
    public abstract void brew();


    /** 倒进杯子 */
    public void pourInCup(){
        System.out.println("倒进杯子");
    }

    /** 加点佐料 */
    public abstract void addCoundiments();

    /** 钩子 */
    public boolean hook(){
        return false;
    }

}
