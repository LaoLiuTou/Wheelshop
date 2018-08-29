package com.wheelshop.service.production;
import java.text.ParseException;
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
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		List<Production> list=iProductionMapper.selectproductionByParam(paramMap);
		///////////////////////
		
		//按生产线：生产时间=系统运行时间（8小时）-当天累计停台时间+加班时间-间休时间-换模时间（实际时间）
		//【生产时间】公式（单位分钟）：该品种的【结束生产时间】-【开始生产时间】-【间休时间】-【午休时间】
		for(int index=0;index<list.size();index++){
			Production p= list.get(index);
			
			//end-start
			int durtime=0;
			//间休时间
			int rest= 0;
			if(p.getEndtime()!=null&&p.getStarttime()!=null){
				durtime=(int) ((p.getEndtime().getTime()-p.getStarttime().getTime())/1000);
			
				//timer
				paramMap=new HashMap(); 
				paramMap.put("prodnum",p.getProdnum()+"");
				//paramMap.put("type","间休");
				int timenumber=iTimerMapper.selectCounttimerByParam(paramMap);
				paramMap.put("fromPage",0);
				paramMap.put("toPage",timenumber); 
				List<Timer> timerList=iTimerMapper.selecttimerByParam(paramMap);
				
				for(Timer t:timerList){
					
					String[] starttimes=t.getStarttime().split(":");
					String[] endtimes=t.getEndtime().split(":");
					 //t.getEndtime()-t.getStarttime();
					if(endtimes.length==3&&starttimes.length==3){
						
						try {
							if(!sdf2.parse(t.getStarttime()).before(sdf2.parse(sdf2.format(p.getStarttime())))&&
									!sdf2.parse(t.getEndtime()).after(sdf2.parse(sdf2.format(p.getEndtime())))){
								rest+=(Integer.parseInt(endtimes[0])*60*60+Integer.parseInt(endtimes[1])*60+Integer.parseInt(endtimes[2]))-
										(Integer.parseInt(starttimes[0])*60*60+Integer.parseInt(starttimes[1])*60+Integer.parseInt(endtimes[2]));
							
							}
							
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			
			}
			int prodtime= durtime-rest;
			//list.get(index).setProdtime(TimeUtils.formatTime(Long.parseLong((prodtime*1000)+"")));
			if(prodtime>=60)
			list.get(index).setProdtime((prodtime/60)+"");
			
			
			
			//dstate
			paramMap=new HashMap(); 
			paramMap.put("production",p.getProdnum()+"");
			paramMap.put("state","01");
			paramMap.put("adddate",sdf.format(p.getAdddate()));
			int equipstopnumber=iDstateMapper.selectCountdstateByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",equipstopnumber); 
			List<Dstate> equipstopList=iDstateMapper.selectdstateByParam(paramMap);
			//设备停台时间
			int equipstopTime=0;
			for(Dstate dstate:equipstopList){
				if(dstate.getDuration()!=null){
					equipstopTime+=Integer.parseInt(dstate.getDuration());
				}
				
			}
			list.get(index).setEquipstop(equipstopTime+"");
			
			paramMap=new HashMap(); 
			paramMap.put("production",p.getProdnum()+"");
			paramMap.put("state","02");
			paramMap.put("adddate",sdf.format(p.getAdddate()));
			int toolstopnumber=iDstateMapper.selectCountdstateByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",toolstopnumber); 
			List<Dstate> toolstopList=iDstateMapper.selectdstateByParam(paramMap);
			//工装停台时间
			int toolstopduration=0;
			for(Dstate dstate:toolstopList){
				if(dstate.getDuration()!=null){
					toolstopduration+=Integer.parseInt(dstate.getDuration());
				}
				
			}
			list.get(index).setToolstop(toolstopduration+"");
			
			paramMap=new HashMap(); 
			paramMap.put("production",p.getProdnum()+"");
			paramMap.put("state","03");
			paramMap.put("adddate",sdf.format(p.getAdddate()));
			int prodstopnumber=iDstateMapper.selectCountdstateByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",prodstopnumber); 
			List<Dstate> prodstopList=iDstateMapper.selectdstateByParam(paramMap);
			//生产停台时间
			int prodstopduration=0;
			for(Dstate dstate:prodstopList){
				if(dstate.getDuration()!=null){
					prodstopduration+=Integer.parseInt(dstate.getDuration());
				}
				
			}
			list.get(index).setProdstop(prodstopduration+"");
		}
		
		/////////////////////////
	
	 
	
		return list;
	}
	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Production> selectProductionByParam2(Map paramMap){ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
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
			}
			
			//end-start
			int durtime=0;
			//间休时间
			int rest= 0;
			if(startTime!=null&&endTime!=null){
				durtime=(int) ((endTime.getTime()-startTime.getTime())/1000);
			 
				//timer
				paramMap=new HashMap(); 
				paramMap.put("prodnum",alllist.get(i).getProdnum()+"");
				//paramMap.put("type","间休");
				int timenumber=iTimerMapper.selectCounttimerByParam(paramMap);
				paramMap.put("fromPage",0);
				paramMap.put("toPage",timenumber); 
				List<Timer> timerList=iTimerMapper.selecttimerByParam(paramMap);
				
				for(Timer t:timerList){
					String[] starttimes=t.getStarttime().split(":");
					String[] endtimes=t.getEndtime().split(":");
					//t.getEndtime()-t.getStarttime();
					if(endtimes.length==3&&starttimes.length==3){
						
						try {
							if(!sdf2.parse(t.getStarttime()).before(sdf2.parse(sdf2.format(startTime)))&&
									!sdf2.parse(t.getEndtime()).after(sdf2.parse(sdf2.format(endTime)))){
								rest+=(Integer.parseInt(endtimes[0])*60*60+Integer.parseInt(endtimes[1])*60+Integer.parseInt(endtimes[2]))-
										(Integer.parseInt(starttimes[0])*60*60+Integer.parseInt(starttimes[1])*60+Integer.parseInt(endtimes[2]));
							}
						 
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			int prodtime= durtime-rest;
			//list.get(index).setProdtime(TimeUtils.formatTime(Long.parseLong((prodtime*1000)+"")));
			if(prodtime>=60){
				allProdTime+=(prodtime/60);
			}
			//list.get(index).setProdtime((prodtime/60)+"");
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
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		List<Production> list=iProductionMapper.selectallproductionByParam(paramMap);
		///////////////////////
				
		//按生产线：生产时间=系统运行时间（8小时）-当天累计停台时间+加班时间-间休时间-换模时间（实际时间）
		//【生产时间】公式（单位分钟）：该品种的【结束生产时间】-【开始生产时间】-【间休时间】-【午休时间】
		for(int index=0;index<list.size();index++){
			Production p= list.get(index);
			
			//end-start
			int durtime=0;
			//间休时间
			int rest= 0;
			if(p.getEndtime()!=null&&p.getStarttime()!=null){
				durtime=(int) ((p.getEndtime().getTime()-p.getStarttime().getTime())/1000);
			
				//timer
				paramMap=new HashMap(); 
				paramMap.put("prodnum",p.getProdnum()+"");
				//paramMap.put("type","间休");
				int timenumber=iTimerMapper.selectCounttimerByParam(paramMap);
				paramMap.put("fromPage",0);
				paramMap.put("toPage",timenumber); 
				List<Timer> timerList=iTimerMapper.selecttimerByParam(paramMap);
				
				for(Timer t:timerList){
					String[] starttimes=t.getStarttime().split(":");
					String[] endtimes=t.getEndtime().split(":");
					 //t.getEndtime()-t.getStarttime();
					if(endtimes.length==3&&starttimes.length==3){
						
						
						try {
							if(!sdf2.parse(t.getStarttime()).before(sdf2.parse(sdf2.format(p.getStarttime())))&&
									!sdf2.parse(t.getEndtime()).after(sdf2.parse(sdf2.format(p.getEndtime())))){
								rest+=(Integer.parseInt(endtimes[0])*60*60+Integer.parseInt(endtimes[1])*60+Integer.parseInt(endtimes[2]))-
										(Integer.parseInt(starttimes[0])*60*60+Integer.parseInt(starttimes[1])*60+Integer.parseInt(endtimes[2]));
							
							}
							
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			
			}
			int prodtime= durtime-rest;
			//list.get(index).setProdtime(TimeUtils.formatTime(Long.parseLong((prodtime*1000)+"")));
			if(prodtime>=60)
			list.get(index).setProdtime((prodtime/60)+"");
			
			
			//dstate
			paramMap=new HashMap(); 
			paramMap.put("production",p.getProdnum()+"");
			paramMap.put("state","01");
			paramMap.put("adddate",sdf.format(p.getAdddate()));
			int equipstopnumber=iDstateMapper.selectCountdstateByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",equipstopnumber); 
			List<Dstate> equipstopList=iDstateMapper.selectdstateByParam(paramMap);
			//设备停台时间
			int equipstopTime=0;
			for(Dstate dstate:equipstopList){
				if(dstate.getDuration()!=null){
					equipstopTime+=Integer.parseInt(dstate.getDuration());
				}
				
			}
			list.get(index).setEquipstop(equipstopTime+"");
			
			paramMap=new HashMap(); 
			paramMap.put("production",p.getProdnum()+"");
			paramMap.put("state","02");
			paramMap.put("adddate",sdf.format(p.getAdddate()));
			int toolstopnumber=iDstateMapper.selectCountdstateByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",toolstopnumber); 
			List<Dstate> toolstopList=iDstateMapper.selectdstateByParam(paramMap);
			//工装停台时间
			int toolstopduration=0;
			for(Dstate dstate:toolstopList){
				if(dstate.getDuration()!=null){
					toolstopduration+=Integer.parseInt(dstate.getDuration());
				}
				
			}
			list.get(index).setToolstop(toolstopduration+"");
			
			paramMap=new HashMap(); 
			paramMap.put("production",p.getProdnum()+"");
			paramMap.put("state","03");
			paramMap.put("adddate",sdf.format(p.getAdddate()));
			int prodstopnumber=iDstateMapper.selectCountdstateByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",prodstopnumber); 
			List<Dstate> prodstopList=iDstateMapper.selectdstateByParam(paramMap);
			//生产停台时间
			int prodstopduration=0;
			for(Dstate dstate:prodstopList){
				if(dstate.getDuration()!=null){
					prodstopduration+=Integer.parseInt(dstate.getDuration());
				}
				
			}
			list.get(index).setProdstop(prodstopduration+"");
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

