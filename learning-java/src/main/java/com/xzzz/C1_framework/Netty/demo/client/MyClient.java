package com.xzzz.C1_framework.Netty.demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;


public class MyClient {

    public static void main(String[] args) throws Exception {


        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        // 创建bootstrap对象，配置参数
        Bootstrap clientBootStrap = new Bootstrap();
        // 设置线程组
        clientBootStrap.group(clientGroup)
                //设置客户端的通道实现类型
                .channel(NioSocketChannel.class)
                //使用匿名内部类初始化通道
                .handler(new ChannelInitializer<SocketChannel>() {

                    /**
                     * 给 pipeline 设置处理器, 在连接建立后
                     * @param channel socket channel
                     */
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        // 将字符串转为 ByteBuf
                        channel.pipeline().addLast(new StringEncoder());
                        //添加客户端通道的处理器
//                        channel.pipeline().addLast(new MyClientHandler());
                    }
                });

        // 连接服务端
        ChannelFuture clientChannel = clientBootStrap
                .connect("127.0.0.1", 6666)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        System.out.println("连接成功!");
                        Thread.sleep(1000);
                        future.channel().close();
                    } else {
                        System.err.println("连接失败!");
                    }
                });

        /*
        阻塞主线程，等待异步连接成功.
        主要是防止还没有连接到服务器时就开始通过 channel 发送消息等.
        但是阻塞会有问题, 会造成主线程停止, 如果服务器无法启动, 应用会阻塞在这里直到连接成功或失败
         */
        Channel channel = clientChannel.sync().channel();


        // 对通道关闭进行监听
        ChannelFuture closeFuture = clientChannel.channel().closeFuture();
        closeFuture.addListener((ChannelFutureListener) future -> {
            System.out.println("连接关闭！");
            clientGroup.shutdownGracefully();
        });
    }
}
