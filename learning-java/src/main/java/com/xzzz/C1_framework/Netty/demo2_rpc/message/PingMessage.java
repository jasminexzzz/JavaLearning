package com.xzzz.C1_framework.Netty.demo2_rpc.message;

public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
