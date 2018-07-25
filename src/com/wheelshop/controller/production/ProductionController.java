package com.wheelshop.controller.production;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.IntArraySerializer;
import com.wheelshop.service.production.IProductionService;
import com.wheelshop.service.timer.ITimerService;
import com.wheelshop.service.varieties.IVarietiesService;
import com.wheelshop.utils.ExcelUtil;
import com.wheelshop.chat.common.NettyChannelMap;
import com.wheelshop.model.device.Device;
import com.wheelshop.model.dstate.Dstate;
import com.wheelshop.model.production.Production;
import com.wheelshop.model.timer.Timer;
import com.wheelshop.model.varieties.Varieties;
@Controller
public class ProductionController {
	@Autowired
	private IProductionService iProductionService;
	@Autowired
	private ITimerService iTimerService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("WheelshopLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addProduction")
	@ResponseBody
	public Map add(Production production){
		Map resultMap=new HashMap();
		try {
			
			//更新 或者 新建
			Map paramMap=new HashMap();
			paramMap.put("fromPage",0);
			paramMap.put("toPage",1); 
			paramMap.put("prodnum",production.getProdnum());
			paramMap.put("adddate","1");
			List<Production> plist=iProductionService.selectProductionByParam(paramMap);
			if(plist.size()>0){
				production.setId(plist.get(0).getId());
				if(plist.get(0).getProdstate().equals("换模时间")&&
						production.getProdstate().equals("生产时间")){
					long times=new Date().getTime()-plist.get(0).getStartctime().getTime();
					production.setChangtime(times/60000+"");
				}
				else if(production.getProdstate().equals("换模时间")){
					production.setStartctime(new Date());
				}
				//切换品种 计划产量清空
				if(plist.get(0).getVariety()!=null&&production.getVariety()!=null&&
						!plist.get(0).getVariety().equals(production.getVariety())){
					production.setYield("0");
					production.setActualcomp("0");
				}
				
				iProductionService.updateProduction(production);
			}
			else{
				iProductionService.addProduction(production);
			}
			
			
			
			//推送
			for (Map.Entry entry:NettyChannelMap.map.entrySet()){
	            if (entry.getKey().toString().substring(0, 1).equals(production.getProdnum())){
	            	int rest=0;
	            	if(production.getChangtime()!=null){
	            		rest=Integer.parseInt(production.getChangtime())*60;
	            	}
					ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) entry.getValue();
	            	Map<String, String> contentMap = new HashMap<String, String>();
	            	contentMap.put("T", "5");
	            	contentMap.put("NAME", "system");
	            	contentMap.put("FI", entry.getKey().toString());  
	            	contentMap.put("PRO", production.getProdnum());
	            	contentMap.put("TYPE", production.getProdstate());
	            	contentMap.put("TIMES", rest+"");
					ObjectMapper mapper = new ObjectMapper();
					String json = "";
					json = mapper.writeValueAsString(contentMap);
					
					if(channelHandlerContext!=null){
						
					   channelHandlerContext.writeAndFlush(new TextWebSocketFrame(json));
			        }
	            }
	        }
			
			
			resultMap.put("status", "0");
			resultMap.put("msg", production.getId());
			logger.info("新建成功，主键："+production.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addActualcomp")
	@ResponseBody
	public Map actualcomp(Production production){
		Map resultMap=new HashMap();
		try {
			//查询对应的设备编号
        	Map paramMap=new HashMap();
			paramMap.put("fromPage",0);
			paramMap.put("toPage",1); 
			paramMap.put("prodnum",production.getProdnum());
			List<Production> list=iProductionService.selectProductionByParam(paramMap);
        	
			if(list.size()>0){
				
				Production temp=new Production();
				temp.setId(list.get(0).getId());
				temp.setActualcomp(production.getActualcomp());
				iProductionService.updateProduction(temp);
			}
			//推送
			for (Map.Entry entry:NettyChannelMap.map.entrySet()){
	            if (entry.getKey().toString().substring(0, 1).equals(production.getProdnum())){
	            	
					ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) entry.getValue();
	            	Map<String, String> contentMap = new HashMap<String, String>();
	            	contentMap.put("T", "4");
	            	contentMap.put("NAME", "system");
	            	contentMap.put("FI", entry.getKey().toString());  
	            	contentMap.put("AC", production.getActualcomp());
	            	contentMap.put("POWER", list.get(0).getPower());
	            	contentMap.put("PRO", list.get(0).getProdnum());
					ObjectMapper mapper = new ObjectMapper();
					String json = "";
					json = mapper.writeValueAsString(contentMap);
					
					if(channelHandlerContext!=null){
						
					   channelHandlerContext.writeAndFlush(new TextWebSocketFrame(json));
			        }
					 
	            }
	        }
			
			resultMap.put("status", "0");
			resultMap.put("msg", "修改成功！");
			//System.out.println(production.getProdnum()+":"+production.getActualcomp());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "修改失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/resetProduction")
	@ResponseBody
	public Map reset(Production production){
		Map resultMap=new HashMap();
		try {
			if(production.getProdnum()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				production.setAdddate(new Date());
				production.setProdstate("生产时间");
				production.setOvertime("0");
				production.setCreater("prod"+production.getProdnum());
				production.setStarttime(new Date());
				iProductionService.addProduction(production);
				resultMap.put("status", "0");
				resultMap.put("msg", production.getId());
				logger.info("新建成功，主键："+production.getId());
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdProduction")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Production production){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Production.class);
			List<Production> list = (List<Production>)objectMapper.readValue(data, javaType);
			iProductionService.muladdProduction(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+production.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/deleteProduction")
	@ResponseBody
	public Map delete(Production production){
		Map resultMap=new HashMap();
		try {
			if(production.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iProductionService.deleteProduction(production.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+production.getId());
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "删除失败！");
			logger.info("删除失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/selectProduction")
	@ResponseBody
	public Map select(Production production){
		Map resultMap=new HashMap();
		try {
			if(production.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Production resultSelect=iProductionService.selectProductionById(production.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", resultSelect);
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/updateProduction")
	@ResponseBody
	public Map update(Production production){
		Map resultMap=new HashMap();
		try {
			if(production.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iProductionService.updateProduction(production);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+production.getId());
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "更新失败！");
			logger.info("更新失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/lastProduction")
	@ResponseBody
	public Map last(HttpServletRequest request, HttpServletResponse response,Production production)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			String page=request.getParameter("page");
			String size=request.getParameter("size");
			if(page!=null&&size!=null){
				Map paramMap=new HashMap();
				paramMap.put("fromPage",(Integer.parseInt(page)-1)*Integer.parseInt(size));
				paramMap.put("toPage",Integer.parseInt(size)); 
				paramMap.put("id",production.getId());
				paramMap.put("prodnum",production.getProdnum());
				paramMap.put("production",production.getProduction());
				paramMap.put("changed",production.getChanged());
				paramMap.put("yield",production.getYield());
				paramMap.put("prodstop",production.getProdstop());
				paramMap.put("power",production.getPower());
				paramMap.put("rate",production.getRate());
				paramMap.put("variety",production.getVariety());
				paramMap.put("rhythm",production.getRhythm());
				paramMap.put("itemtime",production.getItemtime());
				paramMap.put("plancomp",production.getPlancomp());
				paramMap.put("equipstop",production.getEquipstop());
				String starttimeFrom=request.getParameter("starttimeFrom");
				String starttimeTo=request.getParameter("starttimeTo");
				if(starttimeFrom!=null&&!starttimeFrom.equals(""))
				paramMap.put("starttimeFrom", sdf.parse(starttimeFrom));
				if(starttimeTo!=null&&!starttimeTo.equals(""))
				paramMap.put("starttimeTo", sdf.parse(starttimeTo));
				paramMap.put("actualcomp",production.getActualcomp());
				paramMap.put("toolstop",production.getToolstop());
				paramMap.put("overtime",production.getOvertime());
				paramMap.put("prodstate",production.getProdstate());
				paramMap.put("creater",production.getCreater());
				String adddateFrom=request.getParameter("adddateFrom");
				String adddateTo=request.getParameter("adddateTo");
				if(adddateFrom!=null&&!adddateFrom.equals(""))
				paramMap.put("adddateFrom", sdf.parse(adddateFrom));
				if(adddateTo!=null&&!adddateTo.equals(""))
				paramMap.put("adddateTo", sdf.parse(adddateTo));
				paramMap.put("flag",production.getFlag());
				
				//当天数据
				paramMap.put("adddate","1");
				
				//int totalnumber=iProductionService.selectCountProductionByParam(paramMap);
				List<Production> list=iProductionService.selectProductionByParam(paramMap);
				if(list.size()==0){
					production.setAdddate(new Date());
					production.setProdstate("生产时间");
					production.setOvertime("0");
					production.setCreater(request.getAttribute("userName").toString());
					production.setStarttime(new Date());
					iProductionService.addProduction(production);
					list=iProductionService.selectProductionByParam(paramMap);
					/*paramMap.remove("adddate");
					list=iProductionService.selectProductionByParam(paramMap);
					if(list.size()>0){
						Production p=list.get(0);
						p.setAdddate(new Date());
						p.setProdstate("生产时间");
						p.setOvertime("0");
						p.setActualcomp(null);
						p.setId(null);
						p.setProdstop(null);
						p.setEquipstop(null);
						p.setToolstop(null);
						p.setStarttime(new Date());
						iProductionService.addProduction(p);
						list=iProductionService.selectProductionByParam(paramMap);
					}
					else{
						production.setAdddate(new Date());
						production.setProdstate("生产时间");
						production.setOvertime("0");
						production.setCreater(request.getAttribute("userName").toString());
						production.setStarttime(new Date());
						iProductionService.addProduction(production);
						list=iProductionService.selectProductionByParam(paramMap);
					}*/
				}
				 
				
				Map tempMap=new HashMap();
				resultMap.put("status", "0");
				//tempMap.put("num", totalnumber);
				tempMap.put("data", list);
				resultMap.put("msg", tempMap);
			}
			else{
				resultMap.put("status", "-1");
				resultMap.put("msg", "分页参数不能为空！");
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/listProduction")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Production production)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			String page=request.getParameter("page");
			String size=request.getParameter("size");
			if(page!=null&&size!=null){
				Map paramMap=new HashMap();
				paramMap.put("fromPage",(Integer.parseInt(page)-1)*Integer.parseInt(size));
				paramMap.put("toPage",Integer.parseInt(size)); 
				paramMap.put("id",production.getId());
				paramMap.put("prodnum",production.getProdnum());
				paramMap.put("production",production.getProduction());
				paramMap.put("changed",production.getChanged());
				paramMap.put("yield",production.getYield());
				paramMap.put("prodstop",production.getProdstop());
				paramMap.put("power",production.getPower());
				paramMap.put("rate",production.getRate());
				paramMap.put("variety",production.getVariety());
				paramMap.put("rhythm",production.getRhythm());
				paramMap.put("itemtime",production.getItemtime());
				paramMap.put("plancomp",production.getPlancomp());
				paramMap.put("equipstop",production.getEquipstop());
				String starttimeFrom=request.getParameter("starttimeFrom");
				String starttimeTo=request.getParameter("starttimeTo");
				if(starttimeFrom!=null&&!starttimeFrom.equals(""))
				paramMap.put("starttimeFrom", sdf.parse(starttimeFrom));
				if(starttimeTo!=null&&!starttimeTo.equals(""))
				paramMap.put("starttimeTo", sdf.parse(starttimeTo));
				paramMap.put("actualcomp",production.getActualcomp());
				paramMap.put("toolstop",production.getToolstop());
				paramMap.put("overtime",production.getOvertime());
				paramMap.put("prodstate",production.getProdstate());
				paramMap.put("creater",production.getCreater());
				String adddateFrom=request.getParameter("adddateFrom");
				String adddateTo=request.getParameter("adddateTo");
				if(adddateFrom!=null&&!adddateFrom.equals(""))
				paramMap.put("adddateFrom", sdf.parse(adddateFrom));
				if(adddateTo!=null&&!adddateTo.equals(""))
				paramMap.put("adddateTo", sdf.parse(adddateTo));
				paramMap.put("flag",production.getFlag());
				List<Production> list=iProductionService.selectProductionByParam(paramMap);
				int totalnumber=iProductionService.selectCountProductionByParam(paramMap);
				
				Map tempMap=new HashMap();
				resultMap.put("status", "0");
				tempMap.put("num", totalnumber);
				tempMap.put("data", list);
				resultMap.put("msg", tempMap);
				
				 
				
			}
			else{
				resultMap.put("status", "-1");
				resultMap.put("msg", "分页参数不能为空！");
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/exportProduction")
	public void export(HttpServletRequest request, HttpServletResponse response,Production production)
			throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			
			Map paramMap=new HashMap();
			paramMap.put("id",production.getId());
			paramMap.put("prodnum",production.getProdnum());
			paramMap.put("production",production.getProduction());
			paramMap.put("changed",production.getChanged());
			paramMap.put("yield",production.getYield());
			paramMap.put("prodstop",production.getProdstop());
			paramMap.put("power",production.getPower());
			paramMap.put("rate",production.getRate());
			paramMap.put("variety",production.getVariety());
			paramMap.put("rhythm",production.getRhythm());
			paramMap.put("itemtime",production.getItemtime());
			paramMap.put("plancomp",production.getPlancomp());
			paramMap.put("equipstop",production.getEquipstop());
			String starttimeFrom=request.getParameter("starttimeFrom");
			String starttimeTo=request.getParameter("starttimeTo");
			if(starttimeFrom!=null&&!starttimeFrom.equals(""))
			paramMap.put("starttimeFrom", sdf.parse(starttimeFrom));
			if(starttimeTo!=null&&!starttimeTo.equals(""))
			paramMap.put("starttimeTo", sdf.parse(starttimeTo));
			paramMap.put("actualcomp",production.getActualcomp());
			paramMap.put("toolstop",production.getToolstop());
			paramMap.put("overtime",production.getOvertime());
			paramMap.put("prodstate",production.getProdstate());
			paramMap.put("creater",production.getCreater());
			String adddateFrom=request.getParameter("adddateFrom");
			String adddateTo=request.getParameter("adddateTo");
			if(adddateFrom!=null&&!adddateFrom.equals(""))
			paramMap.put("adddateFrom", sdf.parse(adddateFrom));
			if(adddateTo!=null&&!adddateTo.equals(""))
			paramMap.put("adddateTo", sdf.parse(adddateTo));
			paramMap.put("flag",production.getFlag());
			List<Production> list=iProductionService.selectAllProductionByParam(paramMap);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			List<String[]> exportList = new ArrayList<>();
			//float sum_1=0,sum_2=0,sum_3=0;
			for(int index=0;index<list.size();index++){
				Production temp = list.get(index);
				
				Float comRate = null;
				if(StringUtils.isNumeric(temp.getActualcomp())&&StringUtils.isNumeric(temp.getPlancomp())){
					comRate= (Float.parseFloat(temp.getActualcomp())/Float.parseFloat(temp.getPlancomp()))*100;
				}
				Float rate = null;
				if(StringUtils.isNumeric(temp.getActualcomp())&&StringUtils.isNumeric(temp.getPower())){
					rate= (Float.parseFloat(temp.getActualcomp())/Float.parseFloat(temp.getPower()))*100;
				}
				String  comRateStr="",rateStr="";
				if(comRate!=null){
					comRateStr=String.format("%.0f", comRate);
				}
				if(rate!=null){
					rateStr=String.format("%.1f", rate)+"%";
				}
				
				String[] strings = {(index+1)+"", temp.getProduction(), temp.getPlancomp(),temp.getActualcomp(),  
						comRateStr,temp.getProdtime(),rateStr,sdf1.format(temp.getAdddate())};
				exportList.add(strings);
			}
			/*String[] strings = {"合计", "", "", "", "","", "", "","","", 
					String.format("%.3f", sum_1), String.format("%.2f", sum_2), String.format("%.2f", sum_3),
					"", "","", "", ""};
			exportList.add(strings);*/
			 
			ServletOutputStream out=response.getOutputStream();
			String fileName = "生产完成统计"+sdf1.format(new Date());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
			String[] titles = { "序号","生产线", "计划完成", "实际完成", "完成率", "生产时间", "可动率", "日期"}; 
			ExcelUtil.export(titles, out, exportList);
			
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		//return resultMap;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/statisticProduction")
	@ResponseBody
	public Map statistic(HttpServletRequest request, HttpServletResponse response,Production production)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
		
			Map paramMap=new HashMap();
		
			paramMap.put("id",production.getId());
			paramMap.put("prodnum",production.getProdnum());
			paramMap.put("production",production.getProduction());
			paramMap.put("changed",production.getChanged());
			paramMap.put("yield",production.getYield());
			paramMap.put("prodstop",production.getProdstop());
			paramMap.put("power",production.getPower());
			paramMap.put("rate",production.getRate());
			paramMap.put("variety",production.getVariety());
			paramMap.put("rhythm",production.getRhythm());
			paramMap.put("itemtime",production.getItemtime());
			paramMap.put("plancomp",production.getPlancomp());
			paramMap.put("equipstop",production.getEquipstop());
			String starttimeFrom=request.getParameter("starttimeFrom");
			String starttimeTo=request.getParameter("starttimeTo");
			if(starttimeFrom!=null&&!starttimeFrom.equals(""))
			paramMap.put("starttimeFrom", sdf.parse(starttimeFrom));
			if(starttimeTo!=null&&!starttimeTo.equals(""))
			paramMap.put("starttimeTo", sdf.parse(starttimeTo));
			paramMap.put("actualcomp",production.getActualcomp());
			paramMap.put("toolstop",production.getToolstop());
			paramMap.put("overtime",production.getOvertime());
			paramMap.put("prodstate",production.getProdstate());
			paramMap.put("creater",production.getCreater());
			String adddateFrom=request.getParameter("adddateFrom");
			String adddateTo=request.getParameter("adddateTo");
			if(adddateFrom!=null&&!adddateFrom.equals(""))
			paramMap.put("adddateFrom", sdf.parse(adddateFrom));
			if(adddateTo!=null&&!adddateTo.equals(""))
			paramMap.put("adddateTo", sdf.parse(adddateTo));
			paramMap.put("flag",production.getFlag());
			List<Production> list=iProductionService.selectStatisticproductionByParam(paramMap);
			
			Map tempMap=new HashMap();
			resultMap.put("status", "0");
			resultMap.put("msg", list);
				
			
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
}
