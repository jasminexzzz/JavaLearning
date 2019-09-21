package com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.抽象工厂.factory;

import com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.model.Cpu.Cpu;
import com.jasmine.设计模式.工厂模式_Factory.抽象工厂.例子2.model.mainboard.MainBoard;

/**
 * @author : jasmineXz
 */
public interface AbstractFactory {
    /**
     * 创建CPU对象
     * @return CPU对象
     */
    Cpu createCpu();
    /**
     * 创建主板对象
     * @return 主板对象
     */
    MainBoard createMainboard();
}
