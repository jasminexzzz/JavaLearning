package com.xzzz.B3_design_mode.装饰器模式_Decorator.例子1;

/**
 * 创建一个电脑
 * @author : jasmineXz
 */
public abstract class AbstractComputer implements Computer{

    protected Computer computer;

    public AbstractComputer(Computer computer){
        this.computer = computer;
    }
}
