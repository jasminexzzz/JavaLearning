package com.jasmine.C1_framework.Netty.demo_final.client;

import ch.qos.logback.classic.LoggerContext;
import cn.hutool.core.thread.ThreadUtil;
import com.jasmine.C1_framework.Netty.demo_final.XzCmd;
import com.jasmine.C1_framework.Netty.demo_final.XzProtocolCodec;
import com.jasmine.C1_framework.Netty.demo_final.XzProtocolInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.LoggerFactory;


public class XzClient {

    /**
     * 打印 NETTY 的日志
     */
    private static final LoggingHandler HANDLER_LOG = new LoggingHandler(LogLevel.DEBUG);

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("io.netty");
        logger.setLevel(ch.qos.logback.classic.Level.DEBUG);
    }

    private static void start(String host, int port) throws InterruptedException {
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
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(HANDLER_LOG);

                        //region 5秒没有写, 则发送一个心跳包
                        channel.pipeline().addLast(new IdleStateHandler(0, 3, 0));
                        channel.pipeline().addLast(KeepaliveHandler.name, new KeepaliveHandler());
                        //endregion

                        channel.pipeline().addLast(new XzProtocolCodec());
                    }
                });

        // 连接服务端
        ChannelFuture clientChannel = clientBootStrap
                .connect(host, port)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        System.out.println("连接成功!");

                        for (int i = 0; i < 15; i++) {
                            XzProtocolInfo info = new XzProtocolInfo();
                            info.setSeq(1);
                            info.setCmd(XzCmd.CUSTOM.getCmd());
                            info.setContent("message" + i);
                            future.channel().writeAndFlush(info);
                            ThreadUtil.safeSleep(1000);
                        }
                    } else {
                        System.err.println("连接失败!");
                    }
                });

        /*
        阻塞主线程，等待异步连接成功.
        主要是防止还没有连接到服务器时就开始通过 channel 发送消息等.
        但是阻塞会有问题, 会造成主线程停止, 如果服务器无法启动, 应用会阻塞在这里直到连接成功或失败
         */
        final Channel channel = clientChannel.sync().channel();
        // 对通道关闭进行监听
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener((ChannelFutureListener) future -> {
            System.out.println("连接关闭！");
            clientGroup.shutdownGracefully();
        });
    }

    public static void main(String[] args) throws Exception {
        start("127.0.0.1", 6666);
    }
}
