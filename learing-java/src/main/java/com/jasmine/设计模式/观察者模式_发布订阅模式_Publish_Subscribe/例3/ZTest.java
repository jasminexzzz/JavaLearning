package com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3;

import com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.channel.Channel;
import com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.channel.WeChat;
import com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.channel.Web;
import com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.observer.LiSi;
import com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.observer.Observer;
import com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例3.observer.ZhangSan;

/**
 * @author jasmineXz
 */
public class ZTest {
    public static void main(String[] args) {
        Observer zhangsan = new ZhangSan();
        Observer liSi = new LiSi();

        Channel web = new Web();
        Channel weChat = new WeChat();

        web.add(zhangsan);
        web.add(liSi);

        weChat.add(zhangsan);

//        web.publish("今天天气好");

        weChat.publish("今天天气不好");

    }
}
