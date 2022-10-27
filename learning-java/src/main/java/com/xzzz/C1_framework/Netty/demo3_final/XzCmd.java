package com.xzzz.C1_framework.Netty.demo3_final;

import lombok.Getter;

@Getter
public enum XzCmd {

    PING       (1, "心跳包客户端发送 ping", false),
    PONG       (2, "心跳包服务端响应 pong", false),
    CUSTOM     (1000, "自定义消息体消息", false),

    ASYNC_PING   (5001, "心跳包客户端发送 ping", true),
    ASYNC_CUSTOM (6000, "自定义消息体消息", true),

    ;

    private final short cmd;
    private final String desc;
    private final boolean async;

    XzCmd(int cmd, String desc, boolean async) {
        this.cmd = (short) cmd;
        this.desc = desc;
        this.async = async;
    }

    /**
     * 判断方法是否为异步方法
     * @param cmd cmd
     * @return 是否异步
     */
    public static boolean isAsync(int cmd) {
        for (XzCmd value : XzCmd.values()) {
            if (value.getCmd() == cmd) {
                return value.async;
            }
        }
        return false;
    }
}
