package com.jasmine.C2_middleware.rabbitmq;

/**
 * @author : jasmineXz
 */
public class 概念 {
    /**

     一. exchange 交换机
        1. 类型
         1). 直接 (direct) 1:1

         2). 广播 (fanout) 扇形路由 1:N
            可以把一个消息并行发布到多个队列上去,简单的说就是,当多个队列绑定到fanout的交换器,那么交换器一次性拷贝多个消息分别发送到绑定的队
            列上,每个队列有这个消息的副本.
            可以达到并行的目的,如维护个人信息会增加积分,可以将个人信息提及到一个队列,将积分增加到积分队列,然后并行处理.

         3). 话题 (topic) N:1
            多个交换器可以路由消息到同一个队列.根据模糊匹配,比如一个队列的routing key 为*.test ,那么凡是到达交换器的消息中的routing key
            后缀.test都被路由到这个队列上.


    二. MQ镜像部署
        1.
     */
}
