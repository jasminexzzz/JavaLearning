package com.jasmine.设计模式.工厂模式_Factory.简单工厂;

/**
 * @author : jasmineXz
 */
public class ZTest {
    public static void main(String[] args) {
        Car b = CarFactory.createCarFactory(CarEnum.BMW.getTypeId());
        b.build();

        //多方法静态工厂
        Car b1 = MoreMethodCarFactory.createAudi();
        b1.build();
    }
}
