package com.jasmine.A1_java.base.D_IO.NIO.nonblocking;

import com.jasmine.A1_java.base.D_IO.NIO.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NonBlockingService {

    public static void main(String[] args) throws IOException {
        // 使用 nio 来理解阻塞模式, 单线程
        // 0. ByteBuffer 用来存放数据
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1. 创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.configureBlocking(false);// 设置为非阻塞

        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8081));

        // 3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();

        // 无限循环用来等待连接
        while (true) {
            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            // 非阻塞方法，拿到拿不到都会返回，拿不到就返回 null
            SocketChannel sc = ssc.accept();

            if (sc != null) {
                // channel 也要设置成非阻塞
                sc.configureBlocking(false);
                System.out.printf("收到客户端(%s)连接\n", sc.getRemoteAddress().toString());
                channels.add(sc);
            }


            // 遍历所有通道，有多少连接就有多少通道
            for (SocketChannel channel : channels) {
                // 5. 接收客户端发送的数据，如果没有数据则一直等待，哪怕此时有新的连接到来，新连接也无法进行

                // 大于0有数据，等于0没有从 channel 中读到数据
                while (channel.read(buffer) > 0) {
                    System.out.printf("读取到通道(%s)发生的数据\n", channel.getRemoteAddress().toString());
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                    System.out.printf("从通道(%s)读取数据完成!\n", channel.getRemoteAddress().toString());
                }
            }
        }
    }
}
