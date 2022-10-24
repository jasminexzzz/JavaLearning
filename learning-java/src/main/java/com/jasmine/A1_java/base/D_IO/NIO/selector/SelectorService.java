package com.jasmine.A1_java.base.D_IO.NIO.selector;

import com.jasmine.A1_java.base.D_IO.NIO.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class SelectorService {

    public static void main(String[] args) {
        try {
            // 1. 创建 selector, 管理多个 channel
            Selector selector = Selector.open();

            // 非阻塞服务监听
            ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(8081));
            ssc.configureBlocking(false);

            /*
             * 2. channel 注册到对应的 Selector, selector 不能认为是替代 channel 来接收, 而是监控 channel 发生的事件
             *
             * SelectionKey: 可以知道事件, 以及相关的 channel
             *
             * @param sel
             * @ops 关注的事件 {@link SelectionKey}
             * @att 附件, 与 channel 相关联
             *
             * 事件类型：
             *  - accept: 有连接请求时触发
             *  - connect: 客户端建立连接后触发
             *  - read: 可读事件
             *  - write: 可写事件
             *
             * 对于 ServerSocketChannel 的 key, 通常只关注 accept/connect 事件
             */
            SelectionKey serverChannelKey = ssc.register(selector, SelectionKey.OP_ACCEPT, null);
            while (true) {
                // 3. select 没有事件发生, 线程阻塞, 有事件, 线程恢复运行, 事件不处理, 就一直触发
                selector.select();
                // 4. 处理事件, 包含了所有发生的事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    // 事件的 channel key, 与注册的是同一个key
                    SelectionKey key = iterator.next();
                    // 处理完事件时, 需要将事件从事件列表中删除, 防止重复处理
                    iterator.remove();
                    eventDispatch(selector, key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 事件分发
     */
    private static void eventDispatch(Selector selector, SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            accept(selector, selectionKey);
        } else if (selectionKey.isReadable()) {
            readable(selectionKey);
        } else {
            // 如果不想接收, 需要取消事件, 如果不接收, 则系统内核会一直通知你处理, 即水平驱动
            selectionKey.cancel();
        }
    }

    /**
     * 处理 accept 事件
     * <p>将请求的客户端注册到 selector, 用于统一监听
     *
     * @param selector     selector 选择器
     * @param selectionKey 产生对应事件的 channel
     */
    private static void accept(Selector selector, SelectionKey selectionKey) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = channel.accept();
        socketChannel.configureBlocking(false);
        // 作为该 channel 的附件绑定
        ByteBuffer buffer = ByteBuffer.allocate(5);
        // socket channel 也交由 selector 管理, 只关注读事件
        socketChannel.register(selector, SelectionKey.OP_READ, buffer);
        System.out.println(String.format("客户端[%s]连接", socketChannel.getRemoteAddress().toString()));
    }

    /**
     * 如果是读事件, 多种情况会产生读事件
     * <p>1. 客户端发来消息, 通过 {@link SocketChannel#read(ByteBuffer)} != -1 来判断
     * <p>2. 客户端正常断开, 通过 {@link SocketChannel#read(ByteBuffer)} == -1 来判断
     * <p>3. 客户端异常断开, 通过捕获 IOException 来处理
     *
     * @param selectionKey 产生对应事件的 channel
     */
    private static void readable(SelectionKey selectionKey) {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            // 获取 selectionKey 上关联的附件
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            int read = socketChannel.read(buffer);
            // 如果是 -1, 说明是客户端正常断开触发的 read 事件。
            if (read == -1) {
                System.out.println("客户端断开连接...");
                selectionKey.cancel();
            } else {
                split(buffer);
                // 如果读完后, position = limit, 说明没有读取到分隔符, compact 后buffer仍然是满的, 这时就需要扩容
                if (buffer.position() == buffer.limit()) {
                    ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                    buffer.flip();
                    newBuffer.put(buffer);
                    selectionKey.attach(newBuffer);
                }
            }
        } catch (IOException e) {
            System.out.println("客户端连接异常:" + e.getMessage());
            // 客户端异常断开时也会触发一次 read 事件, 本次 read 是无法读取的, 所以需要取消该事件
            selectionKey.cancel();
        }
    }

    /**
     * 根据 \n 对 buffer 的内容进行拆分
     *
     * @param source 原数据
     */
    private static void split(ByteBuffer source) {
        source.flip();

        /**
         * 挨个判断 source 内的字符
         * limit 是实际的最大指针位置. {@link ByteBuffer#get(int)} 不会移动 position 指针, 是为了后续通过
         * {@link ByteBuffer#get(int)} 读取, 如果本次 buffer 中的数据不包含换行符, 说明读取的是不完整的, 那么即使 compact 后 position
         * 会与 limit 相同(因为 position 会变成最大写入位置, 因为buffer已满, 所以写入位置就是limit写入限制)
         */
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();

                // 完整数据读取到 target, 用于输出
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从source 读，向 target 写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }

                ByteBufferUtil.debugAll(target);
            }
        }
        // 整理未读数据
        source.compact();
    }

}
