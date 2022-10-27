package com.xzzz.C1_framework.Netty.demo2_rpc.server.handler;

import com.xzzz.C1_framework.Netty.demo2_rpc.message.GroupChatRequestMessage;
import com.xzzz.C1_framework.Netty.demo2_rpc.message.GroupChatResponseMessage;
import com.xzzz.C1_framework.Netty.demo2_rpc.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        List<Channel> channels = GroupSessionFactory.getGroupSession()
                .getMembersChannel(msg.getGroupName());

        for (Channel channel : channels) {
            channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
    }
}
