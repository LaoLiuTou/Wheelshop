package com.wheelshop.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.wheelshop.chat.server.WebsocketServerInitializer;
 
@Component 
public class ChatTask {
  
	@PostConstruct //初始化方法的注解方式  等同与init-method=init 
	public void init(){
		Thread thread = new Thread(new MyRunnable());
		thread.start();

	}
	public class MyRunnable implements Runnable {
	   public void run(){

			Logger logger = Logger.getLogger("WheelshopLogger");
	       
	        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        try {
	            ServerBootstrap b = new ServerBootstrap(); // (2)
	           
	           	 
	           	 b.group(bossGroup, workerGroup)
	                .channel(NioServerSocketChannel.class) // (3)
	                .childHandler(new WebsocketServerInitializer())  //(4)
	                .option(ChannelOption.SO_BACKLOG, 128)          // (5)
	                .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
	            
	            logger.info("启动ChatServer");
	            
	    		//System.out.println("ChatServer 启动了");
	    		//参数
	      	 	Properties properties = new Properties();
	   		String base = ChatTask.class.getResource("/")
	   				.getPath();
	   		try {
	   			properties.load(new FileInputStream(base
	   					+ "socket/socket.properties"));
	   		} catch (Exception e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}   
	   		
	   		int port=Integer.parseInt(properties.getProperty("port").trim());
	            // 绑定端口，开始接收进来的连接
	            ChannelFuture f = b.bind(port).sync(); // (7)

	            f.channel().closeFuture().sync();

	        }
	        catch (Exception e) {
				// TODO: handle exception
	       	 e.printStackTrace();
			 }
	        finally {
	            workerGroup.shutdownGracefully();
	            bossGroup.shutdownGracefully();
	            logger.info("关闭ChatServer");
	    		//System.out.println("ChatServer 关闭了");
	        }
	   }
	}
 
}
