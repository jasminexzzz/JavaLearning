package com.jasmine.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例1;


/**
 * @author : jasmineXz
 */
public interface Subject {

    //添加观察者
    void addObserver(Observer obj);

    //移除观察者
    void deleteObserver(Observer obj);

    //通知所有的观察者,当主题方法改变时,这个方法被调用
    void notifyObserver();
}
