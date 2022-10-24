package com.xzzz.B3_design_mode.工厂模式_Factory.简单工厂;

/**
 * @author : jasmineXz
 */
public class ZTest {
    public static void main(String[] args) {
        Car b = CarFactory.createCarFactory(CarEnum.Benz.getTypeId());
        b.build();

        //多方法静态工厂
        Car b1 = MoreMethodCarFactory.createAudi();
        b1.build();
    }
}
