package com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例2;

/**
 * @author : jasmineXz
 */
public class ZTest {

    public static void main(String[] args) throws InterruptedException {
        // R星广播电台
        RadioGame radioGame = new RadioGame("RockStar");

        // 订阅者
        Subscriber jasmine = new Subscriber1("jasmine ");
        Subscriber xzzz = new Subscriber1("xzzz ");

        // 添加订阅
        radioGame.subscribe(jasmine);
        radioGame.subscribe(xzzz);

        Thread.sleep(3000);
        // 发布消息
        radioGame.publish("我们发售了GTA5.");
    }
}
