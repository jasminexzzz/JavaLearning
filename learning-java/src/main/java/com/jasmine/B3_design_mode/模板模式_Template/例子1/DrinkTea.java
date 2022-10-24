package com.jasmine.B3_design_mode.模板模式_Template.例子1;

/**
 * @author : jasmineXz
 */
public class DrinkTea extends Drink{
    @Override
    public void brew() {
        System.out.println("冲点茶");
    }

    @Override
    public void addCoundiments() {
    }

    /**
     * 其实可以不写
     * @return
     */
    public boolean hook(){
        return false;
    }
}
