package com.xzzz.C1_framework.Netty.demo3_final;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <h1>自定义协议</h1>
 * <h2>一. 协议规范</h2>
 * <ol>
 * <li>协议长度应该为 2 的整数倍</li>
 * </ol><br/><br/>
 * <h2>二. 协议格式</h2>
 * <pre>{@code
 * ┌───────┬───────────┬─────────┬────────────┬───────┬───────┬────────┬─────────┐
 * │sort   │ 1         │ 2       │ 3          │ 4     │ 5     │ 6      │ 7       │
 * ├───────┼───────────┼─────────┼────────────┼───────┼───────┼────────┼─────────┤
 * │field  │ MAGIC_NUM │ VERSION │ SERIALIZER │ CMD   │ SEQ   │ LENGTH │ CONTENT │
 * ├───────┼───────────┼─────────┼────────────┼───────┼───────┼────────┼─────────┤
 * │byte   │ 2         │ 2       │ 2          │ 2     │ 4     │ 4      │ 0 ~ max │
 * ├───────┼───────────┼─────────┼────────────┼───────┼───────┼────────┼─────────┤
 * │type   │ short     │ short   │ short      │ short │ float │ int    │ bytes   │
 * └───────┴───────────┴─────────┴────────────┴───────┴───────┴────────┴─────────┘
 * }</pre>
 * <h2>三. 协议字段</h2>
 * <ol>
 * <li>MAGIC_NUM : 魔数, 占用2字节, {@link Short} 类型, 取值范围在[-32768 ~ 32767].</li>
 * <li>VERSION   : 版本号, 占用2字节, {@link Short} 类型, 固定为1</li>
 * <li>SERIALIZER: 序列化方式, 占用2字节, {@link Short} 类型, 固定为1<br>1: JSON</li>
 * <li>CMD       : 执行的类型, 占用2字节, {@link Short} 类型, 取值范围在[-32768 ~ 32767]</li>
 * <li>SEQ       : 序号, 占用4字节, {@link Integer} 类型, 取值范围在[0~{@link Integer#MAX_VALUE}]</li>
 * <li>LENGTH    : 内容的长度, 占用4字节, {@link Integer} 类型, 取值范围在[0~({@link XzProtocolCodec#maxFrameLength}-16)]</li>
 * <li>CONTENT   : 内容, {@link Byte} 数组, 取值范围在[0~({@link XzProtocolCodec#maxFrameLength}-16)]</li>
 * </ol>
 */
@Slf4j
public class XzProtocolCodec extends ByteToMessageCodec<XzProtocolInfo> {

    /**
     * 报文整体长度
     */
    private static final int maxFrameLength = 256;
    /**
     * 从开始到 length 字段的位置
     */
    private static final int lengthFieldOffset = 12;
    /**
     * length 字段的长度(字节), 通常都是 int 类型, 占用4字节
     */
    private static final int lengthFieldLength = 4;
    /**
     * length 字段开始位置向后偏移到内容的长度
     */
    private static final int lengthAdjustment = 0;
    /**
     * 实际读取报文内容的文位置, 即 content 的位置
     */
    private static final int initialBytesToStrip = 0;
    /**
     * 实际的最大报文长度, 需要减去报文头的长度
     */
    private static final int readContentLength = maxFrameLength - 16;

    /**
     * 编码
     *
     * @param ctx 上下文
     * @param msg 消息对象
     * @param out byteBuf
     * @throws Exception 异常
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, XzProtocolInfo msg, ByteBuf out) throws Exception {
        System.out.println("编码: " + msg.toString());
        byte[] bytes = msg.getContent().getBytes();
        if (bytes.length > readContentLength) {
            throw new RuntimeException(String.format("消息内容超长, 最大长度:%s, 当前长度: %s", readContentLength, bytes.length));
        }
        out.writeShort(10000);        // 1 > 2字节: 魔数
        out.writeShort(1);            // 2 > 2字节: 版本
        out.writeShort(1);            // 3 > 2字节: 序列化方式
        out.writeShort(msg.getCmd()); // 4 > 2字节: 命令
        out.writeInt(msg.getSeq());   // 5 > 4字节: 序号
        out.writeInt(bytes.length);   // 6 > 4字节: 内容长度
        out.writeBytes(bytes);        // 7 > 内容
    }

    /**
     * 解码
     *
     * @param ctx 上下文
     * @param in  ByteBuf
     * @param out 解码对象集合
     * @throws Exception 异常
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        short magicNum      = in.readShort(); // 1 > 2字节: 魔数
        short version       = in.readShort(); // 2 > 2字节: 版本
        short serializer    = in.readShort(); // 3 > 2字节: 序列化方式
        short cmd           = in.readShort(); // 4 > 2字节: 命令
        int seq             = in.readInt();   // 5 > 4字节: 序号
        int length          = in.readInt();   // 6 > 4字节: 内容长度
        byte[] contentBytes = new byte[length];
        in.readBytes(contentBytes, 0, length);// 7 > 内容写入到 bytes 数组

        XzProtocolInfo info = new XzProtocolInfo();
        info.setCmd(cmd);

        // 根据序列化方式处理
        if (1 == serializer) {
            info.setContent(SerializerUtil.deserializationContent(contentBytes));
        }
        out.add(info);
    }


    public static String handlerName = "XzLengthFieldBasedFrameDecoder";

    /**
     * 不能在多线程中使用同一个 {@link LengthFieldBasedFrameDecoder}
     *
     * @return
     */
    public static LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder() {
        return new LengthFieldBasedFrameDecoder(
                maxFrameLength,
                lengthFieldOffset,
                lengthFieldLength,
                lengthAdjustment,
                initialBytesToStrip);
    }
}
