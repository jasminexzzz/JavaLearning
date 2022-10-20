package com.jasmine.java.base.D_IO.NIO.nonblocking;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

@Slf4j
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel sc = SocketChannel.open();

        sc.connect(new InetSocketAddress("localhost", 8081));
        System.out.println("客户端已连接...");

        System.out.println("等待发送消息...");
        // 连发两条消息，可能粘包，服务端需要处理，客户端需要给与拆包标识，即在每次发送内容以换行符结尾来标识
        sc.write(Charset.defaultCharset().encode("hello\n"));
        // 休眠3秒
        Thread.sleep(3000);

        // 由于服务的 read 方法也会阻塞，所以有当第一个客户端没有发送内容时，第二个客户端连接后也无法触发后续动作，因为服务端线程阻塞了
//        Thread.sleep(3000 * 60 * 60);
        sc.write(Charset.defaultCharset().encode("good by\n"));
        System.out.println("消息发送完成");

        // 客户端通道关闭
        sc.close();
    }

}
