package com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.工厂方法;

/**
 * @author : jasmineXz
 */
public class Client {
    public static void main(String[]args){
        ComputerEngineer cf = new ComputerEngineer();
        // 工厂方法例子
        cf.makeComputer(1,1);
        //在客户的非法调用中,可能会出现组装错误的情况
        cf.makeComputer(1,2);
    }
}
