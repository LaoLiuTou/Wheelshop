package com.wheelshop.controller.dstate;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wheelshop.service.device.IDeviceService;
import com.wheelshop.service.dstate.IDstateService;
import com.wheelshop.service.production.IProductionService;
import com.wheelshop.utils.ExcelUtil;
import com.wheelshop.utils.TimeUtils;
import com.wheelshop.chat.common.NettyChannelMap;
import com.wheelshop.model.device.Device;
import com.wheelshop.model.dstate.Dstate;
import com.wheelshop.model.production.Production; 
@Controller
public class DstateController {
	@Autowired
	private IDstateService iDstateService;
	@Autowired
	private IDeviceService iDeviceService;
	@Autowired
	private IProductionService iProductionService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("WheelshopLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addDstate")
	@ResponseBody
	public Map add(Dstate dstate){
		Map resultMap=new HashMap();
		try {
			iDstateService.addDstate(dstate);
			ObjectMapper mapper = new ObjectMapper();
			
			//查询对应的设备编号
			Map paramMap=new HashMap();
			paramMap.put("fromPage",0);
			paramMap.put("toPage",1); 
			paramMap.put("deviceno",dstate.getDeviceno());
			paramMap.put("production",dstate.getProduction());
			List<Device> list=iDeviceService.selectDeviceByParam(paramMap);
        	
			if(list.size()>0){
				
				paramMap=new HashMap();
				paramMap.put("fromPage",0);
				paramMap.put("toPage",1); 
				paramMap.put("flag",dstate.getProduction());
				List<Production> plist=iProductionService.selectProductionByParam(paramMap);
				if(plist.size()>0){
					Map prodstopmap;
					if(plist.get(0).getProdstop()!=null&&!plist.get(0).getProdstop().equals("")){ 
					    prodstopmap=mapper.readValue(plist.get(0).getProdstop(), Map.class);
					}
					else{
						prodstopmap=new HashMap();
					}
					//Map equipstopmap=mapper.readValue(plist.get(0).getEquipstop(), Map.class);
					//Map toolstopmap=mapper.readValue(plist.get(0).getToolstop(), Map.class);
					prodstopmap.put(list.get(0).getNodeno(), dstate.getState());
					//05关闭声音
					if(dstate!=null&&dstate.getState()!=null&&!dstate.getState().equals("05")){
						Production temp=new Production();
						temp.setId(plist.get(0).getId());
						temp.setProdstop(mapper.writeValueAsString(prodstopmap));
						iProductionService.updateProduction(temp);
						
						//04判断持续时间
						if(dstate.getState().equals("04")){
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							paramMap=new HashMap();
							 
							paramMap.put("deviceno",dstate.getDeviceno());
							paramMap.put("production",dstate.getProduction());
							paramMap.put("adddate",sdf1.format(new Date()));
							paramMap.put("statein","123");
							List<Dstate> dlist=iDstateService.selectAllDstateByParam(paramMap);
							for(Dstate d:dlist){
								if(d.getDuration()==null||d.getDuration().equals("")){
									dstate=iDstateService.selectDstateById(dstate.getId()+"");
									Long times=dstate.getAdddate().getTime()-d.getAdddate().getTime();
									//d.setDuration(TimeUtils.formatTime(times));
									d.setDuration((times/1000)+"");
									iDstateService.updateDstate(d);
								}
							}
						}
					}
					
					
				}
				
				
				//推送
				for (Map.Entry entry:NettyChannelMap.map.entrySet()){
					System.out.println(entry.getKey().toString().substring(0, 2));
		            if (entry.getKey().toString().substring(0, 1).equals(dstate.getProduction())){
		            	
						ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) entry.getValue();
		            	Map<String, String> contentMap = new HashMap<String, String>();
		            	contentMap.put("T", "3");
		            	contentMap.put("NAME", "system");
		            	contentMap.put("FI", entry.getKey().toString());  
		            	contentMap.put("PRO", dstate.getProduction());
		            	contentMap.put("NUM", list.get(0).getNodeno());
		            	contentMap.put("STATE", dstate.getState());
						
						String json = "";
						json = mapper.writeValueAsString(contentMap);
						
						if(channelHandlerContext!=null){
						   channelHandlerContext.writeAndFlush(new TextWebSocketFrame(json));
				        }
						
		            }
		        }
			}
			
			
		 
			resultMap.put("status", "0");
			resultMap.put("msg", dstate.getId());
			logger.info("新建成功，主键："+dstate.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdDstate")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Dstate dstate){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Dstate.class);
			List<Dstate> list = (List<Dstate>)objectMapper.readValue(data, javaType);
			iDstateService.muladdDstate(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+dstate.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/deleteDstate")
	@ResponseBody
	public Map delete(Dstate dstate){
		Map resultMap=new HashMap();
		try {
			if(dstate.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iDstateService.deleteDstate(dstate.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+dstate.getId());
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
	@RequestMapping("/selectDstate")
	@ResponseBody
	public Map select(Dstate dstate){
		Map resultMap=new HashMap();
		try {
			if(dstate.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Dstate resultSelect=iDstateService.selectDstateById(dstate.getId()+"");
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
	@RequestMapping("/updateDstate")
	@ResponseBody
	public Map update(Dstate dstate){
		Map resultMap=new HashMap();
		try {
			if(dstate.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iDstateService.updateDstate(dstate);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+dstate.getId());
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
	@RequestMapping("/listDstate")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Dstate dstate)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			String page=request.getParameter("page");
			String size=request.getParameter("size");
			if(page!=null&&size!=null){
				Map paramMap=new HashMap();
				paramMap.put("fromPage",(Integer.parseInt(page)-1)*Integer.parseInt(size));
				paramMap.put("toPage",Integer.parseInt(size)); 
				paramMap.put("id",dstate.getId());
				paramMap.put("production",dstate.getProduction());
				paramMap.put("deviceno",dstate.getDeviceno());
				paramMap.put("state",dstate.getState());
				paramMap.put("duration",dstate.getDuration());
				String adddateFrom=request.getParameter("adddateFrom");
				String adddateTo=request.getParameter("adddateTo");
				if(adddateFrom!=null&&!adddateFrom.equals(""))
				paramMap.put("adddateFrom", sdf.parse(adddateFrom));
				if(adddateTo!=null&&!adddateTo.equals(""))
				paramMap.put("adddateTo", sdf.parse(adddateTo));
				paramMap.put("comment",dstate.getComment());
				paramMap.put("flag",dstate.getFlag());
				paramMap.put("statein","123");
				List<Dstate> list=iDstateService.selectDstateByParam(paramMap);
				int totalnumber=iDstateService.selectCountDstateByParam(paramMap);
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
	@RequestMapping("/exportDstate")
	public void export(HttpServletRequest request, HttpServletResponse response,Dstate dstate)
			throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
				Map paramMap=new HashMap();
				paramMap.put("id",dstate.getId());
				paramMap.put("production",dstate.getProduction());
				paramMap.put("deviceno",dstate.getDeviceno());
				paramMap.put("state",dstate.getState());
				paramMap.put("duration",dstate.getDuration());
				String adddateFrom=request.getParameter("adddateFrom");
				String adddateTo=request.getParameter("adddateTo");
				if(adddateFrom!=null&&!adddateFrom.equals(""))
					paramMap.put("adddateFrom", sdf.parse(adddateFrom));
				if(adddateTo!=null&&!adddateTo.equals(""))
					paramMap.put("adddateTo", sdf.parse(adddateTo));
				paramMap.put("comment",dstate.getComment());
				paramMap.put("flag",dstate.getFlag());
				paramMap.put("statein","123");
				List<Dstate> list=iDstateService.selectAllDstateByParam(paramMap);
				
				List<String[]> exportList = new ArrayList<>();
				//float sum_1=0,sum_2=0,sum_3=0;
				for(int index=0;index<list.size();index++){
					Dstate temp = list.get(index);
					String state="";
					 
					if(temp.getState()!=null){
						switch (temp.getState()) {
						case "01":
							state="设备异常";
							break;
						case "02":
							state="工装异常";
							break;
						case "03":
							state="生产异常";
							break;
						default:
							break;
						}
					}
					
					String[] strings = {(index+1)+"", temp.getProduction(), state,temp.getDuration(), temp.getDeviceno(), 
							sdf.format(temp.getAdddate()), temp.getComment()};
					exportList.add(strings);
				}
				/*String[] strings = {"合计", "", "", "", "","", "", "","","", 
						String.format("%.3f", sum_1), String.format("%.2f", sum_2), String.format("%.2f", sum_3),
						"", "","", "", ""};
				exportList.add(strings);*/
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				ServletOutputStream out=response.getOutputStream();
				String fileName = "停台数据统计"+sdf1.format(new Date());
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
				String[] titles = { "序号","生产线", "停台类型号", "停台时长（秒）", "停台设备", "日期", "备注"}; 
			 
				ExcelUtil.export(titles, out, exportList);
			
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		//return resultMap;
	}
}
