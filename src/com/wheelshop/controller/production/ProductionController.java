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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheelshop.service.production.IProductionService;
import com.wheelshop.chat.common.NettyChannelMap;
import com.wheelshop.model.device.Device;
import com.wheelshop.model.production.Production;
@Controller
public class ProductionController {
	@Autowired
	private IProductionService iProductionService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("WheelshopLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addProduction")
	@ResponseBody
	public Map add(Production production){
		Map resultMap=new HashMap();
		try {
			iProductionService.addProduction(production);
			
			//推送
			for (Map.Entry entry:NettyChannelMap.map.entrySet()){
	            if (entry.getKey().toString().substring(0, 1).equals(production.getFlag()+"")){
	            	 
					ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) entry.getValue();
	            	Map<String, String> contentMap = new HashMap<String, String>();
	            	contentMap.put("T", "5");
	            	contentMap.put("NAME", "system");
	            	contentMap.put("FI", entry.getKey().toString());  
	            	contentMap.put("PRO", production.getFlag()+"");
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
			//推送
			for (Map.Entry entry:NettyChannelMap.map.entrySet()){
	            if (entry.getKey().toString().substring(0, 1).equals(production.getProduction())){
	            	//查询对应的设备编号
	            	Map paramMap=new HashMap();
					paramMap.put("fromPage",0);
					paramMap.put("toPage",1); 
					paramMap.put("flag",production.getProduction());
					List<Production> list=iProductionService.selectProductionByParam(paramMap);
	            	
					if(list.size()>0){
						
						Production temp=new Production();
						temp.setId(list.get(0).getId());
						temp.setActualcomp(production.getActualcomp());
						iProductionService.updateProduction(temp);
						ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) entry.getValue();
		            	Map<String, String> contentMap = new HashMap<String, String>();
		            	contentMap.put("T", "4");
		            	contentMap.put("NAME", "system");
		            	contentMap.put("FI", entry.getKey().toString());  
		            	contentMap.put("AC", production.getActualcomp());
		            	contentMap.put("POWER", list.get(0).getPower());
		            	contentMap.put("PRO", list.get(0).getProduction());
						ObjectMapper mapper = new ObjectMapper();
						String json = "";
						json = mapper.writeValueAsString(contentMap);
						
						if(channelHandlerContext!=null){
							
						   channelHandlerContext.writeAndFlush(new TextWebSocketFrame(json));
				        }
					}
	            }
	        }
			
			resultMap.put("status", "0");
			resultMap.put("msg", "修改成功！");
			System.out.println(production.getProduction()+":"+production.getActualcomp());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "修改失败！");
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
				paramMap.put("production",production.getProduction());
				paramMap.put("changed",production.getChanged());
				paramMap.put("yield",production.getYield());
				paramMap.put("prodstop",production.getProdstop());
				paramMap.put("power",production.getPower());
				paramMap.put("rate",production.getRate());
				paramMap.put("variety",production.getVariety());
				paramMap.put("rhythm",production.getRhythm());
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
}
