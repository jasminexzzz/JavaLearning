package com.xzzz.C1_framework.Netty.demo3_final;

import lombok.Data;

@Data
public class XzProtocolInfo {

    public XzProtocolInfo() {
    }

    public XzProtocolInfo(XzCmd cmd) {
        this.setCmd(cmd.getCmd());
    }

    /**
     * seq
     */
    private int seq;

    /**
     * 执行命令类型
     */
    private short cmd;

    /**
     * 消息内容
     */
    private String content;

}
