package com.xzzz.C1_framework.Netty.demo3_final.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cn.hutool.core.thread.ThreadUtil;
import com.xzzz.C1_framework.Netty.demo3_final.XzCmd;
import com.xzzz.C1_framework.Netty.demo3_final.XzProtocolCodec;
import com.xzzz.C1_framework.Netty.demo3_final.XzProtocolInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Slf4j
public class XzClient {

    /**
     * 打印 NETTY 的日志
     */
    private static final LoggingHandler HANDLER_LOG = new LoggingHandler(LogLevel.INFO);

    private static int CONNECT_RETRY_SECOND = 1;

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("io.netty");
        logger.setLevel(Level.INFO);
    }

    /**
     * <h2>二、配置说明</h2>
     * <ol>
     *  <li>CONNECT_TIMEOUT_MILLIS: 客户端连接超时时间, 超过该时间未连接至服务器, 则抛出 {@link io.netty.channel.ConnectTimeoutException} 异常,
     *  该值比较大时, 可能会提前抛出 {@link java.net.ConnectException} 异常</li>
     *  <li>SO_TIMEOUT: 用于阻塞式IO的连接超时时间, 如文件读写等, 用于控制阻塞时间</li>
     *  <li>
     *  TCP_NODELAY: 默认不开启NODELAY, 即使用<a href='https://www.yuque.com/xiaozeizeizi/learning/qv3twp#NMJvX'>《Nagle 算法》</a>，
     *  会将多个小的报文包合并为一个大的发送, 如果确定客户端与服务端的通信十分频繁, 可以设置为 {@code false}, 否则建议设置为 {@code true}
     *  </li>
     *  <li>SO_SNDBUF/SO_RCVBUF: 即<a href='https://www.yuque.com/xiaozeizeizi/learning/qv3twp#VqXLB'>《滑动窗口》</a>的上限，该值建议不设置，系统会自适应动态分配</li>
     * </ol>
     */
    @SuppressWarnings("all")
    public static void start(String host, int port) {
        try {
            NioEventLoopGroup clientGroup = new NioEventLoopGroup();
            // 创建bootstrap对象，配置参数
            Bootstrap clientBootStrap = new Bootstrap();
            // 设置线程组
            clientBootStrap.group(clientGroup)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .option(ChannelOption.TCP_NODELAY, false)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {

                        /**
                         * 给 pipeline 设置处理器, 在连接建立后
                         *
                         * @param channel socket channel
                         */
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            channel.pipeline().addLast(HANDLER_LOG); // 日志
                            channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    resetRetry();
                                    log.info("连接服务器[{}:{}]成功", host, port);
                                    super.channelRegistered(ctx);
                                }

                                @Override
                                public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                                    log.error("连接服务器[{}:{}]失败, 准备{}秒后重连", host, port, CONNECT_RETRY_SECOND);
                                    clientGroup.schedule(() -> {
                                        log.warn("重连服务器[{}:{}]中...", host, port);
                                        connect(clientBootStrap, clientGroup, host, port);
                                    }, CONNECT_RETRY_SECOND, TimeUnit.SECONDS);
                                }
                            });
                            //region 5秒没有写, 则发送一个心跳包
                            channel.pipeline().addLast(new IdleStateHandler(0, 3, 0));
                            channel.pipeline().addLast(KeepaliveHandler.name, new KeepaliveHandler());
                            //endregion
                            channel.pipeline().addLast(new XzProtocolCodec()); // 编码
                        }
                    });

            // 连接服务端
            ChannelFuture clientChannel = connect(clientBootStrap, clientGroup, host, port);

            /**
             * 阻塞主线程，等待异步连接成功.
             * 主要是防止还没有连接到服务器时就开始通过 channel 发送消息等.
             * 但是阻塞会有问题, 会造成主线程停止, 如果服务器无法启动, 应用会阻塞在这里直到连接成功或失败
             */
            channel = clientChannel.channel();
            // 对通道关闭进行监听
            ChannelFuture closeFuture = channel.closeFuture().sync();
            closeFuture.addListener((ChannelFutureListener) future -> {
                log.warn("服务器连接关闭: [{}:{}]", host, port);
                // clientGroup.shutdownGracefully();
                // connect(clientBootStrap, clientGroup, host, port);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接服务器
     *
     * @param clientBootStrap
     * @param clientGroup
     * @param host
     * @param port
     * @return
     */
    private static ChannelFuture connect(Bootstrap clientBootStrap, NioEventLoopGroup clientGroup, String host, int port) {
        try {
            incrementRetry();
            return clientBootStrap
                    .connect(host, port)
                    .sync();
//                    .addListener((ChannelFutureListener) future -> {
//                        if (future.isSuccess()) {
//                            resetRetry();
//                            log.info("连接服务器[{}:{}]成功", host, port);
//                        } else {
//                            log.error("连接服务器[{}:{}]失败, 准备{}秒后重连", host, port, CONNECT_RETRY_SECOND);
//                            // 提交一个异步定时任务执行重连操作
//                            clientGroup.schedule(() -> {
//                                log.warn("重连服务器[{}:{}]中...", host, port);
//                                connect(clientBootStrap, clientGroup, host, port);
//                            }, CONNECT_RETRY_SECOND, TimeUnit.SECONDS);
//                        }
//                    })
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("链接服务器[%s.%s]异常: %s", host, port, e.getMessage()));
        }
    }

    private static synchronized void incrementRetry() {
        CONNECT_RETRY_SECOND = Math.min(30, CONNECT_RETRY_SECOND * 2);
    }

    private static synchronized void resetRetry() {
        CONNECT_RETRY_SECOND = 2;
    }


    private static Channel channel = null;

    public static void send(String msg) {
        XzProtocolInfo info = new XzProtocolInfo();
        info.setSeq(1);
        info.setCmd(XzCmd.CUSTOM.getCmd());
        info.setContent(msg);
        channel.writeAndFlush(info).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("");
            }
        });
    }


    public static void main(String[] args) throws Exception {
        start("127.0.0.1", 6666);
    }
}
