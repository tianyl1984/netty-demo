package com.tianyl.netty.demo.http;

import java.util.concurrent.ExecutionException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyHttpServer {

	private NioEventLoopGroup serverBossGroup;

	private NioEventLoopGroup serverWorkerGroup;

	public NettyHttpServer() {
		serverBossGroup = new NioEventLoopGroup(1);
		serverWorkerGroup = new NioEventLoopGroup(1);
	}

	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new HttpServerCodec());
						pipeline.addLast(new HttpObjectAggregator(64 * 1024));
						pipeline.addLast(new ChunkedWriteHandler());
						pipeline.addLast(new HttpRequestHandler());
					}
				});
		try {
			bootstrap.bind(8080).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.out.println("启动失败");
		}
		System.out.println("启动成功");
	}

	public void stop() {
		serverBossGroup.shutdownGracefully();
		serverWorkerGroup.shutdownGracefully();
	}

	public static void main(String[] args) {
		new NettyHttpServer().start();
	}

}
