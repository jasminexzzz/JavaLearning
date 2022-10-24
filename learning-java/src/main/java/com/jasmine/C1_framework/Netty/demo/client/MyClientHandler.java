package com.jasmine.C1_framework.Netty.demo.client;

import cn.hutool.core.thread.ThreadUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Thread thread = new Thread(new Task(ctx));
        thread.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(
                String.format("收到服务端 [%s] 发来的消息: [%s]",
                        ctx.channel().remoteAddress(),
                        byteBuf.toString(CharsetUtil.UTF_8))
        );
    }

    public static class Task implements Runnable {
        private ChannelHandlerContext ctx;
        private Integer num = 1;

        public Task(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                ctx.writeAndFlush(
                        Unpooled.copiedBuffer(
                                String.format("你好 SERVER, 我是 %s (%s)", ctx.channel().localAddress().toString(), num++), CharsetUtil.UTF_8)
                );
                ThreadUtil.safeSleep(1000);
            }
        }
    }
}