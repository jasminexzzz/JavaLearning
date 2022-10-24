package com.jasmine.B3_design_mode.模板模式_Template.例子1;

/**
 * @author : jasmineXz
 */
public class DrinkCoffee extends Drink{
    @Override
    public void brew() {
        System.out.println("冲点咖啡");
    }

    @Override
    public void addCoundiments() {
        System.out.println("加糖加奶");
    }

    public boolean hook(){
        System.out.println("---------------------");
        return true;
    }
}
