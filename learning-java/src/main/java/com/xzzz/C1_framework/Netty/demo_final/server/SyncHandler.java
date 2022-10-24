package com.xzzz.C1_framework.Netty.demo_final.server;

import com.xzzz.C1_framework.Netty.demo_final.XzCmd;
import com.xzzz.C1_framework.Netty.demo_final.XzProtocolInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 同步任务执行器
 */
@Slf4j
public class SyncHandler extends ChannelInboundHandlerAdapter {

    public static final String name = "SyncHandler";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        XzProtocolInfo info = (XzProtocolInfo) msg;

        // 5001 ~ 10000 为 1 ~ 5000 的异步执行方法
        if (XzCmd.isAsync(info.getCmd())) {
            ctx.fireChannelRead(msg);
        } else {
            log.debug("同步执行: " + msg.toString());
        }
    }
}
