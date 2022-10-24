package com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.抽象工厂;

import com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.抽象工厂.factory.AbstractFactory;
import com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.抽象工厂.factory.IntelFactory;

/**
 * @author : jasmineXz
 */
public class Client {
    public static void main(String[]args){
        //创建装机工程师对象
        ComputerEngineer cf = new ComputerEngineer();
        //客户选择并创建需要使用的产品对象
        AbstractFactory af = new IntelFactory();
        //告诉装机工程师自己选择的产品，让装机工程师组装电脑
        cf.makeComputer(af);
    }
}
