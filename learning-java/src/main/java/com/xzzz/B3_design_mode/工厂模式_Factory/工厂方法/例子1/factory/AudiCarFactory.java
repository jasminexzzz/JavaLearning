package com.xzzz.B3_design_mode.工厂模式_Factory.工厂方法.例子1.factory;

import com.xzzz.B3_design_mode.工厂模式_Factory.工厂方法.例子1.car.Audi;

/**
 * @author : jasmineXz
 */
public class AudiCarFactory implements CarFactory{
    @Override
    public Object createCarFactory() {
        return new Audi();
    }
}
