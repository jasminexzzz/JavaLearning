package com.xzzz.A1_java.base.D_IO.NIO.transferAndMmap;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

@SuppressWarnings("all")
public class Client {

    public static void main(String[] args) throws IOException {
        mmap();
    }

    /**
     * <p>169MB:  用时(ms)2213
     * <p> ============================
     * <p>1590MB / 1024  / 1024 : 用时(ms)24528
     * <p>1590MB / 10240 / 1024 : 用时(ms)5587
     * <p>1590MB / 20480 / 1024 : 用时(ms)5219
     * <p>1590MB / 20480 / 10240: 用时(ms)2358
     * <p>1590MB / 8388608 / 8388608: 用时(ms)1976
     */
    private static void write() throws IOException {
        File source = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB.MP4");
        FileChannel inChannel = new FileInputStream(source).getChannel();

        // 链接socket服务
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8081));
        System.out.println(String.format("文件总长度: [%s]", inChannel.size()));

        long batchLength = 8388608;
        long alreadyLength = 0;
        long start = System.currentTimeMillis();

        ByteBuffer buffer = ByteBuffer.allocate((int) batchLength);

        while (true) {
            long sendLength = inChannel.read(buffer);
            if (sendLength < 1) {
                break;
            }

//            System.out.println(String.format("本次发送长度: [%s]", sendLength));

            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }

        System.out.println("用时(ms)" + (System.currentTimeMillis() - start));

        socketChannel.close();
    }

    /**
     * <p>169MB          : 用时(ms)4808
     * <p> ============================
     * <p>1590MB / 1024  / 1024 : 用时(ms)43114
     * <p>1590MB / 10240 / 1024 : 用时(ms)5585
     * <p>1590MB / 20480 / 1024 : 用时(ms)5463
     * <p>1590MB / 20480 / 10240: 用时(ms)3092
     * <p>1590MB / 8388608 / 8388608: 用时(ms)1343
     */
    private static void transferTo() throws IOException {
        File source = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB.MP4");
        FileChannel inChannel = new FileInputStream(source).getChannel();

        // 链接socket服务
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8081));

        System.out.println(String.format("文件总长度: [%s]", inChannel.size()));

        long batchLength = 8388608;
        long alreadyLength = 0;

        long start = System.currentTimeMillis();

        while (alreadyLength < inChannel.size()) {
            long sendLength = Math.min(batchLength, inChannel.size() - alreadyLength);

            // windows 环境最多只可传输 8MB数据, 如果不分段传输会丢失数据
            inChannel.transferTo(alreadyLength, sendLength, socketChannel);
            alreadyLength += sendLength;
//            System.out.println(String.format("本次发送长度: [%s]", sendLength));
        }

        System.out.println("用时(ms)" + (System.currentTimeMillis() - start));

        socketChannel.close();
    }


    /**
     *
     * @throws IOException
     */
    private static void mmap() throws IOException {
        File source = new File("D:\\WangYunFei\\TestFile\\VIDEO-1590MB.MP4");
        // rwd: 读写, 且对文件内容的更新都同步写入基础存储设备
        RandomAccessFile accessFile = new RandomAccessFile(source, "rwd");
        FileChannel channel = accessFile.getChannel();

        channel.force(true);

        // 使用 mmap 将文件映射到内核空间
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, accessFile.length());

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8081));
        long start = System.currentTimeMillis();

        while (true) {
            long sendLength = channel.read(mappedByteBuffer);
            if (sendLength < 1) {
                break;
            }
            mappedByteBuffer.flip();
            // 将数据从 buffer 写入到 channel 中
            socketChannel.write(mappedByteBuffer);
        }

        System.out.println("用时(ms)" + (System.currentTimeMillis() - start));
        socketChannel.close();
    }
}
