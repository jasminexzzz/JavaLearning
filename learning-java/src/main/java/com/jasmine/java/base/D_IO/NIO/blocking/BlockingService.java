package com.jasmine.java.base.D_IO.NIO.blocking;

import com.jasmine.java.base.D_IO.NIO.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class BlockingService {

    public static void main(String[] args) throws IOException {
        // 使用 nio 来理解阻塞模式, 单线程
        // 0. ByteBuffer 用来存放数据
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1. 创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8081));

        // 3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();

        // 无限循环用来等待连接
        while (true) {
            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            System.out.println("\n=====================================================================\n" +
                    "等待客户端连接, 没有连接则一直等待");
            // 阻塞方法，线程停止运行，直到系统内核告知有连接发生，才返回对应通道
            SocketChannel sc = ssc.accept();
            System.out.printf("收到客户端(%s)连接\n", sc.getRemoteAddress().toString());
            channels.add(sc);

            // 遍历所有通道，有多少连接就有多少通道
            for (SocketChannel channel : channels) {
                // 5. 接收客户端发送的数据，如果没有数据则一直等待，哪怕此时有新的连接到来，新连接也无法进行
                System.out.printf("准备read通道(%s)数据并写入到 Buffer，没有数据则一直阻塞。\n", channel.getRemoteAddress().toString());
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
