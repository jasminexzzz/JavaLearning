//package com.jasmine.learingsb.kafka;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author : jasmineXz
// */
//@Component
//public class DemoListener {
//    private static final Logger log= LoggerFactory.getLogger(DemoListener.class);
//
//    //声明consumerID为demo，监听topicName为topic.quick.demo的Topic
//    @KafkaListener(id = "bjsdemo", topics = "bjstestkafka")
//    public void listen(String msgData) {
//        System.out.println("DemoListener接收到了消息 : " + msgData);
//    }
//
//}
