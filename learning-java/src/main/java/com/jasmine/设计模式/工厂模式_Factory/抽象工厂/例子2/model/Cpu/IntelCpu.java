package com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.model.Cpu;

/**
 * @author : jasmineXz
 */
public class IntelCpu implements Cpu {
    /** CPU的针脚数 */
    private int pins = 0;
    public  IntelCpu(int pins){
        this.pins = pins;
    }

    @Override
    public void cheahCpu() {
        // TODO Auto-generated method stub
        System.out.println("Intel CPU的针脚数：" + pins);

    }

}
