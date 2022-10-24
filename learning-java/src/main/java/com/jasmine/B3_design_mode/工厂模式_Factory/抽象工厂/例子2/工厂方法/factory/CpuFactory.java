package com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.工厂方法.factory;

import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.model.Cpu.AmdCpu;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.model.Cpu.Cpu;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.model.Cpu.IntelCpu;

/**
 * @author : jasmineXz
 */
public class CpuFactory {
    public static Cpu createCpu(int type){
        Cpu cpu = null;
        if(type == 1){
            cpu = new IntelCpu(755);
        }else if(type == 2){
            cpu = new AmdCpu(938);
        }
        return cpu;
    }
}
