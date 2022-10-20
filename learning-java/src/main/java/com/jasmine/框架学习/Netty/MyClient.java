package com.jasmine.框架学习.Netty;

import cn.hutool.core.date.DateUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.TimeUnit;

public class MyClient {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap clientBootStrap = new Bootstrap();
            //设置线程组
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
                            channel.pipeline().addLast(new MyClientHandler());
                        }
                    });

            //连接服务端
            ChannelFuture clientChannel = connect(clientBootStrap, "127.0.0.1", 6666, MAX_RETRY);
            /*
            阻塞主线程，等待异步连接成功.
            主要是防止还没有连接到服务器时就开始通过 channel 发送消息等.
            但是阻塞会有问题, 会造成主线程停止, 如果服务器无法启动, 应用会阻塞在这里直到连接成功或失败
             */
            clientChannel.sync();
            //对通道关闭进行监听
            clientChannel.channel().closeFuture().sync();

        } finally {
            //关闭线程组
            clientGroup.shutdownGracefully();
        }
    }

    /**
     * @param bootstrap 启动器
     * @param host 地址
     * @param port 端口
     * @param retry 重试次数
     * @return
     * @throws InterruptedException
     */
    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) throws InterruptedException {
        ChannelFuture channelFuture =
                // 异步非阻塞, 调用了之后不关心是否连接, 调用之后可以继续向下运行. 真正执行连接的是
                bootstrap.connect(host, port)
                        // 异步处理连接结果
                        .addListener(new ChannelFutureListener() {
                            /**
                             *
                             * @param future 与调用的 channel 是相同的
                             */
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if (future.isSuccess()) {
                                    System.out.println("连接成功!");
                                } else if (retry == 0) {
                                    System.err.println("重试次数已用完，放弃连接！");
                                } else {
                                    // 第几次重连
                                    int order = (MAX_RETRY - retry) + 1;
                                    // 本次重连的间隔，1 << order 相当于 1乘以2的order次方
                                    int delay = 1 << order;
                                    System.out.println(DateUtil.now() + ": 连接失败，第" + order + "次重连!");
                                    bootstrap.config()
                                            .group()
                                            .schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
                                }
                            }
                        });

        return channelFuture;
    }
}
