package com.jasmine.C1_framework.Netty.demo_final.server;

import ch.qos.logback.classic.LoggerContext;
import com.jasmine.C1_framework.Netty.demo_final.XzProtocolCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

@Slf4j
public class XzService {

    /**
     * 打印 NETTY 的日志
     */
    private static final LoggingHandler HANDLER_LOG = new LoggingHandler(LogLevel.INFO);

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("io.netty");
        logger.setLevel(ch.qos.logback.classic.Level.INFO);
    }

    /**
     * 启动异常
     */
    public static void start(int port) {
        /*
         * 创建两个线程组 boosGroup、workerGroup
         * bossGroup 用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到 workerGroup 的 Selector 中.
         * 默认线程数是CPU核数 * 2, 也可以用 new NioEventLoopGroup(1) 指定线程数
         * 两个都是无限循环
         *
         * bossGroup: 负责 accept 事件
         * workGroup: 负责 read/write 事件
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // workerGroup用于处理每一个连接发生的读写事件。
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动器, 设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置两个线程组 boosGroup 和 workerGroup
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)                  // 设置服务端通道实现类
                    .option(ChannelOption.SO_BACKLOG, 128)            // 设置线程队列等待的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)    // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 处理 worker(child) 能执行的操作

                        /**
                         * 给 pipeline 设置处理器, 在连接建立后, accept 之后
                         * @param channel socket channel
                         */
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            // 5秒没有收到 channel 数据
                            channel.pipeline().addLast(new IdleStateHandler(10,0,0));
                            // 通过心跳校验客户端
                            channel.pipeline().addLast(new AutoCloseIdleChannelHandler());
                            // 日志处理器
                            channel.pipeline().addLast(HANDLER_LOG);
                            // LTC 解码器
                            channel.pipeline().addLast(XzProtocolCodec.handlerName, XzProtocolCodec.lengthFieldBasedFrameDecoder());
                            // 自定义解码器
                            channel.pipeline().addLast(new XzProtocolCodec());
                            // 同步任务执行器
                            channel.pipeline().addLast(SyncHandler.name, new SyncHandler());
                            // 异步任务处理器
                            channel.pipeline().addLast(AsyncHandler.asyncGroup, AsyncHandler.name, new AsyncHandler());
                            // 连接异常处理器
                            channel.pipeline().addLast(new QuitHandler());
                        }
                    });

            // 绑定端口号，启动服务端
            Channel channel = bootstrap.bind(port).sync().channel();
            log.warn("》》》服务端准备就绪《《《");
            // 对通道的关闭进行监听
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            /*
             * 优雅地关闭EventLoopGroup, 释放掉所有的资源，包括创建的线程
             */
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
