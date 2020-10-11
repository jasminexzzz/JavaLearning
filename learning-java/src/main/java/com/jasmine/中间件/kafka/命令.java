package com.jasmine.中间件.kafka;

/**
 * @author : jasmineXz
 */
public class 命令 {
    /**
     *

     查看topic
     ./kafka-topics.sh --list --zookeeper 192.168.1.3:2181
     ./kafka-topics.sh --list --zookeeper 172.16.150.85:2181

     查看具体的topic详细信息：
     ./kafka-topics.sh --zookeeper 172.16.150.85:2181 --topic learningTopic1 --describe

     添加分区
     ./kafka-topics.sh --zookeeper 172.16.150.85:2181 --alter --topic learningTopic1 --partitions 2

     查看分区消息数

     启动一个消费者
     bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic bjstestkafka
     ./kafka-console-consumer.sh --bootstrap-server 121.199.36.219:9092 --topic springCloudBus
     */
}
