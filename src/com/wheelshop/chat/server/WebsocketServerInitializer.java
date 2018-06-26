package com.wheelshop.chat.server;



import org.apache.log4j.Logger;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 服务端 ChannelInitializer
 */
public class WebsocketServerInitializer extends
		ChannelInitializer<SocketChannel> {

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
		Logger logger = Logger.getLogger("NettyChatLogger");
		 ChannelPipeline pipeline = ch.pipeline();
        /*pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", new ServerHandler());
        pipeline.addLast(new IdleStateHandler(10, 0, 0));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 0)); */
        
        pipeline.addLast("http-codec",new HttpServerCodec());
        pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
        pipeline.addLast("handler",new WebSocketServerHandler());
 
        logger.info("客户端:"+ch.remoteAddress() +"已连接");
    }
}
