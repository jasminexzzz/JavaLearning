package com.jasmine.设计模式.观察者模式_发布订阅模式_Publish_Subscribe.例子1;

/**
 * @author : jasmineXz
 */
public class ZTest {

    public static void main(String[] args) {
        //创建主题(被观察者)
        SubjectMagazine magazine = new SubjectMagazine();
        //创建三个不同的观察者
        ObserverCustomer a = new ObserverCustomer("A");
        ObserverCustomer b = new ObserverCustomer("B");
        ObserverCustomer c = new ObserverCustomer("C");
        //将观察者注册到主题中
        magazine.addObserver(a);
//        magazine.addObserver(b);
//        magazine.addObserver(c);

        //更新主题的数据，当数据更新后，会自动通知所有已注册的观察者
        magazine.publish();
    }
}
