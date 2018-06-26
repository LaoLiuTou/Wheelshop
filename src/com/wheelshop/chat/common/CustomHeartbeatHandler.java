package com.wheelshop.chat.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author lt
 * @version 1.0 
 */
public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<String> {
    public static final String PING_MSG = "1";
    public static final String PONG_MSG = "2";
    public static final String CUSTOM_MSG = "3";
    protected String name;
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String string) throws Exception {
    	//System.out.println(context.name()+"|"+string);
    	if (string.equals(PING_MSG)) {
            sendPongMsg(context);
        } else if (string.equals(PONG_MSG)){
            System.out.println(name + " get pong msg from " + context.channel().remoteAddress());
        }
        else {
            handleData(context, string);
            //System.out.println(context.name()+"|");
        }
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        /*ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PING_MSG);
        buf.retain();
        context.writeAndFlush(buf);*/
        context.writeAndFlush(PING_MSG);
        heartbeatCount++;
        System.out.println(name + " sent ping msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        /*ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PONG_MSG);
        context.channel().writeAndFlush(buf);*/
        context.channel().writeAndFlush(PONG_MSG);
        heartbeatCount++;
        System.out.println(name + " sent pong msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

   //protected abstract void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);
    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, String content);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
        //System.err.println("---" + ctx.channel().remoteAddress() + " is active---");
     
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
     
        //移除
        NettyChannelMap.remove(ctx);
      
        
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }
}