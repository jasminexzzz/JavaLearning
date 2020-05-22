package com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.channel;

import com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.observer.Observer;
import io.netty.channel.AbstractChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信订阅渠道
 *
 * @author jasmineXz
 */
public class WeChat implements Channel {

    private static final List<Observer> observers = new ArrayList<>();

    @Override
    public void add(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void del(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void publish(String str) {
        observers.forEach(observer -> {
            observer.receive(this.getClass().getName(),str);
        });
    }
}
