package com.jasmine.设计模式.装饰器模式_Decorator.演变例子;

/**
 * @author : jasmineXz
 */
public class LogToAll extends LogImpl{
    @Override
    public void printLog() {
        System.out.println("打印到控制台");
        System.out.println("输出到文件");
        System.out.println("保存到数据库");
    }
}
