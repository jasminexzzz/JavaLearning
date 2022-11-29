package com.jasmine.rocketmq;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProducerTest {
    private static final DefaultMQProducer producer;

    static {
        producer = new DefaultMQProducer(MQConstants.PRODUCER_GROUP,true);
        // 设置NameServer地址
        producer.setNamesrvAddr(MQConstants.NAMESRVADDR);
        // 启动producer
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            // 普通消息
            // send("hello normal > " + i);
//            ThreadUtil.safeSleep(1000);

            // 单向消息
            // sendOneway("hello oneway > " + i);

            // 顺序消息: 前五条发送至一个队列, 后五条发送至另一个队列
            // sendOrder("hello order > " + i, 1);

            // 延迟消息
            sendDelayTime("hello delay time > " + i);

            // 批量消息
            // batch.add("hello batch > " + i);

            // 事务消息
            // sendTransaction("hello transaction > " + i);
        }
    }

    /**
     * 同步发送
     */
    private static void send(String msg) {
        try {
            Message message = new Message(MQConstants.TOPIC, "TagA", msg.getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 利用 Producer 进行发送，并同步等待发送结果
            SendResult sendResult = producer.send(message);
            log.info("发送: {}", sendResult);
        } catch (UnsupportedEncodingException | MQClientException | InterruptedException | RemotingException | MQBrokerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步发送
     */
    private static void sendAsync(String msg) {
        try {
            Message message = new Message(MQConstants.TOPIC, "TagA", msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%s 发送成功 %s %n", msg, sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.printf("%s 发送失败 %s %n", msg, e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (UnsupportedEncodingException | InterruptedException | RemotingException | MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单向发送, 不关注是否发送成功
     */
    private static void sendOneway(String msg) {
        try {
            Message message = new Message(MQConstants.TOPIC, "TagA", msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(message);
        } catch (UnsupportedEncodingException | InterruptedException | RemotingException | MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 顺序发送
     */
    private static void sendOrder(String msg, int orderId) {
        try {
            Message message = new Message(MQConstants.TOPIC, "TagA", msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
                    Integer orderId = (Integer) arg;
                    int queueIndex = orderId % list.size();
                    log.info("队列个数:{}, 本次发送到队列:{} ", list.size(), queueIndex);
                    return list.get(queueIndex);
                }
            }, orderId);
        } catch (UnsupportedEncodingException | InterruptedException | RemotingException | MQClientException | MQBrokerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 延迟消息
     */
    private static void sendDelayTime(String msg) {
        System.out.println("发送延迟消息");
        try {
            Message message = new Message(MQConstants.TOPIC, "TagA", msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            // level_3: 10秒
            message.setDelayTimeLevel(5);

            // 利用 Producer 进行发送，并同步等待发送结果
            SendResult sendResult = producer.send(message);
            log.info("{}}", sendResult);
        } catch (UnsupportedEncodingException | MQClientException | InterruptedException | RemotingException | MQBrokerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量发送
     */
    private static void sendBatch(List<String> batch) {
        System.out.println("========== 批量发送 ==========");
        List<Message> messages = new ArrayList<>();
        for (String msg : batch) {
            try {
                Message message = new Message(MQConstants.TOPIC, "TagA", msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
                messages.add(message);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        try {
            SendResult sendResult = producer.send(messages);
            System.out.println(sendResult);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
        }

    }


    // ========== 事务消息

    // 事务消息的 Producer 需要单独创建
    private static final TransactionMQProducer transactionProducer;

    static {
        transactionProducer = new TransactionMQProducer(MQConstants.PRODUCER_GROUP);
        transactionProducer.setNamesrvAddr(MQConstants.NAMESRVADDR);
        transactionProducer.setTransactionListener(new TransactionListenerImpl());
        try {
            transactionProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送事务
     */
    private static void sendTransaction(String msg) {
        try {
            String keys = String.valueOf(System.currentTimeMillis());
            Message message = new Message(MQConstants.TOPIC, "TagA", msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            message.setKeys(keys);
            // 发送事务消息
            SendResult sendResult = transactionProducer.sendMessageInTransaction(message, null);
            log.info("发送事务消息:{}", keys);
            // 执行本地事务...

        } catch (UnsupportedEncodingException | MQClientException e) {
            e.printStackTrace();
        }
    }

    static class TransactionListenerImpl implements TransactionListener {
        /**
         * 当发送事务准备(半)消息成功时,将调用此方法来执行本地事务。
         * <p>该方法和发送消息是同一线程, 当调用 {@link TransactionMQProducer#sendMessageInTransaction(Message, Object)}方法后
         * 就会调用该方法.
         * <p>如果发送消息在事务内, 调用此方法应该永远返回 {@link LocalTransactionState#UNKNOW}, 将消息的事务交给
         * {@link TransactionListener#checkLocalTransaction(MessageExt)}方法校验并提交, 否则可能本地事务回滚而消息事务提交.
         */
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            // 决定是否提交消息事务
            log.warn("executeLocalTransaction: 消息[{}]的决定是否提交事务, 事务ID:{}", msg.getKeys(), msg.getTransactionId());
            return LocalTransactionState.UNKNOW;
        }

        /**
         * 检查本条消息的事务结果，是否可以提交事务
         * <p>本地事务状态类型：
         * <br/>1.UNKNOW           : 中间状态，它代表需要检查消息队列来确定状态。
         * <br/>2.COMMIT_MESSAGE   : 提交事务，消费者可以消费此消息
         * <br/>3.ROLLBACK_MESSAGE : 回滚事务，它代表该消息将被删除，不允许被消费。
         *
         * @param msg 消息
         * @return 本地事务状态
         */
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = 0;//RandomUtil.randomInt(0, 2);
            log.warn("检查本地事务: [{}]消息的事务检查结果:{}", msg.getKeys(), status);
            switch (status) {
                case 0:
                    return LocalTransactionState.UNKNOW;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                default:
                    return LocalTransactionState.COMMIT_MESSAGE;
            }
        }
    }
}
