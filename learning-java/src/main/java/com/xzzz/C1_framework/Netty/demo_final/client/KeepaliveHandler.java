package com.xzzz.C1_framework.Netty.demo_final.client;

import com.xzzz.C1_framework.Netty.demo_final.XzCmd;
import com.xzzz.C1_framework.Netty.demo_final.XzProtocolInfo;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeepaliveHandler extends ChannelDuplexHandler {

    public static final String name = "KeepaliveHandler";

    /**
     * 用来触发特殊事件
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception e
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.WRITER_IDLE) {
            log.warn("客户端长时间未发送消息至服务端, 将发送心跳保活");
            XzProtocolInfo info = new XzProtocolInfo(XzCmd.PING);
            info.setContent("PING");
            ctx.channel().writeAndFlush(info);
        }
    }
}
