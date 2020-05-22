package com.jasmine.设计模式.装饰器模式_Decorator.演变例子_结合工厂;

/**
 * @author : jasmineXz
 */
public class LogConsole implements Log {

    @Override
    public void print() {
        System.out.println("打印到控制台");
    }
}
