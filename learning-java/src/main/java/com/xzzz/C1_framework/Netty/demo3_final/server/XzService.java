package com.xzzz.C1_framework.Netty.demo3_final.server;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.xzzz.C1_framework.Netty.demo3_final.XzProtocolCodec;
import com.xzzz.C1_framework.Netty.demo3_final.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
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
        logger.setLevel(Level.INFO);
    }

    public static void main(String[] args) {
        start(6666);
    }

    /**
     * <h2>一、服务端说明</h2>
     * 创建两个线程组 [boosGroup,workerGroup] 两个都是无限循环
     * <p>bossGroup: 负责 accept 事件用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到 workerGroup 的 Selector 中. 默认线程数
     * 是CPU核数 * 2, 也可以用 new NioEventLoopGroup(1) 指定线程数,
     * <p>workGroup: 负责 read/write 事件, 处理每一个连接发生的读写事件
     * <p>
     * <h2>二、配置说明</h2>
     * <ol>
     *  <li>SO_BACKLOG: ServerSocketChannel 设置线程队列等待的连接个数, Windows 默认 200, Linux 默认 128</li>
     *  <li>
     *  ALLOCATOR: 配置 ByteBuf, 例如是否池化, 使用堆内堆外内存等. 默认是堆外池化
     *  <p>- 堆内分配快，速度慢
     *  <p>- 堆外分配慢，速度快
     *  <ul>- 非池化分配方式
     *  <li>{@link UnpooledByteBufAllocator#DEFAULT}                        : 非池化堆外</li>
     *  <li>{@link UnpooledByteBufAllocator#UnpooledByteBufAllocator(boolean)} : (true) 非池化堆外</li>
     *  <li>{@link UnpooledByteBufAllocator#UnpooledByteBufAllocator(boolean)} : (false)非池化堆内</li>
     *  </ul>
     *  <ul>- 池化分配方式
     *  <li>{@link PooledByteBufAllocator#DEFAULT}                           : 池化堆内</li>
     *  <li>{@link PooledByteBufAllocator#PooledByteBufAllocator(boolean)} : (true) 池化</li>
     *  <li>{@link PooledByteBufAllocator#PooledByteBufAllocator(boolean)} : (false)池化堆内</li>
     *  </ul>
     *  </li>
     *  <li>SO_KEEPALIVE: SocketChannel 设置保持活动连接状态</li>
     * </ol>
     */
    @SuppressWarnings("all")
    public static void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    // 设置服务端通道实现类
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 处理 worker(child) 能执行的操作

                        /**
                         * 给 pipeline 设置处理器, 在连接建立后, accept 之后
                         *
                         * @param channel socket channel
                         */
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            // 5秒没有收到 channel 数据
                            channel.pipeline().addLast(new IdleStateHandler(10, 0, 0));
                            // 客户端监控
                            channel.pipeline().addLast(ClientMetricHandler.name, new ClientMetricHandler());
                            // 通过心跳校验客户端
                            channel.pipeline().addLast(AutoCloseIdleChannelHandler.name, new AutoCloseIdleChannelHandler());
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
            log.warn("\n》》》XzServer 服务端准备就绪《《《");
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
