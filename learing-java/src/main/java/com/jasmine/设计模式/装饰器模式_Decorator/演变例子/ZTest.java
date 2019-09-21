package com.jasmine.设计模式.装饰器模式_Decorator.演变例子;

/**
 * @author : jasmineXz
 */
public class ZTest {

    public static void main(String[] args) {
        Log l = new LogToDataBase();
        l.printLog();
    }
}
