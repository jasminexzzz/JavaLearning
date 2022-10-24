package com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.工厂方法.factory;

import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.model.mainboard.AmdMainBoard;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.model.mainboard.IntelMainBoard;
import com.jasmine.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.model.mainboard.MainBoard;

/**
 * @author : jasmineXz
 */
public class MainboardFactory {
    public static MainBoard createMainboard(int type){
        MainBoard mainboard = null;
        if(type == 1){
            mainboard = new IntelMainBoard(755);
        }else if(type == 2){
            mainboard = new AmdMainBoard(938);
        }
        return mainboard;
    }
}
