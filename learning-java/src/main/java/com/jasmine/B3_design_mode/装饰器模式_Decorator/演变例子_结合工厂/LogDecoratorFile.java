package com.jasmine.B3_design_mode.装饰器模式_Decorator.演变例子_结合工厂;

/**
 * @author : jasmineXz
 */
public class LogDecoratorFile extends LogDecorator {
    @Override
    public void print() {
        super.print();
        System.out.println("输出到文件");
    }
}
