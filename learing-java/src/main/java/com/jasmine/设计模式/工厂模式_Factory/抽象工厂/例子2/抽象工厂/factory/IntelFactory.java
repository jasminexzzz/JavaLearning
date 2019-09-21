package com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.抽象工厂.factory;

import com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.model.Cpu.Cpu;
import com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.model.Cpu.IntelCpu;
import com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.model.mainboard.IntelMainBoard;
import com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.model.mainboard.MainBoard;

/**
 * @author : jasmineXz
 */
public class IntelFactory implements AbstractFactory {

    @Override
    public Cpu createCpu() {
        // TODO Auto-generated method stub
        return new IntelCpu(755);
    }

    @Override
    public MainBoard createMainboard() {
        // TODO Auto-generated method stub
        return new IntelMainBoard(755);
    }

}
