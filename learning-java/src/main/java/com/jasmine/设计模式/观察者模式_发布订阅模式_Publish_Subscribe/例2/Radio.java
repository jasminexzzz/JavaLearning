package com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例2;

/**
 * @author : jasmineXz
 */
public interface Radio {

    /**
     * 订阅广播
     */
    void subscribe(Subscriber sub);

    /**
     * 取消订阅
     */
    void unsubscribe(Subscriber sub);

    /**
     * 发布广播消息
     */
    void publish(String msg);

    String getRadioName();

}
