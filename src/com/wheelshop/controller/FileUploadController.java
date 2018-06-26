package com.wheelshop.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
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
				fileList.add(request.getScheme()+"://"+ request.getServerName()+
						":"+request.getServerPort()+
						request.getContextPath()+"/upload/"+middleStr+filename);
				} 
			}  
		}
		resultMap.put("status", "0");
		resultMap.put("data", fileList);
	}
	catch (Exception e)
	{
		resultMap.put("status", "-1");
		resultMap.put("data", "上传失败!");
		e.printStackTrace();
	}  
	return resultMap;  
	} 
}
