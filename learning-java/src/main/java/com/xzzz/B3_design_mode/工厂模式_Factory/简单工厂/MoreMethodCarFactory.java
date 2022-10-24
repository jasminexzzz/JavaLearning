package com.xzzz.B3_design_mode.工厂模式_Factory.简单工厂;

/**
 * @author : jasmineXz
 */
public class MoreMethodCarFactory {
    public static Car createBenz(){
        return new Benz();
    }

    public static Car createBMW(){
        return new BMW();
    }

    public static Car createAudi(){
        return new Audi();
    }
}
