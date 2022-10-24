package com.xzzz.B3_design_mode.工厂模式_Factory.工厂方法.例子1;

import com.xzzz.B3_design_mode.工厂模式_Factory.工厂方法.例子1.car.Car;
import com.xzzz.B3_design_mode.工厂模式_Factory.工厂方法.例子1.factory.AudiCarFactory;
import com.xzzz.B3_design_mode.工厂模式_Factory.工厂方法.例子1.factory.CarFactory;

/**
 * @author : jasmineXz
 */
public class ZTest {
    public static void main(String[] args) {
        CarFactory cf = new AudiCarFactory();
        Car c = (Car) cf.createCarFactory();
        c.build();
    }
}
