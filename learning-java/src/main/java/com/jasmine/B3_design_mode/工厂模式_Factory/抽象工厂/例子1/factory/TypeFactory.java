package com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.factory;

import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.car.Car;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.type.Auto;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.type.Sports;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.type.Type;

/**
 * @author : jasmineXz
 */
public class TypeFactory extends AbstractFactory {
    @Override
    public Car getCar(String car) {
        return null;
    }

    @Override
    public Type getType(String type) {
        if(type == null){
            return null;
        }

        if("auto".equals(type)){
            return new Auto();
        }else if("sports".equals(type)){
            return new Sports();
        }

        return null;
    }
}
