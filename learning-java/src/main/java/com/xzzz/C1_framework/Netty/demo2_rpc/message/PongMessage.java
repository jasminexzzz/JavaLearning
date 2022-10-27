package com.xzzz.C1_framework.Netty.demo2_rpc.message;

public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
