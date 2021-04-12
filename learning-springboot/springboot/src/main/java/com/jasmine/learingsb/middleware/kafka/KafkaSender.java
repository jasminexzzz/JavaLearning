//package com.jasmine.learingsb.middleware.kafka;
//
//import cn.hutool.json.JSONUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
///**
// * @author : jasmineXz
// */
//@Slf4j
//@RestController
//@RequestMapping("/kafka")
//public class KafkaSender {
//
//    private String LEARNING_TOPIC_1 = "learningTopic1";
//
//    @Autowired
//    private KafkaTemplate<String,Object> kafkaTemplate;
//
//    @GetMapping("/send")
//    public void send(@RequestParam("msg")String msg) {
//        KafkaElement message = new KafkaElement();
//        message.setId(System.currentTimeMillis());
//        message.setMsg(msg);
//        message.setSendTime(LocalDateTime.now());
//
//        ListenableFuture<SendResult<String, Object>> send = kafkaTemplate.send(LEARNING_TOPIC_1, JSONUtil.toJsonStr(message));
//
//        send.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                log.error(LEARNING_TOPIC_1+"-生产者发送消息失败"+throwable.getMessage());
//            }
//            @Override
//            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
//                String dateTime = LocalDateTime.now().toString();
////                log.trace(LEARNING_TOPIC_1+" - 生产者发送消息成功 " + stringObjectSendResult.toString()+" 时间 : "+dateTime);
//            }
//        });
////        ListenableFuture<SendResult<String, String>> test2
////                = kafkaTemplate.send("learningTopic2", JSONUtil.toJsonStr(message));
//    }
//}
