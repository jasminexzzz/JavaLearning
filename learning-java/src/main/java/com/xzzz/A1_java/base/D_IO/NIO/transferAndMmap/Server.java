package com.xzzz.A1_java.base.D_IO.NIO.transferAndMmap;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8388608);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8081));

        File target = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB_COPY.MP4");
        FileChannel outChannel = new FileOutputStream(target).getChannel();

        while (true) {
            SocketChannel sc = ssc.accept();
            System.out.printf("收到客户端(%s)连接\n", sc.getRemoteAddress().toString());

            while (true) {
                int len = sc.read(buffer);
                if (len < 1) {
                    break;
                }
//                System.out.println("接收到数据" + sum);
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }

            break;
        }
    }
}
