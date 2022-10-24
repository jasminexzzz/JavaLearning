package com.xzzz.sbkafka.kafka;

import java.time.LocalDateTime;

/**
 * @author : jasmineXz
 */
public class KafkaElement {

    private Long id;

    private String msg;

    private LocalDateTime sendTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
