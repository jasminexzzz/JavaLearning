package com.jasmine.learingsb.config.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author : jasmineXz
 */
@Component
public class MyLisenterByAnno {

    /**
     * 同步调用
     * @param event
     */
    @EventListener
    public void listener1(MyEvent event) {
        System.out.println("注解事件监听 -> 1："+ event.getMsg());
    }

    /**
     * 异步调用
     * @param event
     */
    @Async
    @EventListener
    public void listener2(MyEvent event) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("注解事件监听 -> 2："+ event.getMsg());
    }
}
