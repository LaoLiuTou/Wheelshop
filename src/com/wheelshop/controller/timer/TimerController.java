package com.wheelshop.controller.timer;
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
import com.wheelshop.service.timer.ITimerService;
import com.wheelshop.utils.ExcelUtil;
import com.wheelshop.model.timer.Timer;
import com.wheelshop.model.varieties.Varieties;
@Controller
public class TimerController {
	@Autowired
	private ITimerService iTimerService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("WheelshopLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addTimer")
	@ResponseBody
	public Map add(Timer timer){
		Map resultMap=new HashMap();
		try {
			iTimerService.addTimer(timer);
			resultMap.put("status", "0");
			resultMap.put("msg", timer.getId());
			logger.info("新建成功，主键："+timer.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdTimer")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Timer timer){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Timer.class);
			List<Timer> list = (List<Timer>)objectMapper.readValue(data, javaType);
			iTimerService.muladdTimer(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+timer.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/deleteTimer")
	@ResponseBody
	public Map delete(HttpServletRequest request){
		Map resultMap=new HashMap();
		String ids=request.getParameter("ids");
		try {
			if(ids==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iTimerService.deleteTimer(ids);
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+ids);
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
	@RequestMapping("/selectTimer")
	@ResponseBody
	public Map select(Timer timer){
		Map resultMap=new HashMap();
		try {
			if(timer.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Timer resultSelect=iTimerService.selectTimerById(timer.getId()+"");
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
	@RequestMapping("/updateTimer")
	@ResponseBody
	public Map update(Timer timer){
		Map resultMap=new HashMap();
		try {
			if(timer.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iTimerService.updateTimer(timer);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+timer.getId());
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
	@RequestMapping("/listTimer")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Timer timer)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			String page=request.getParameter("page");
			String size=request.getParameter("size");
			if(page!=null&&size!=null){
				Map paramMap=new HashMap();
				paramMap.put("fromPage",(Integer.parseInt(page)-1)*Integer.parseInt(size));
				paramMap.put("toPage",Integer.parseInt(size)); 
				paramMap.put("id",timer.getId());
				paramMap.put("prodnum",timer.getProdnum());
				paramMap.put("production",timer.getProduction());
				paramMap.put("type",timer.getType());
				paramMap.put("starttime",timer.getStarttime());
				paramMap.put("endtime",timer.getEndtime());
				paramMap.put("creater",timer.getCreater());
				String adddateFrom=request.getParameter("adddateFrom");
				String adddateTo=request.getParameter("adddateTo");
				if(adddateFrom!=null&&!adddateFrom.equals(""))
				paramMap.put("adddateFrom", sdf.parse(adddateFrom));
				if(adddateTo!=null&&!adddateTo.equals(""))
				paramMap.put("adddateTo", sdf.parse(adddateTo));
				paramMap.put("flag",timer.getFlag());
				List<Timer> list=iTimerService.selectTimerByParam(paramMap);
				int totalnumber=iTimerService.selectCountTimerByParam(paramMap);
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
	@RequestMapping("/exportTimer")
	public void export(HttpServletRequest request, HttpServletResponse response,Timer timer)
			throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			Map paramMap=new HashMap();
			paramMap.put("id",timer.getId());
			paramMap.put("prodnum",timer.getProdnum());
			paramMap.put("production",timer.getProduction());
			paramMap.put("type",timer.getType());
			paramMap.put("starttime",timer.getStarttime());
			paramMap.put("endtime",timer.getEndtime());
			paramMap.put("creater",timer.getCreater());
			String adddateFrom=request.getParameter("adddateFrom");
			String adddateTo=request.getParameter("adddateTo");
			if(adddateFrom!=null&&!adddateFrom.equals(""))
			paramMap.put("adddateFrom", sdf.parse(adddateFrom));
			if(adddateTo!=null&&!adddateTo.equals(""))
			paramMap.put("adddateTo", sdf.parse(adddateTo));
			paramMap.put("flag",timer.getFlag());
			int totalnumber=iTimerService.selectCountTimerByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",totalnumber); 
			List<Timer> list=iTimerService.selectTimerByParam(paramMap);
				
			List<String[]> exportList = new ArrayList<>();
			//float sum_1=0,sum_2=0,sum_3=0;
			for(int index=0;index<list.size();index++){
				Timer temp = list.get(index);
				 
				String[] strings = {(index+1)+"", temp.getProduction(), temp.getType(), 
						temp.getStarttime()+"-"+temp.getEndtime(),
						temp.getCreater(),sdf.format(temp.getAdddate())};
				exportList.add(strings);
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			ServletOutputStream out=response.getOutputStream();
			String fileName = "时间管理"+sdf1.format(new Date());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
			String[] titles = { "序号","生产线", "时间类型", "时间",  "添加人", "添加时间"}; 
		  
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
