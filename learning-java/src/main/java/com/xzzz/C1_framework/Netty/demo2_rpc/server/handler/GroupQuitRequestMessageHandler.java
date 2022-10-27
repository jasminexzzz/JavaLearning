package com.xzzz.C1_framework.Netty.demo2_rpc.server.handler;

import com.xzzz.C1_framework.Netty.demo2_rpc.message.GroupJoinResponseMessage;
import com.xzzz.C1_framework.Netty.demo2_rpc.message.GroupQuitRequestMessage;
import com.xzzz.C1_framework.Netty.demo2_rpc.server.session.Group;
import com.xzzz.C1_framework.Netty.demo2_rpc.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().removeMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, "已退出群" + msg.getGroupName()));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, msg.getGroupName() + "群不存在"));
        }
    }
}
