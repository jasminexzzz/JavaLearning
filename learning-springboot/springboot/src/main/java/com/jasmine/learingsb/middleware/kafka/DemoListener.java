//package com.jasmine.learingsb.middleware.kafka;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.Partitioner;
//import org.apache.kafka.common.Cluster;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.Optional;
//
///**
// * @author : jasmineXz
// */
//@Slf4j
//@Component
//public class DemoListener implements Partitioner {
//    private static final Logger log= LoggerFactory.getLogger(DemoListener.class);
//
//    @KafkaListener(topics = "learningTopic1",groupId = "learning-consumer-group1")
//    public void topic1Listen1(
//            ConsumerRecord<?, ?> record,
//            Acknowledgment ack,
//            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
//        if (kafkaMessage.isPresent()) {
//            Object message = kafkaMessage.get();
//            log.trace("topic1Listen1 - message : " + message);
//            ack.acknowledge();
//        }
//    }
//
////    @KafkaListener(topics = "learningTopic1",groupId = "learning-consumer-group2")
//    public void topic1Listen2(ConsumerRecord<?, ?> record,
//            Acknowledgment ack,
//          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
//        if (kafkaMessage.isPresent()) {
//            Object message = kafkaMessage.get();
//            log.trace("topic1Listen2 - message : " + message);
//            ack.acknowledge();
//        }
//    }
//
//    @Override
//    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
//        cluster.topics().forEach(item -> {
//            log.trace("topic个数" + item);
//        });
//        return 0;
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    @Override
//    public void configure(Map<String, ?> map) {
//
//    }
//
////    @KafkaListener(topics = "learningTopic2")
////    public void listen2(ConsumerRecord<?, ?> record) {
////        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
////        if (kafkaMessage.isPresent()) {
////            Object message = kafkaMessage.get();
////            System.out.println("learningTopic2 : message : " + message);
////        }
////    }
//
//}
