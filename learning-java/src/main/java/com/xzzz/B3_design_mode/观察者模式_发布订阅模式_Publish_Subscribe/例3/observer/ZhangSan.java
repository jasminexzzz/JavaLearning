package com.xzzz.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例3.observer;

/**
 * @author jasmineXz
 */
public class ZhangSan implements Observer{

    @Override
    public void receive(String channel,String msg) {
        System.out.println(String.format("张三收到%s发送的消息 : %s",channel,msg));
    }
}
