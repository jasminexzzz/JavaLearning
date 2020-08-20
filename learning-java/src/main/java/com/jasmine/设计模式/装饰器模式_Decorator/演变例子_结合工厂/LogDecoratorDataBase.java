package com.jasmine.设计模式.装饰器模式_Decorator.演变例子_结合工厂;

/**
 * @author : jasmineXz
 */
public class LogDecoratorDataBase extends LogDecorator {
    @Override
    public void print() {
        super.print();
        System.out.println("保存到数据库");
    }
}
