package com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.factory;

import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.car.Car;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.type.Type;

/**
 * @author : jasmineXz
 */
public abstract class AbstractFactory {
    public abstract Car getCar(String car);
    public abstract Type getType(String type);
}
