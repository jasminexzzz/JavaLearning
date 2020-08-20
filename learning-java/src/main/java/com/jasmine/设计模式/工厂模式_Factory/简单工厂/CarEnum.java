package com.jasmine.设计模式.工厂模式_Factory.简单工厂;

/**
 * @author : jasmineXz
 */
public enum CarEnum {
    BMW(1),
    Audi(2),
    Benz(3);


    private int typeId;

    CarEnum(int typeId){
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }
}
