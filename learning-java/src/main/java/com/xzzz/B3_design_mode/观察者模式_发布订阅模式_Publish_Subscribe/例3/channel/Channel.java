package com.xzzz.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例3.channel;

import com.xzzz.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例3.observer.Observer;

/**
 * 渠道 (主体)
 *
 * @author jasmineXz
 */
public interface Channel {

    /**
     * 新增订阅
     */
    void add(Observer observer);

    /**
     * 删除订阅
     */
    void del(Observer observer);

    /**
     * 发布通知
     */
    void publish(String str);
}
