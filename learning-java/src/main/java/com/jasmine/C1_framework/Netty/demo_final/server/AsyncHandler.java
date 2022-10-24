package com.jasmine.C1_framework.Netty.demo_final.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步任务处理器, 会判断是否异步执行任务
 */
@Slf4j
public class AsyncHandler extends ChannelInboundHandlerAdapter {

    public static final String name = "AsyncHandler";

    // 异步线程 group, 用于处理 pipeline 上指定 handler
    protected static final EventLoopGroup asyncGroup = new DefaultEventLoop();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("异步执行: " + msg.toString());
    }
}
