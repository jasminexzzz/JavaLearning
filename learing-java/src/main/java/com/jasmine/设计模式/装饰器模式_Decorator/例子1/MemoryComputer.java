package com.jasmine.设计模式.装饰器模式_Decorator.例子1;

/**
 * 给电脑加个内存条
 *
 * @author : jasmineXz
 */
public class MemoryComputer extends AbstractComputer{

    public MemoryComputer(Computer computer){
        super(computer);
    }

    @Override
    public void cpu() {
        System.out.println("增加了一个内存");
        computer.cpu();
    }
}
