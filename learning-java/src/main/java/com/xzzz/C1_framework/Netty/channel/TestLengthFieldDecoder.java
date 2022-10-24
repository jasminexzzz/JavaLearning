package com.xzzz.C1_framework.Netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 测试 LTC 解码器
 */
public class TestLengthFieldDecoder {

    private static final int maxFrameLength      = 30;// 整体长度 30 个字节
    private static final int lengthFieldOffset   = 10;// 前 10 个字节是 header
    private static final int lengthFieldLength   = 4; // 再 4 个是内容的长度, 长度一般是 int 类型, 也就是 4 个字节
    private static final int lengthAdjustment    = 4; // 再 4 个是版本的长度, 版本一般是 int 类型, 也就是 4 个字节
    // 如果解析的内容想要包含 header, length, version, 那么就为0, 如果只想解析发送的内容, 那么就为 10+4+4=18, 即前 18 个字节忽略
    private static final int initialBytesToStrip = 18;
    private static final int realMaxContentLength = maxFrameLength - lengthFieldOffset - lengthFieldLength - lengthAdjustment;

    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                lengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip),
                new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        createContent(byteBuf, "hello, world"); // 12 字节
        createContent(byteBuf, "hi!");          // 4  字节

        channel.writeInbound(byteBuf);
    }

    /**
     * 构造请求内容
     *
     * [HEADER][LENGTH][VERSION][CONTENT]
     *  10byte  4byte   4byte    30-10-4-4=12
     *
     * @param byteBuf byteBuf
     * @param content 实际内容
     */
    private static void createContent(ByteBuf byteBuf, String content) {
        // 12 个字节
        byte[] bytes = content.getBytes();
        int length = bytes.length;

        if (length > realMaxContentLength) {
            throw new RuntimeException(String.format("最大长度为%s, 实际长度为:%s", realMaxContentLength, length));
        }

        byte[] header = "testHeader".getBytes();

        // 写入头
        byteBuf.writeBytes(header);
        // 写入长度
        byteBuf.writeInt(length);
        // 写入版本
        byteBuf.writeInt(9999999);
        // 写入内容
        byteBuf.writeBytes(bytes);
    }

    /**
     * 说明 {@link LengthFieldBasedFrameDecoder} 参数
     *
     * @param maxFrameLength      消息的最大长度, 如果长度大于这个值, 将抛出 TooLongFrameException
     * @param lengthFieldOffset   长度字段(Field)的偏移量, 说明长度字段的的位置是从哪里开始, 如果消息一开始就是长度字段, 那么就是0
     * @param lengthFieldLength   长度字段(Field)的长度, 长度一定要小于消息的最大长度, 在构造前就可以进行校验
     * @param lengthAdjustment    调整字段(Adjustment)的长度之后预留给其他内容的长度
     * @param initialBytesToStrip 整个消息中(包含长度字段 Field 长度, 调整字段 Adjustment 长度 )要剥离出来的长度, 通常在内容的头部都是一些其他要素, 例如长度/一些规范头等, 这次之后才是实际内容, 这里就是实际的内容的位置
     */
    @SuppressWarnings("all")
    private static LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder(int maxFrameLength,
                                                                             int lengthFieldOffset,
                                                                             int lengthFieldLength,
                                                                             int lengthAdjustment,
                                                                             int initialBytesToStrip) {
        return new LengthFieldBasedFrameDecoder(
                maxFrameLength,
                lengthFieldOffset,
                lengthFieldLength,
                lengthAdjustment,
                initialBytesToStrip);
    }
}
