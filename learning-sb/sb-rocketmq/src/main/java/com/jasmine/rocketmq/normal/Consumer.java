/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jasmine.rocketmq.normal;

import com.jasmine.rocketmq.MQConstants;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

import java.nio.charset.StandardCharsets;

/**
 * This example shows how to subscribe and consume messages using providing {@link DefaultMQPushConsumer}.
 */
public class Consumer {
    private static final DefaultMQPushConsumer consumer;

    static {
        consumer = new DefaultMQPushConsumer(MQConstants.CONSUMER_GROUP);
        consumer.setNamesrvAddr(MQConstants.NAMESRVADDR);
        /*
         * 指定从哪里开始，以防特定的消费群体是一个全新的。
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    }

    public static void main(String[] args) throws MQClientException {

        consumer.subscribe(MQConstants.TOPIC, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            System.out.printf("%s 接收到消息: %s %n", Thread.currentThread().getName(), new String(msg.get(0).getBody(), StandardCharsets.UTF_8));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        System.out.println("消费放已启动");
    }
}
