package com.xzzz.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例3.observer;

/**
 * 订阅者/观察者
 *
 * @author jasmineXz
 */
public interface Observer {

    void receive (String channel,String msg);
}
