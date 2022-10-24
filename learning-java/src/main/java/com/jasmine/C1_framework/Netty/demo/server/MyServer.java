package com.jasmine.C1_framework.Netty.demo.server;

import ch.qos.logback.classic.LoggerContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.LoggerFactory;



public class MyServer {


    public static void main(String[] args) throws Exception {

        /*
         * 创建两个线程组 boosGroup、workerGroup
         * bossGroup 用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到 workerGroup 的 Selector 中。
         * 默认线程数是CPU核数 * 2，也可以用 new NioEventLoopGroup(1) 指定线程数
         * 两个都是无限循环
         *
         * bossGroup: 负责 accept 事件
         * workGroup: 负责 read/write 事件
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup用于处理每一个连接发生的读写事件。
        EventLoopGroup workGroup = new NioEventLoopGroup();
        // 异步线程 group, 用于处理 pipeline 上指定 handler
        EventLoopGroup asyncGroup = new DefaultEventLoop();

        try {
            // 创建服务端的启动器，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置两个线程组 boosGroup 和 workerGroup
            bootstrap.group(bossGroup, workGroup)
                    // Netty 用什么级别打印日志
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 设置服务端通道实现类
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列等待的连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 处理 worker(child) 能执行的操作
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 给 pipeline 设置处理器, 在连接建立后, accept 之后
                         * @param channel socket channel
                         */
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 将 ByteBuf 转换为字符串
                            channel.pipeline().addLast(new StringDecoder());

                            channel.pipeline().addLast("work", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    // 如果包含某个任务, 交由某个线程执行
                                    if (msg.toString().contains("(2)")) {
                                        ctx.fireChannelRead(msg);
                                    } else {
                                        System.out.println("work:" + msg);
                                    }
                                }
                            });

                            channel.pipeline().addLast(asyncGroup,"async", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("async:" + msg);
                                }
                            });

                            //给pipeline管道设置处理器
//                            channel.pipeline().addLast(new MyServerLastHandler());
//
//                            /*
//                             * IdleStateHandler 是 netty 提供的处理空闲状态的处理器
//                             * @param readerIdleTime 多长时间没有读, 就发送一个心跳包检测是否连接, 0 为禁用
//                             * @param writerIdleTime 多长时间没有写, 就发送一个心跳包检测是否连接, 0 为禁用
//                             * @param allIdleTime 多长时间没有读写, 就发送一个心跳包检测是否连接, 0 为禁用
//                             */
//                            channel.pipeline()
//                                    .addLast("IdleStateListener", new IdleStateHandler(10, 10, 0, TimeUnit.SECONDS))
//                                    .addLast(new IdleStateListener());
                        }
                    });// 给workerGroup的EventLoop对应的管道设置处理器
            // 绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(6666).sync();
            System.out.println("》》》服务端准备就绪《《《");
            // 对通道的关闭进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            /*
             * 优雅地关闭EventLoopGroup, 释放掉所有的资源，包括创建的线程
             */
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("io.netty");
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
    }
}
