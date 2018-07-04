package com.wheelshop.utils;

 
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheelshop.chat.common.NettyChannelMap;
import com.wheelshop.model.timer.Timer;
import com.wheelshop.service.timer.ITimerService;
/**
 * @author lt
 * @version 1.0 
 */
@Component 
public class TokenUtils {
    public static Map<String,TokenBean> map=new ConcurrentHashMap<String, TokenBean>();
    public static void add(String token,TokenBean tokenBean){
    	//非单点登录不需要清除相同用户id的token
        map.put(token,tokenBean);
    }
    public static TokenBean get(String token){
       return map.get(token);
    }
 
    public static void remove(String token){
    	 map.remove(token);
    }
    
    
    @SuppressWarnings("rawtypes")
	public static void main(String[] s){
    	System.out.println(get("b6f71e0e-6634-4529-aee5-324cecdb1fbe"));
    	for (Map.Entry entry:map.entrySet()){
            
            System.out.println(entry.getValue()+":"+entry.getKey());
        }
    }

    
    public class TokenBean { 
    	private String timesamp;
    	private String username;
    	private String userid;
		public String getTimesamp() {
			return timesamp;
		}
		public void setTimesamp(String timesamp) {
			this.timesamp = timesamp;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
    	
       
    } 
    
    private static long removeTime = 60*60*1000;//失效时间
    Logger logger = Logger.getLogger("WheelshopLogger");
    @SuppressWarnings("rawtypes")
	//@Scheduled(cron="0 0 0/1 * * ? ")  
    public void cleanToken(){  
    	try {
			String message ="清理过期token,起始token数量："+map.size();
			for (Map.Entry entry:map.entrySet()){
				TokenBean tb=(TokenBean) entry.getValue();
				String tokenTime = tb.getTimesamp();
				long timesampLong=Long.parseLong(tokenTime);
				long tokenTimeNow=System.currentTimeMillis();
				System.out.println(tokenTimeNow-timesampLong);
				if((tokenTimeNow-timesampLong)>removeTime){
					map.remove(entry.getKey());
				}
			    
			}
			message+="，清理后token数量："+map.size();
			logger.info(message);
		} catch (Exception e) {
			logger.info("清理过期token出错！");
			e.printStackTrace();
		}
    	 
    } 

    @Autowired
	private ITimerService iTimerService;
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Scheduled(cron="0 0/1 * * * ? ")  
    public void getRest(){  
    	try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			
			Map paramMap=new HashMap();
			paramMap.put("starttime",sdf.format(new Date()));
			paramMap.put("fromPage",0);
			paramMap.put("toPage",1); 
			List<Timer> list=iTimerService.selectTimerByParam(paramMap);
			logger.info("查询间休午休开始时间:"+list.size());
			if(list.size()>0){
				//推送
				for (Map.Entry entry:NettyChannelMap.map.entrySet()){
		            if (entry.getKey().toString().substring(0, 1).equals(list.get(0).getProdnum())){
		            	
		            	String[] starttimes=list.get(0).getStarttime().split(":");
						String[] endtimes=list.get(0).getEndtime().split(":");
						 //t.getEndtime()-t.getStarttime();
						int rest=(Integer.parseInt(endtimes[0])*60*60+Integer.parseInt(endtimes[1])*60)-
								(Integer.parseInt(starttimes[0])*60*60+Integer.parseInt(starttimes[1])*60);
						
						ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) entry.getValue();
		            	Map<String, String> contentMap = new HashMap<String, String>();
		            	contentMap.put("T", "6");
		            	contentMap.put("NAME", "system");
		            	contentMap.put("FI", entry.getKey().toString());  
		            	contentMap.put("PRO", list.get(0).getProdnum());
		            	contentMap.put("TYPE", list.get(0).getType());
		            	contentMap.put("TIMES", rest+"");
						ObjectMapper mapper = new ObjectMapper();
						String json = "";
						json = mapper.writeValueAsString(contentMap);
						
						if(channelHandlerContext!=null){
							
						   channelHandlerContext.writeAndFlush(new TextWebSocketFrame(json));
				        }
		            }
		        }
			}
			
			
			paramMap=new HashMap();
			paramMap.put("endtime",sdf.format(new Date()));
			paramMap.put("fromPage",0);
			paramMap.put("toPage",1); 
			list=iTimerService.selectTimerByParam(paramMap);
			logger.info("查询间休午休结束时间:"+list.size()); 
			if(list.size()>0){
				//推送
				for (Map.Entry entry:NettyChannelMap.map.entrySet()){
		            if (entry.getKey().toString().substring(0, 1).equals(list.get(0).getProdnum())){
		            	 
						ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) entry.getValue();
		            	Map<String, String> contentMap = new HashMap<String, String>();
		            	contentMap.put("T", "7");
		            	contentMap.put("NAME", "system");
		            	contentMap.put("FI", entry.getKey().toString());  
		            	contentMap.put("PRO", list.get(0).getProdnum());
		            	contentMap.put("TYPE", list.get(0).getType());
						ObjectMapper mapper = new ObjectMapper();
						String json = "";
						json = mapper.writeValueAsString(contentMap);
						
						if(channelHandlerContext!=null){
							
						   channelHandlerContext.writeAndFlush(new TextWebSocketFrame(json));
				        }
		            }
		        }
			}
			
			
		} catch (Exception e) {
			logger.info("查询间休午休时间出错！");
			e.printStackTrace();
		}
    	 
    } 
}
