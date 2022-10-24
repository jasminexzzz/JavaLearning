package com.xzzz.B3_design_mode.装饰器模式_Decorator.演变例子_结合工厂;

/**
 * 日志装饰器
 *
 * @author jasmineXz
 */
public abstract class LogDecorator implements Log {

    protected Log log;

    public void setLog(Log log) {
        this.log = log;
    }

    @Override
    public void print() {
        if (null != log) {
            log.print();
        }
    }
}
