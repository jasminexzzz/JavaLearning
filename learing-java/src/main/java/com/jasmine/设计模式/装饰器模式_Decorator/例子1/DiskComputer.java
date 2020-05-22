package com.jasmine.设计模式.装饰器模式_Decorator.例子1;

/**
 * 给电脑价格硬盘
 *
 * @author : jasmineXz
 */
public class DiskComputer extends AbstractComputer{

    public DiskComputer(Computer computer){
        super(computer);
    }

    @Override
    public void cpu() {
        System.out.println("增加了一个硬盘");
        computer.cpu();
    }
}
