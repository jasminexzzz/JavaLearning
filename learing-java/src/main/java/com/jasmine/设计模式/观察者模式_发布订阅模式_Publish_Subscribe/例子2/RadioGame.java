package com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例子2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : jasmineXz
 */
public class RadioGame implements Radio{
    private String radioName;
    /**
     * 订阅者
     */
    private List<Subscriber> subscriberList = new ArrayList<>();

    public RadioGame (String name) {
        this.radioName = name;
    }

    public RadioGame (Subscriber[] subscriber,String name) {
        subscriberList.addAll(Arrays.asList(subscriber));
        this.radioName = name;
    }

    @Override
    public void subscribe(Subscriber sub) {
        subscriberList.add(sub);
    }

    @Override
    public void unsubscribe(Subscriber sub) {

    }

    @Override
    public void publish(String msg) {
        subscriberList.forEach(item -> item.receive(this,msg ));
    }

    @Override
    public String getRadioName() {
        return this.radioName;
    }

}
