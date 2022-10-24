package com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.factory;

import com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.car.Benz;
import com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.car.Car;
import com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.type.Type;

/**
 * @author : jasmineXz
 */
public class CarFactory extends AbstractFactory {
    @Override
    public Car getCar(String car) {
        if(car == null){
            return null;
        }

        if("benz".equals(car)){
            return new Benz();
        }else if("audi".equals(car)){
            return new Benz();
        }

        return null;
    }

    @Override
    public Type getType(String type) {
        return null;
    }
}
