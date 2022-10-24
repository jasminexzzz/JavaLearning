package com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子1.factory;

/**
 * @author : jasmineXz
 */
public class FactoryProducer {

    public static AbstractFactory getFactory(String factory){
        if(factory.equalsIgnoreCase("car")){
            return new CarFactory();
        } else if(factory.equalsIgnoreCase("type")){
            return new TypeFactory();
        }
        return null;
    }
}
