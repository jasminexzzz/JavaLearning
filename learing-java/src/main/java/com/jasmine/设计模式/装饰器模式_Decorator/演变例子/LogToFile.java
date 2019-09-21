package com.jasmine.设计模式.装饰器模式_Decorator.演变例子;

import sun.rmi.runtime.Log;

/**
 * @author : jasmineXz
 */
public class LogToFile extends LogImpl {
    @Override
    public void printLog() {
        super.printLog();
        System.out.println("输出到文件");
    }
}
