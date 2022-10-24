package com.jasmine.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例1;

/**
 * 观察者模式
 * @author : jasmineXz
 */
public interface Observer {

    //当主题状态改变时,更新通知
    void update(int version);
}
