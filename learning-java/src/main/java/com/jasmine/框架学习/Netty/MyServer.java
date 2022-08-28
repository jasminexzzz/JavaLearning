package com.jasmine.框架学习.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer {
    public static void main(String[] args) throws Exception {
        Logger.getLogger("io.netty").setLevel(Level.INFO);

        //创建两个线程组 boosGroup、workerGroup
        // bossGroup 用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到workerGroup的Selector中。
        // 默认线程数是CPU核数的两倍，也可以用 new NioEventLoopGroup(1) 指定线程数
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup用于处理每一个连接发生的读写事件。
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给pipeline管道设置处理器
                            socketChannel.pipeline()
                                    .addLast(new MyServerLastHandler());
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器
            System.out.println("》》》服务端准备就绪《《《");
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(6666).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            /*
             * 优雅地关闭EventLoopGroup, 释放掉所有的资源，包括创建的线程
             */
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
