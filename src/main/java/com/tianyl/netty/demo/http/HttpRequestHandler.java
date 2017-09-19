package com.tianyl.netty.demo.http;

import java.util.Set;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		// 请求
		HttpMethod method = request.method();
		String uri = request.uri();
		System.out.println("request:" + uri);
		System.out.println("method:" + method.name());
		System.out.println("-----------header start-----------");
		HttpHeaders httpheaders = request.headers();
		Set<String> headers = httpheaders.names();
		for (String header : headers) {
			System.out.println(header + ":" + httpheaders.get(header));
		}
		System.out.println("-----------header end-------------");
		int bodyLen = request.content().readableBytes();
		if (bodyLen == 0) {
			System.out.println("no body");
		} else {
			byte[] buf = new byte[request.content().readableBytes()];
			request.content().readBytes(buf);
			String body = new String(buf);
			System.out.println(body);
		}
		// 响应
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.wrappedBuffer("ok".getBytes()));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		response.headers().set(HttpHeaderNames.SERVER, "NettyServer");
		ChannelFuture future = ctx.writeAndFlush(response);
		if (!HttpUtil.isKeepAlive(request)) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

}
