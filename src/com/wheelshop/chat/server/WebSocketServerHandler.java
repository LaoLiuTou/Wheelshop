package com.wheelshop.chat.server;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheelshop.chat.common.NettyChannelMap;

/**
 * @author LT
 * @version 1.0
 * @date 2014年2月14日
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	Logger logger = Logger.getLogger("WheelshopLogger");
    private WebSocketServerHandshaker handshaker;
   
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) throws Exception {

        // 如果HTTP解码失败，返回HHTP异常
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                    BAD_REQUEST));
            return;
        }

        String socketIp = "",socketPort="";
        try {
            Properties props = new Properties();  
       	 
            props.load(this.getClass().getClassLoader().getResourceAsStream("socket/socket.properties"));
            
            socketIp=props.getProperty("ip").trim();
            socketPort=props.getProperty("port").trim();
            
            
            
           
         } catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}  
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://"+socketIp+":"+socketPort, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext channelHandlerContext,
                                      WebSocketFrame frame) {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(channelHandlerContext.channel(),
                    (CloseWebSocketFrame) frame.retain());
            
            String userid=NettyChannelMap.getkey(channelHandlerContext);
              
            //移除
            NettyChannelMap.remove(channelHandlerContext);
            channelHandlerContext.close();
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
        	channelHandlerContext.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        try {
			String content = request;
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>(); 
			map = mapper.readValue(content, new TypeReference<Map<String, String>>(){});
			if(map.get("T").equals("1")){//login
		        //登录
				String replayContent ="";
				if(NettyChannelMap.get(map.get("UI"))!=null){
					//被踢下线
					replayContent = "{\"T\":\"6\",\"R\":\"0\"}";//被踢下线
					ChannelHandlerContext tempCtx=NettyChannelMap.get(map.get("UI"));
	                tempCtx.writeAndFlush(new TextWebSocketFrame(replayContent));
	                tempCtx.close();
					NettyChannelMap.remove(NettyChannelMap.get(map.get("UI")));
				}
		        try {
		        	NettyChannelMap.add(map.get("UI"),channelHandlerContext);
			        replayContent = "{\"T\":\"1\",\"R\":\"0\"}";//登录成功
					channelHandlerContext.writeAndFlush(new TextWebSocketFrame(replayContent));
				} catch (Exception e) {
					replayContent = "{\"T\":\"1\",\"R\":\"1\"}";//登录失败
					 channelHandlerContext.writeAndFlush(new TextWebSocketFrame(replayContent));
					e.printStackTrace();
				}
				logger.info("用户登录，ID:"+map.get("UI")+"；用户名："+map.get("UN"));
			}
			else if(map.get("T").equals("2")){//logout
				String replayContent = "{\"T\":\"2\",\"R\":\"0\"}";//退出成功
				try { 
					NettyChannelMap.remove(channelHandlerContext);
					channelHandlerContext.writeAndFlush(new TextWebSocketFrame(replayContent));
					channelHandlerContext.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					replayContent = "{\"T\":\"2\",\"R\":\"1\"}";//退出失败
					channelHandlerContext.writeAndFlush(new TextWebSocketFrame(replayContent));
					channelHandlerContext.close();
					e.printStackTrace();
				}
				logger.info("用户退出，ID:"+map.get("UI")+"；用户名："+map.get("UN"));
			}
			else if(map.get("T").equals("3")||map.get("T").equals("7")||map.get("T").equals("8")
					||map.get("T").equals("9")){//text message
				SimpleDateFormat dfs = new SimpleDateFormat("HH:mm:ss.SSS");  
				ChannelHandlerContext chc=NettyChannelMap.get(map.get("FI"));
				if(chc!=null){
					chc.writeAndFlush(new TextWebSocketFrame(content));
					logger.info("发送消息："+content);
				}
				else{
					logger.info("接收端不在线！"); 
				}
			}
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("推送异常:"+e.toString());
			e.printStackTrace();
		} 
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
        NettyChannelMap.remove(ctx);
    }
    
  
}