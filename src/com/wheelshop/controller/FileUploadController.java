package com.wheelshop.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
 
import com.wheelshop.model.prodnum.Prodnum;
import com.wheelshop.model.timer.Timer;
import com.wheelshop.model.varieties.Varieties;
import com.wheelshop.service.prodnum.IProdnumService;
import com.wheelshop.service.timer.ITimerService;
import com.wheelshop.service.varieties.IVarietiesService;
import com.wheelshop.utils.ExcelUtil;

@Controller
public class FileUploadController {
	@Autowired
	private ITimerService iTimerService;
	@Autowired
	private IVarietiesService iVarietiesService;
	@Autowired
	private IProdnumService iProdnumService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	Logger logger = Logger.getLogger("WheelshopLogger");
	/*** 
	 * 保存文件 
	 * @param file 
	 * @return 
	 */  
	private boolean saveFile(MultipartFile file, String path,String filename) {
		if (!file.isEmpty()) { 
			try {  
				File filepath = new File(path);
				if (!filepath.exists()) 
				filepath.mkdirs();
				// 转存文件  
				file.transferTo(new File(path+filename));  
				return true;  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
	return false;  
	}  

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/filesUpload") 
	@ResponseBody
	public Map filesUpload(@RequestParam("files") MultipartFile[] files,HttpServletRequest request) {
		Map resultMap=new HashMap();
		try {
			List<String> fileList= new ArrayList<String>();
			String projectName=request.getParameter("project");
			if(projectName==null||projectName.equals("")){
				projectName="default";
			}
			String middleStr=projectName+"/"+sdf.format(new Date())+"/";
			String path=request.getSession().getServletContext().getRealPath("/")+"upload/"+middleStr;
			//判断file数组不能为空并且长度大于0 
			if(files!=null&&files.length>0){  
				//循环获取file数组中得文件  
				for(int i = 0;i<files.length;i++){  
					MultipartFile file = files[i];  
					String filename=UUID.randomUUID()+"."+file.getOriginalFilename().split("\\.")[1];
					//保存文件  
					if(saveFile(file, path,filename)){
						
						//读取文件后存储
						String tableName=request.getParameter("table");
						if(tableName!=null&&tableName.equals("timer")){
							createTimer(path, filename);
							filename=filename.split("\\.")[0]+"_result."+filename.split("\\.")[1];
							fileList.add(request.getScheme()+"://"+ request.getServerName()+
									":"+request.getServerPort()+
									request.getContextPath()+"/upload/"+middleStr+filename);
						}
						else if(tableName!=null&&tableName.equals("varieties")){
							createVarieties(path, filename);
							filename=filename.split("\\.")[0]+"_result."+filename.split("\\.")[1];
							fileList.add(request.getScheme()+"://"+ request.getServerName()+
									":"+request.getServerPort()+
									request.getContextPath()+"/upload/"+middleStr+filename);
						}
						else{
							fileList.add(request.getScheme()+"://"+ request.getServerName()+
									":"+request.getServerPort()+
									request.getContextPath()+"/upload/"+middleStr+filename);
						}
						
					     
					} 
				}  
			}
			resultMap.put("status", "0");
			resultMap.put("data", fileList);
		}
		catch (Exception e){
			resultMap.put("status", "-1");
			resultMap.put("data", "上传失败!");
			e.printStackTrace();
		}  
		return resultMap;  
	} 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String createVarieties(String uploadPath,String excelName){
		String result="";
		try {
			DecimalFormat df = new DecimalFormat("0");    
			String message="";
			ExcelUtil eu =new ExcelUtil();
			eu.setExcelPath(uploadPath+excelName);
			List<Row> inList=eu.readExcel();
			List<Row> outList=new ArrayList<Row>();
			if(inList.size()>0){
				Row row=inList.get(0);
				row.createCell((short) 9).setCellValue("导入结果");  
				outList.add(row);
			}
			for(int index=1;index<inList.size();index++){
				Row row =inList.get(index);
				try {
					if(eu.getCellValue(row.getCell(1)).equals("")){
						message = "品种名称不能为空";
					}
					else if(eu.getCellValue(row.getCell(7)).equals("")){
						message = "创建人不能为空";
					}
					else{
						Map paramMap= new HashMap();
						paramMap.put("fromPage",0);
						paramMap.put("toPage",1); 
						paramMap.put("production", eu.getCellValue(row.getCell(5)).toString());
						List<Prodnum> list=iProdnumService.selectProdnumByParam(paramMap);
						if(list.size()>0){
							Varieties temp = new Varieties();
							temp.setVariety(eu.getCellValue(row.getCell(1)).toString());
							temp.setYield(eu.getCellValue(row.getCell(2)).toString());
							temp.setRhythm(eu.getCellValue(row.getCell(3)).toString());
							temp.setItemtime(eu.getCellValue(row.getCell(4)).toString());
							temp.setProdnum(list.get(0).getId()+"");
							temp.setProduction(eu.getCellValue(row.getCell(5)).toString());
							
							temp.setCapacity(String.valueOf(df.format(row.getCell(6).getNumericCellValue())));
							temp.setChangtime(eu.getCellValue(row.getCell(7)).toString());
							temp.setCreater(eu.getCellValue(row.getCell(8)).toString());
							int resultSample =iVarietiesService.addVarieties(temp);
							if(resultSample>0){
								message = "成功";
							}
							else{
								message = "失败";
							}
						}
						else{
							message = "系统内不存在该生产线";
						}
						
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					message = "失败";
					e.printStackTrace();
				}
				
				row.createCell((short) 9).setCellValue(message);  
			  
				outList.add(row);
			}
			String[] fileNames=excelName.split("\\.");
			 
			String src_xlsPath=uploadPath+fileNames[0]+"_result."+fileNames[1];
		 
			eu.writeExcel(outList, src_xlsPath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}  
		return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String createTimer(String uploadPath,String excelName){
		String result="";
		try {
			String message="";
			ExcelUtil eu =new ExcelUtil();
			eu.setExcelPath(uploadPath+excelName);
			List<Row> inList=eu.readExcel();
			List<Row> outList=new ArrayList<Row>();
			if(inList.size()>0){
				Row row=inList.get(0);
				row.createCell((short)5).setCellValue("导入结果");  
				outList.add(row);
			}
			for(int index=1;index<inList.size();index++){
				Row row =inList.get(index);
				try {
					String time=eu.getCellValue(row.getCell(3)).toString();
					String[] times=time.split("-");
					if(eu.getCellValue(row.getCell(1)).equals("")){
						message = "生产线不能为空";
					}
					else if(eu.getCellValue(row.getCell(2)).equals("")){
						message = "时间类型不能为空";
					}
					else if(eu.getCellValue(row.getCell(4)).equals("")){
						message = "添加人不能为空";
					}
					else if(times.length<2){
						message = "时间格式不正确";
					}
					else{
						
						
						Map paramMap= new HashMap();
						paramMap.put("fromPage",0);
						paramMap.put("toPage",1); 
						paramMap.put("production", eu.getCellValue(row.getCell(1)).toString());
						List<Prodnum> list=iProdnumService.selectProdnumByParam(paramMap);
						if(list.size()>0){
							Timer temp = new Timer();
							temp.setProdnum(list.get(0).getId()+"");
							temp.setType(eu.getCellValue(row.getCell(2)).toString());
							temp.setStarttime(times[0]);
							temp.setEndtime(times[1]);
							temp.setCreater(eu.getCellValue(row.getCell(4)).toString());
							int resultSample =iTimerService.addTimer(temp);
							if(resultSample>0){
								message = "成功";
							}
							else{
								message = "失败";
							}
						}
						else{
							message = "系统内不存在该生产线";
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					message = "失败";
					e.printStackTrace();
				}
				
				row.createCell((short)5).setCellValue(message);  
			  
				outList.add(row);
			}
			String[] fileNames=excelName.split("\\.");
			 
			String src_xlsPath=uploadPath+fileNames[0]+"_result."+fileNames[1];
		 
			eu.writeExcel(outList, src_xlsPath);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}  
		return result;
	}
	
}
