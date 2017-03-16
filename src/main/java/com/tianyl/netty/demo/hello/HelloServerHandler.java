package com.tianyl.netty.demo.hello;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel channel = ctx.channel();
		System.out.println(channel.remoteAddress() + " say: " + msg);
		ctx.writeAndFlush("Received your message ! " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " \n");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
		ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
		super.channelActive(ctx);
	}

}
