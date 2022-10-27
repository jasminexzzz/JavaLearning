package com.xzzz.C1_framework.Netty.demo2_rpc.server.handler;

import com.xzzz.C1_framework.Netty.demo2_rpc.message.GroupMembersRequestMessage;
import com.xzzz.C1_framework.Netty.demo2_rpc.message.GroupMembersResponseMessage;
import com.xzzz.C1_framework.Netty.demo2_rpc.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

@ChannelHandler.Sharable
public class GroupMembersRequestMessageHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        Set<String> members = GroupSessionFactory.getGroupSession()
                .getMembers(msg.getGroupName());
        ctx.writeAndFlush(new GroupMembersResponseMessage(members));
    }
}
