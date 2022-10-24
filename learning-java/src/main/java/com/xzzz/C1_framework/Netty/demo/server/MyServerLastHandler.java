package com.xzzz.C1_framework.Netty.demo.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 * 才能被Netty框架所关联，有点类似SpringMVC的适配器模式
 **/
public class MyServerLastHandler extends ChannelInboundHandlerAdapter {

    // region 客户端连接

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

    // region

    // region 客户端关闭

    /**
     * 用户自定义事件
     * @param event
     *  - IdleStateEvent: 闲置连接状态
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        // super.userEventTriggered(ctx, event);
        System.out.println("发生用户事件: " + event.getClass().getSimpleName());
//         System.out.println("客户端关闭: 1. userEventTriggered");
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

    // endregion

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
     * 读取客户端发送的消息
     * @param ctx 上下文信息
     *            ctx.channel() 获取通道
     *            ctx.pipeline() 获取管道
     *
     */
    @Override
    @SuppressWarnings("all")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 1. 接收方法1, 通过 Netty 自带的解码器 {@link java.lang.StringCoding.StringEncoder} 来完成解码, 会将 ByteBuf 转换
         * 为 String, 此时直接输出即可
         */
        System.out.println(String.format("收到客户端: [%s] 发来的消息: [%s], 准备回复", ctx.channel().remoteAddress(), msg.toString()));

        // 同步处理业务
        // 获取客户端发送过来的消息
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println(String.format("收到客户端: [%s] 发来的消息: [%s], 准备回复", ctx.channel().remoteAddress(), byteBuf.toString(CharsetUtil.UTF_8)));

        // 异步处理任务
//        ctx.channel().eventLoop().execute(() -> {
//            ByteBuf byteBuf = (ByteBuf) msg;
//            System.out.println(String.format("收到客户端: [%s] 发来的消息: [%s], 准备回复", ctx.channel().remoteAddress(), byteBuf.toString(CharsetUtil.UTF_8)));
//        });
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
