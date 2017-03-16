package com.tianyl.netty.demo.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloServer {

	public static void main(String[] args) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup);
			server.channel(NioServerSocketChannel.class);
			server.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					ChannelPipeline pipeline = sc.pipeline();
					pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
					pipeline.addLast("decoder", new StringDecoder());
					pipeline.addLast("encoder", new StringEncoder());
					pipeline.addLast("handler", new HelloServerHandler());
				}
			});
			ChannelFuture future = server.bind(4567).sync();
			System.out.println("start server");
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}
