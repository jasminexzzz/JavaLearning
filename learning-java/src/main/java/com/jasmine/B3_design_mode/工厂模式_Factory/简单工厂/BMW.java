package com.jasmine.B3_design_mode.工厂模式_Factory.简单工厂;

/**
 * @author : jasmineXz
 */
public class BMW implements Car{
    @Override
    public void build() {
        System.out.println("造了一辆:"+this.getClass().getSimpleName());
    }
}
