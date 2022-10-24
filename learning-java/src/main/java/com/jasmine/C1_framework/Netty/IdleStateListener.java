package com.jasmine.C1_framework.Netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;

/**
 * 闲置连接监听
 */
public class IdleStateListener extends ChannelInboundHandlerAdapter {

    /**
     * 用户自定义事件
     * @param event
     *  - IdleStateEvent: 闲置连接状态
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {

        if (event instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) event;

            SocketAddress addr = ctx.channel().remoteAddress();

            switch (idleStateEvent.state()) {
                /*
                长时间未读
                 */
                case READER_IDLE: {
                    System.out.println(String.format("%s = %s: 长时间未进行读, 尝试确认客户端是否存在", System.currentTimeMillis(), addr));
                    // 尝试关闭长时间未读写的链接, 服务端主动关闭后, 会导致客户端出现 RejectedExecutionException
                    // ctx.channel().close();
                    break;
                }
                /*
                长时间未写
                 */
                case WRITER_IDLE: {
                    System.out.println(String.format("%s = %s: 长时间未进行写, 尝试确认客户端是否存在", System.currentTimeMillis(), addr));
                    break;
                }
                /*
                长时间未读或未写, 适用于服务端会读客户端, 且写客户端的场景
                如果客户端只读不写, 那么指定时长不写后同样会进入该事件.
                也可以使用该机制来定时向客户端写数据
                 */
                case ALL_IDLE: {
                    break;
                }
            }
        }
        // super.userEventTriggered(ctx, event);
    }

}
