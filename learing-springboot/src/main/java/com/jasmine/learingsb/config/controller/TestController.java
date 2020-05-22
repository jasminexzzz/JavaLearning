package com.jasmine.learingsb.config.controller;

import com.jasmine.learingsb.config.listener.MyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : jasmineXz
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     *  上下文对象
     */
    @Resource
    private ApplicationContext applicationContext;

    @GetMapping("/hi")
    public String hi(@RequestParam("name") String name){
        return "你好 : " + name;
    }

    @GetMapping("/listener/publish")
    public void listenerPublish(@RequestParam("msg") String msg){
        //通过上下文对象发布监听
        applicationContext.publishEvent(new MyEvent(this,msg));
        System.out.println("controller =====> " + msg);
    }

}
