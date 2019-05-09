package com.wheelshop.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wheelshop.model.production.Production;
import com.wheelshop.service.production.IProductionService;
import com.wheelshop.service.timer.ITimerService;
@Component
public class YieldUtils {
	 
	private static IProductionService iProductionService;
	private static ITimerService iTimerService;
    @Autowired
    public YieldUtils(IProductionService iProductionService,ITimerService iTimerService) {
    	YieldUtils.iProductionService = iProductionService;
    	YieldUtils.iTimerService = iTimerService;
    }
	private static Map<String,Timer> timerMap = new HashMap<String,Timer>();
	public static void startSchedule(final String id,final String prodnum,String interval){
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
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		public void run() {
	    	  
	    	  try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					
				  Map paramMap=new HashMap();
				  paramMap.put("starttime",sdf.format(new Date()));
				  paramMap.put("prodnum",prodnum); 
				  int count=iTimerService.selectCountTimerByParam(paramMap);
				  paramMap.put("fromPage",0);
				  paramMap.put("toPage",count); 
				  List<com.wheelshop.model.timer.Timer> list=iTimerService.selectTimerByParam(paramMap);
				  Date currentDate=new Date();
				  boolean flag = true;
				  for(com.wheelshop.model.timer.Timer item:list){
					  String starttimes=sdf2.format(currentDate)+" "+item.getStarttime();
					  String endtimes  =sdf2.format(currentDate)+" "+item.getEndtime();
					  if(currentDate.after(sdf.parse(starttimes))
							  &&currentDate.before(sdf.parse(endtimes))){
						  flag=false;
					  }
				  }
				  if(flag){
					  Production temp=iProductionService.selectProductionById(id);
					  Production p=new Production();
					  p.setId(Long.parseLong(id));
					  p.setYield((Integer.parseInt(temp.getYield())+1)+"");
					  iProductionService.updateProduction(p);
				  }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	    }, (int)(Float.parseFloat(interval)*1000),  (int)(Float.parseFloat(interval)*1000));
	}
	
	
}
