package com.wheelshop.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wheelshop.model.production.Production;
import com.wheelshop.service.production.IProductionService;
@Component
public class YieldUtils {
	 
	private static IProductionService iProductionService;
	    
    @Autowired
    public YieldUtils(IProductionService iProductionService) {
    	YieldUtils.iProductionService = iProductionService;
    }
	private static Map<String,Timer> timerMap = new ConcurrentHashMap<String,Timer>();
	public static void startSchedule(final String id,String prodnum,String interval){
		Logger logger = Logger.getLogger("WheelshopLogger");
		Timer timer = new Timer();
		
		if(timerMap.get(prodnum)!=null){
			timerMap.get(prodnum).cancel();
			timerMap.put(prodnum, timer);
		}
		else{
			timerMap.put(prodnum, timer);
		}
		 
		logger.info("计时器总数:"+timerMap.size());
		logger.info("开始计时，生产线："+prodnum+";时间间隔："+interval);
	    timer.scheduleAtFixedRate(new TimerTask() {
	      public void run() {
	    	  Production temp=iProductionService.selectProductionById(id);
	    	  Production p=new Production();
	    	  p.setId(Long.parseLong(id));
	    	  p.setYield((Integer.parseInt(temp.getYield())+1)+"");
	    	  iProductionService.updateProduction(p);
	      }
	    }, (int)(Float.parseFloat(interval)*1000),  (int)(Float.parseFloat(interval)*1000));
	}
	
	
}
