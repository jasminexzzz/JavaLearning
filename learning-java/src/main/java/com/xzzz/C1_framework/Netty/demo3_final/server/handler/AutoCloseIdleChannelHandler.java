package com.xzzz.C1_framework.Netty.demo3_final.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 自动关闭长时间未向服务器发送消息的客户端连接
 * <p>该配置受 {@link Channel#pipeline()} 添加的上一个{@link io.netty.handler.timeout.IdleStateHandler}
 * 中配置的 {@link IdleState#READER_IDLE} 值所控制
 */
@Slf4j
public class AutoCloseIdleChannelHandler extends ChannelDuplexHandler {

    public static final String name = "AutoCloseIdleChannelHandler";

    /**
     * 用来触发特殊事件
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception e
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.warn("{} 已经长时间未发送消息到服务器, 服务器将主动断开连接", ctx.channel());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
