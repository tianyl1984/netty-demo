package com.tianyl.netty.demo.hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloClient {

	public static void main(String[] args) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap boot = new Bootstrap();
		boot.group(group);
		boot.channel(NioSocketChannel.class);
		boot.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encoder", new StringEncoder());
				pipeline.addLast("handler", new HelloClientHandler());
			}
		});
		Channel channel = boot.connect("127.0.0.1", 4567).sync().channel();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String line = in.readLine();
			if (line == null) {
				break;
			}
			if (line.contains("exit")) {
				break;
			}
			channel.writeAndFlush(line + "\r\n");
		}
		group.shutdownGracefully();
	}

	public static class HelloClientHandler extends SimpleChannelInboundHandler<String> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			System.out.println("Server say : " + msg);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("Client channelActive ");
			super.channelActive(ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("Client channelInactive ");
			super.channelInactive(ctx);
		}

	}
}
