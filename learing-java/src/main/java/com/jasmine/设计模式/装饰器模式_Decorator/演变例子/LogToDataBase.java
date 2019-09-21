package com.jasmine.设计模式.装饰器模式_Decorator.演变例子;

/**
 * @author : jasmineXz
 */
public class LogToDataBase extends LogImpl{
    @Override
    public void printLog() {
        super.printLog();
        System.out.println("保存到数据库");
    }
}
