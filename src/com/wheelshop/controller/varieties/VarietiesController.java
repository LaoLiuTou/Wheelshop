package com.wheelshop.controller.varieties;
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
import com.wheelshop.service.varieties.IVarietiesService;
import com.wheelshop.utils.ExcelUtil;
import com.wheelshop.model.dstate.Dstate;
import com.wheelshop.model.varieties.Varieties;
@Controller
public class VarietiesController {
	@Autowired
	private IVarietiesService iVarietiesService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("WheelshopLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addVarieties")
	@ResponseBody
	public Map add(Varieties varieties){
		Map resultMap=new HashMap();
		try {
			iVarietiesService.addVarieties(varieties);
			resultMap.put("status", "0");
			resultMap.put("msg", varieties.getId());
			logger.info("新建成功，主键："+varieties.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdVarieties")
	@ResponseBody
	public Map muladd(HttpServletRequest request,Varieties varieties){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Varieties.class);
			List<Varieties> list = (List<Varieties>)objectMapper.readValue(data, javaType);
			iVarietiesService.muladdVarieties(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+varieties.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/deleteVarieties")
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
				int resultDelete=iVarietiesService.deleteVarieties(ids);
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
	@RequestMapping("/selectVarieties")
	@ResponseBody
	public Map select(Varieties varieties){
		Map resultMap=new HashMap();
		try {
			if(varieties.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				Varieties resultSelect=iVarietiesService.selectVarietiesById(varieties.getId()+"");
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
	@RequestMapping("/updateVarieties")
	@ResponseBody
	public Map update(Varieties varieties){
		Map resultMap=new HashMap();
		try {
			if(varieties.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iVarietiesService.updateVarieties(varieties);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+varieties.getId());
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
	@RequestMapping("/listVarieties")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,Varieties varieties)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			String page=request.getParameter("page");
			String size=request.getParameter("size");
			if(page!=null&&size!=null){
				Map paramMap=new HashMap();
				paramMap.put("fromPage",(Integer.parseInt(page)-1)*Integer.parseInt(size));
				paramMap.put("toPage",Integer.parseInt(size)); 
				paramMap.put("id",varieties.getId());
				paramMap.put("variety",varieties.getVariety());
				paramMap.put("yield",varieties.getYield());
				paramMap.put("rhythm",varieties.getRhythm());
				paramMap.put("itemtime",varieties.getItemtime());
				paramMap.put("prodnum",varieties.getProdnum());
				paramMap.put("production",varieties.getProduction());
				paramMap.put("capacity",varieties.getCapacity());
				paramMap.put("changtime",varieties.getChangtime());
				paramMap.put("required",varieties.getRequired());
				paramMap.put("creater",varieties.getCreater());
				String adddateFrom=request.getParameter("adddateFrom");
				String adddateTo=request.getParameter("adddateTo");
				if(adddateFrom!=null&&!adddateFrom.equals(""))
				paramMap.put("adddateFrom", sdf.parse(adddateFrom));
				if(adddateTo!=null&&!adddateTo.equals(""))
				paramMap.put("adddateTo", sdf.parse(adddateTo));
				paramMap.put("type",varieties.getType());
				paramMap.put("flag",varieties.getFlag());
				List<Varieties> list=iVarietiesService.selectVarietiesByParam(paramMap);
				int totalnumber=iVarietiesService.selectCountVarietiesByParam(paramMap);
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
	@RequestMapping("/allVarieties")
	@ResponseBody
	public Map all(HttpServletRequest request, HttpServletResponse response,Varieties varieties)
			throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			
				Map paramMap=new HashMap();
				paramMap.put("id",varieties.getId());
				paramMap.put("variety",varieties.getVariety());
				paramMap.put("yield",varieties.getYield());
				paramMap.put("rhythm",varieties.getRhythm());
				paramMap.put("itemtime",varieties.getItemtime());
				paramMap.put("prodnum",varieties.getProdnum());
				paramMap.put("production",varieties.getProduction());
				paramMap.put("capacity",varieties.getCapacity());
				paramMap.put("changtime",varieties.getChangtime());
				paramMap.put("required",varieties.getRequired());
				paramMap.put("creater",varieties.getCreater());
				String adddateFrom=request.getParameter("adddateFrom");
				String adddateTo=request.getParameter("adddateTo");
				if(adddateFrom!=null&&!adddateFrom.equals(""))
					paramMap.put("adddateFrom", sdf.parse(adddateFrom));
				if(adddateTo!=null&&!adddateTo.equals(""))
					paramMap.put("adddateTo", sdf.parse(adddateTo));
				paramMap.put("type",varieties.getType());
				paramMap.put("flag",varieties.getFlag());
				int totalnumber=iVarietiesService.selectCountVarietiesByParam(paramMap);
				paramMap.put("fromPage",0);
				paramMap.put("toPage",totalnumber); 
				List<Varieties> list=iVarietiesService.selectVarietiesByParam(paramMap);
				Map tempMap=new HashMap();
				resultMap.put("status", "0");
				tempMap.put("num", totalnumber);
				tempMap.put("data", list);
				resultMap.put("msg", tempMap);
			
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "查询失败！");
			logger.info("查询失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/exportVarieties")
	public void export(HttpServletRequest request, HttpServletResponse response,Varieties varieties)
			throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			Map paramMap=new HashMap();
			paramMap.put("id",varieties.getId());
			paramMap.put("variety",varieties.getVariety());
			paramMap.put("yield",varieties.getYield());
			paramMap.put("rhythm",varieties.getRhythm());
			paramMap.put("itemtime",varieties.getItemtime());
			paramMap.put("prodnum",varieties.getProdnum());
			paramMap.put("production",varieties.getProduction());
			paramMap.put("capacity",varieties.getCapacity());
			paramMap.put("changtime",varieties.getChangtime());
			paramMap.put("required",varieties.getRequired());
			paramMap.put("creater",varieties.getCreater());
			String adddateFrom=request.getParameter("adddateFrom");
			String adddateTo=request.getParameter("adddateTo");
			if(adddateFrom!=null&&!adddateFrom.equals(""))
				paramMap.put("adddateFrom", sdf.parse(adddateFrom));
			if(adddateTo!=null&&!adddateTo.equals(""))
				paramMap.put("adddateTo", sdf.parse(adddateTo));
			paramMap.put("type",varieties.getType());
			paramMap.put("flag",varieties.getFlag());
			int totalnumber=iVarietiesService.selectCountVarietiesByParam(paramMap);
			paramMap.put("fromPage",0);
			paramMap.put("toPage",totalnumber); 
			List<Varieties> list=iVarietiesService.selectVarietiesByParam(paramMap);
				
			List<String[]> exportList = new ArrayList<>();
			//float sum_1=0,sum_2=0,sum_3=0;
			for(int index=0;index<list.size();index++){
				Varieties temp = list.get(index);
				 
				String[] strings = {(index+1)+"", temp.getVariety(), temp.getYield(), temp.getRhythm(),
						temp.getItemtime(),temp.getProduction(), temp.getCapacity(),temp.getChangtime(),temp.getCreater(),
						sdf.format(temp.getAdddate())};
				exportList.add(strings);
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			ServletOutputStream out=response.getOutputStream();
			String fileName = "品种清单"+sdf1.format(new Date());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
			String[] titles = { "序号","品种", "计划产量", "节拍","每件时间", "生产线", "产能", "换模时间", "添加人", "添加时间"}; 
		 
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
