package com.jasmine.框架学习.Netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 * 才能被Netty框架所关联，有点类似SpringMVC的适配器模式
 **/
public class MyServerLastHandler extends ChannelInboundHandlerAdapter {

    /**
     * 注册事件
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        System.out.println("客户端连接: 1. channelRegistered");
    }

    /**
     * 建立连接事件
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("客户端连接: 2. channelActive");
    }

    /**
     * 用户自定义事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        System.out.println("客户端关闭: 1. userEventTriggered");
    }

    /**
     * 连接关闭事件
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("客户端关闭: 2. channelInactive");
    }

    /**
     * 注销事件
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("客户端关闭: 3. channelUnregistered");
    }

    /**
     * Channel 可写状态变化事件
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
        System.out.println("channelWritabilityChanged");
    }

    /**
     * 读取事件
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送过来的消息
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(String.format("收到客户端: [%s] 发来的消息: [%s], 准备回复", ctx.channel().remoteAddress(), byteBuf.toString(CharsetUtil.UTF_8)));
    }

    /**
     * 读取完成事件
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //发送消息给客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好, 我收到了你的消息", CharsetUtil.UTF_8));
    }

    /**
     * 异常通知事件
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常，关闭通道
        ctx.close();
    }
}
