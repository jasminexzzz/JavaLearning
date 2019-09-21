package com.jasmine.设计模式.工厂模式_Factory.工厂方法.例子1;

import com.jasmine.设计模式.工厂模式_Factory.工厂方法.例子1.car.Car;
import com.jasmine.设计模式.工厂模式_Factory.工厂方法.例子1.factory.AudiCarFactory;
import com.jasmine.设计模式.工厂模式_Factory.工厂方法.例子1.factory.CarFactory;

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
