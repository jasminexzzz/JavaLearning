package com.jasmine.框架学习.Netty;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyClient {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(new MyClientHandler());
                        }
                    });


            //连接服务端
            ChannelFuture channelFuture = connect(bootstrap, "127.0.0.1", 6666, MAX_RETRY);

                    // 添加监听
            channelFuture = channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                System.out.println("链接成功");
                            } else {
                                System.out.println("链接失败");
                            }
                        }
                    })
                    .sync();
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();

        } finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }

    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) {
        return bootstrap
            .connect(host, port)
            .addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("连接成功!");
                } else if (retry == 0) {
                    System.err.println("重试次数已用完，放弃连接！");
                } else {
                    // 第几次重连
                    int order = (MAX_RETRY - retry) + 1;
                    // 本次重连的间隔，1<<order相当于1乘以2的order次方
                    int delay = 1 << order;
                    System.out.println(DateUtil.now() + ": 连接失败，第" + order + "次重连……");
                    bootstrap.config()
                            .group()
                            .schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
                }
            });
    }
}
