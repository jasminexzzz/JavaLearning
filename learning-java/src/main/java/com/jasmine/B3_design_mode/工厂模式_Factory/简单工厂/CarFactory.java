package com.jasmine.B3_design_mode.工厂模式_Factory.简单工厂;

/**
 * @author : jasmineXz
 */
public class CarFactory {

    /**
     * 构造汽车工厂类
     * @param carType
     * @return
     */
    public static Car createCarFactory(int carType){
        if(carType == 1){
            return new BMW();
        }else if(carType == 2){
            return new Audi();
        }else if(carType == 3){
            return new Benz();
        }else{
            throw new IllegalArgumentException("参数错误");
        }
    }
}
