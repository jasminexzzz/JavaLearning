//package com.jasmine.learingsb.middleware.kafka;
//
//import cn.hutool.json.JSONUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
///**
// * @author : jasmineXz
// */
//@RestController
//@RequestMapping("/kafka")
//public class KafkaSender {
//
//    @Autowired
//    private KafkaTemplate<String,String> kafkaTemplate;
//
//    @GetMapping("/send")
//    public void send() {
//        KafkaElement message = new KafkaElement();
//        message.setId(System.currentTimeMillis());
//        message.setMsg("123");
//        message.setSendTime(LocalDateTime.now());
//        ListenableFuture<SendResult<String, String>> test1
//                = kafkaTemplate.send("learningTopic1", JSONUtil.toJsonStr(message));
//        ListenableFuture<SendResult<String, String>> test2
//                = kafkaTemplate.send("learningTopic2", JSONUtil.toJsonStr(message));
//    }
//}
