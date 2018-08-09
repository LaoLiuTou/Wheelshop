package com.wheelshop.service.production;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.wheelshop.dao.dstate.IDstateMapper;
import com.wheelshop.dao.production.IProductionMapper;
import com.wheelshop.dao.timer.ITimerMapper;
import com.wheelshop.model.dstate.Dstate;
import com.wheelshop.model.production.Production;
import com.wheelshop.model.timer.Timer;
import com.wheelshop.utils.TimeUtils;
public class ProductionServiceImpl  implements IProductionService {

	@Autowired
	private IProductionMapper iProductionMapper;
	@Autowired
	private ITimerMapper iTimerMapper;
	@Autowired
	private IDstateMapper iDstateMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Production selectProductionById(String id){
		return iProductionMapper.selectproductionById(id);
	}
	public List<Production> selectproductionInIds(String ids){
		return iProductionMapper.selectproductionInIds(ids);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Production> selectProductionByParam(Map paramMap){ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Production> list=iProductionMapper.selectproductionByParam(paramMap);
		///////////////////////
		
		//按生产线：生产时间=系统运行时间（8小时）-当天累计停台时间+加班时间-间休时间-换模时间（实际时间）
		//【生产时间】公式（单位分钟）：该品种的【结束生产时间】-【开始生产时间】-【间休时间】-【午休时间】
		for(int index=0;index<list.size();index++){
			Production p= list.get(index);
			
			//end-start
			int durtime=0;
			if(p.getEndtime()!=null&&p.getStarttime()!=null){
				durtime=(int) ((p.getEndtime().getTime()-p.getStarttime().getTime())/1000);
			}
			//timer
			paramMap=new HashMap(); 
			paramMap.put("prodnum",p.getProdnum()+"");
			//paramMap.put("type","间休");
			int timenumber=iTimerMapper.selectCounttimerByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",timenumber); 
			List<Timer> timerList=iTimerMapper.selecttimerByParam(paramMap);
			//间休时间
			int rest= 0;
			for(Timer t:timerList){
				String[] starttimes=t.getStarttime().split(":");
				String[] endtimes=t.getEndtime().split(":");
				 //t.getEndtime()-t.getStarttime();
				if(endtimes.length==3&&starttimes.length==3){
					rest+=(Integer.parseInt(endtimes[0])*60*60+Integer.parseInt(endtimes[1])*60+Integer.parseInt(endtimes[2]))-
							(Integer.parseInt(starttimes[0])*60*60+Integer.parseInt(starttimes[1])*60+Integer.parseInt(endtimes[2]));
				}
			}
			
			
			int prodtime= durtime-rest;
			//list.get(index).setProdtime(TimeUtils.formatTime(Long.parseLong((prodtime*1000)+"")));
			if(prodtime>=60)
			list.get(index).setProdtime((prodtime/60)+"");
		}
		
		/////////////////////////
	
	 
	
		return list;
	}
	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Production> selectProductionByParam2(Map paramMap){ 
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		List<Production> alllist=iProductionMapper.selectproductionByParam2(paramMap);
		
		
		///////////////////////
		for(int i=0;i<alllist.size();i++){
			Date startTime=null,endTime=null;
			int allProdTime=0;
			//按生产线：生产时间=系统运行时间（8小时）-当天累计停台时间+加班时间-间休时间-换模时间（实际时间）
			//【生产时间】公式（单位分钟）：该品种的【结束生产时间】-【开始生产时间】-【间休时间】-【午休时间】
			List<Production> list=iProductionMapper.selectproductionInIds(alllist.get(i).getIds());
			for(int index=0;index<list.size();index++){
				Production p= list.get(index);
				
				//保存班次开始结束时间
				
				if(p.getStarttime()!=null){
					if(startTime==null){
						startTime=p.getStarttime();
					}
					else{
						if(startTime.after(p.getStarttime())){
							startTime=p.getStarttime();
						}
					}
				}
				if(p.getEndtime()!=null){
					if(endTime==null){
						endTime=p.getEndtime();
					}
					else{
						if(endTime.before(p.getStarttime())){
							endTime=p.getEndtime();
						}
					}
				}
				
				//end-start
				int durtime=0;
				if(p.getEndtime()!=null&&p.getStarttime()!=null){
					durtime=(int) ((p.getEndtime().getTime()-p.getStarttime().getTime())/1000);
				}
				//timer
				paramMap=new HashMap(); 
				paramMap.put("prodnum",p.getProdnum()+"");
				//paramMap.put("type","间休");
				int timenumber=iTimerMapper.selectCounttimerByParam(paramMap);
				paramMap.put("fromPage",0);
				paramMap.put("toPage",timenumber); 
				List<Timer> timerList=iTimerMapper.selecttimerByParam(paramMap);
				//间休时间
				int rest= 0;
				for(Timer t:timerList){
					String[] starttimes=t.getStarttime().split(":");
					String[] endtimes=t.getEndtime().split(":");
					//t.getEndtime()-t.getStarttime();
					if(endtimes.length==3&&starttimes.length==3){
						rest+=(Integer.parseInt(endtimes[0])*60*60+Integer.parseInt(endtimes[1])*60+Integer.parseInt(endtimes[2]))-
								(Integer.parseInt(starttimes[0])*60*60+Integer.parseInt(starttimes[1])*60+Integer.parseInt(endtimes[2]));
					}
				}
				
				
				int prodtime= durtime-rest;
				//list.get(index).setProdtime(TimeUtils.formatTime(Long.parseLong((prodtime*1000)+"")));
				if(prodtime>=60){
					allProdTime+=(prodtime/60);
				}
					//list.get(index).setProdtime((prodtime/60)+"");
			}
			alllist.get(i).setStarttime(startTime);
			alllist.get(i).setEndtime(endTime);
			alllist.get(i).setProdtime(allProdTime+"");
			/////////////////////////
			
			
		}
		
		
		
		
		return alllist;
	}
	@Transactional
	@SuppressWarnings("rawtypes")
	public List<Production> selectAllProductionByParam(Map paramMap){ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Production> list=iProductionMapper.selectallproductionByParam(paramMap);
		///////////////////////
				
		//按生产线：生产时间=系统运行时间（8小时）-当天累计停台时间+加班时间-间休时间-换模时间（实际时间）
		//【生产时间】公式（单位分钟）：该品种的【结束生产时间】-【开始生产时间】-【间休时间】-【午休时间】
		for(int index=0;index<list.size();index++){
			Production p= list.get(index);
			
			//end-start
			int durtime=0;
			if(p.getEndtime()!=null&&p.getStarttime()!=null){
				durtime=(int) ((p.getEndtime().getTime()-p.getStarttime().getTime())/1000);
			}
			//timer
			paramMap=new HashMap(); 
			paramMap.put("prodnum",p.getProdnum()+"");
			//paramMap.put("type","间休");
			int timenumber=iTimerMapper.selectCounttimerByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",timenumber); 
			List<Timer> timerList=iTimerMapper.selecttimerByParam(paramMap);
			//间休时间
			int rest= 0;
			for(Timer t:timerList){
				String[] starttimes=t.getStarttime().split(":");
				String[] endtimes=t.getEndtime().split(":");
				 //t.getEndtime()-t.getStarttime();
				if(endtimes.length==3&&starttimes.length==3){
					rest+=(Integer.parseInt(endtimes[0])*60*60+Integer.parseInt(endtimes[1])*60+Integer.parseInt(endtimes[2]))-
							(Integer.parseInt(starttimes[0])*60*60+Integer.parseInt(starttimes[1])*60+Integer.parseInt(endtimes[2]));
				}
			}
			
			
			int prodtime= durtime-rest;
			//list.get(index).setProdtime(TimeUtils.formatTime(Long.parseLong((prodtime*1000)+"")));
			if(prodtime>=60)
			list.get(index).setProdtime((prodtime/60)+"");
		}

		
		
		return list;
	}

	 
	@SuppressWarnings("rawtypes")
	public List<Production> selectStatisticproductionByParam(Map paramMap){ 
		return iProductionMapper.selectStatisticproductionByParam(paramMap);
	}
	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountProductionByParam(Map paramMap){ 
		return iProductionMapper.selectCountproductionByParam(paramMap);
	}
	@SuppressWarnings("rawtypes")
	public int selectCountProductionByParam2(Map paramMap){ 
		return iProductionMapper.selectCountproductionByParam2(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateProduction(Production production){
		return iProductionMapper.updateproduction(production);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addProduction(Production production){
		return iProductionMapper.addproduction(production);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdProduction(List<Production> list){
		return iProductionMapper.muladdproduction(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteProduction(String id){
		return iProductionMapper.deleteproduction(id);
	}

}

