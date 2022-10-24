package com.jasmine.B3_design_mode.装饰器模式_Decorator.例子1;

/**
 * 有个CPU
 *
 * @author : jasmineXz
 */
public class NormalComputer implements Computer{
    @Override
    public void cpu() {
        System.out.println("有一个CPU");
    }
}
