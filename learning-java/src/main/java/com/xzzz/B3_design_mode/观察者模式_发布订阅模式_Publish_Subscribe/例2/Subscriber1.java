package com.xzzz.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例2;

/**
 * @author : jasmineXz
 */
public class Subscriber1 implements Subscriber {

    private String name;

    public Subscriber1(String name) {
        this.name = name;
    }

    @Override
    public void receive(Radio radio,String msg) {
        System.out.println(name + "听到" + radio.getRadioName() + "说 :" + msg);
    }
}
