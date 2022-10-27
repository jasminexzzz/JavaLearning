package com.xzzz.C1_framework.Netty.demo3_final.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@ChannelHandler.Sharable
public class ClientMetricHandler extends ChannelInboundHandlerAdapter {

    private static final Set<Channel> alive_channels = new HashSet<>();

    public static final String name = "ClientMetricHandler";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        alive_channels.add(ctx.channel());
        log.debug("客户端 {} 链接", ctx.channel());
        log.debug("当前在线用户\n{}", alive_channels.toArray());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("客户端 {} 断开连接", ctx.channel());
        alive_channels.remove(ctx.channel());
        super.channelInactive(ctx);
    }
}
