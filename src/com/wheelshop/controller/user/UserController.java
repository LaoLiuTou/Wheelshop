package com.wheelshop.controller.user;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.wheelshop.model.user.User;
import com.wheelshop.service.user.IUserService;
import com.wheelshop.utils.MD5Encryption;
import com.wheelshop.utils.TokenUtils;
import com.wheelshop.utils.TokenUtils.TokenBean;
@Controller
public class UserController {
	@Autowired
	private IUserService iUserService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("WheelshopLogger");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/addUser")
	@ResponseBody
	public Map add(User user){
		Map resultMap=new HashMap();
		try {
			iUserService.addUser(user);
			resultMap.put("status", "0");
			resultMap.put("msg", user.getId());
			logger.info("新建成功，主键："+user.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/muladdUser")
	@ResponseBody
	public Map muladd(HttpServletRequest request,User user){
		Map resultMap=new HashMap();
		try {
			String data=request.getParameter("data");
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, User.class);
			List<User> list = (List<User>)objectMapper.readValue(data, javaType);
			iUserService.muladdUser(list);
			resultMap.put("status", "0");
			resultMap.put("msg", "新建成功");
			logger.info("新建成功，主键："+user.getId());
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "新建失败！");
			logger.info("新建失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/deleteUser")
	@ResponseBody
	public Map delete(User user){
		Map resultMap=new HashMap();
		try {
			if(user.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultDelete=iUserService.deleteUser(user.getId()+"");
				resultMap.put("status", "0");
				resultMap.put("msg", "删除成功！");
				logger.info("删除成功，主键："+user.getId());
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
	@RequestMapping("/selectUser")
	@ResponseBody
	public Map select(User user){
		Map resultMap=new HashMap();
		try {
			if(user.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				User resultSelect=iUserService.selectUserById(user.getId()+"");
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
	@RequestMapping("/updateUser")
	@ResponseBody
	public Map update(User user){
		Map resultMap=new HashMap();
		try {
			if(user.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				int resultUpdate=iUserService.updateUser(user);
				resultMap.put("status", "0");
				resultMap.put("msg", "更新成功！");
				logger.info("更新成功，主键："+user.getId());
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
	@RequestMapping("/listUser")
	@ResponseBody
	public Map list(HttpServletRequest request, HttpServletResponse response,User user)
		throws ServletException, IOException {
		Map resultMap=new HashMap();
		try {
			String page=request.getParameter("page");
			String size=request.getParameter("size");
			if(page!=null&&size!=null){
				Map paramMap=new HashMap();
				paramMap.put("fromPage",(Integer.parseInt(page)-1)*Integer.parseInt(size));
				paramMap.put("toPage",Integer.parseInt(size)); 
				paramMap.put("id",user.getId());
				paramMap.put("username",user.getUsername());
				paramMap.put("password",user.getPassword());
				paramMap.put("nickname",user.getNickname());
				paramMap.put("comment",user.getComment());
				paramMap.put("c_id",user.getC_id());
				paramMap.put("state",user.getState());
				String c_dtFrom=request.getParameter("c_dtFrom");
				String c_dtTo=request.getParameter("c_dtTo");
				if(c_dtFrom!=null&&!c_dtFrom.equals(""))
				paramMap.put("c_dtFrom", sdf.parse(c_dtFrom));
				if(c_dtTo!=null&&!c_dtTo.equals(""))
				paramMap.put("c_dtTo", sdf.parse(c_dtTo));
				String u_dtFrom=request.getParameter("u_dtFrom");
				String u_dtTo=request.getParameter("u_dtTo");
				if(u_dtFrom!=null&&!u_dtFrom.equals(""))
				paramMap.put("u_dtFrom", sdf.parse(u_dtFrom));
				if(u_dtTo!=null&&!u_dtTo.equals(""))
				paramMap.put("u_dtTo", sdf.parse(u_dtTo));
				List<User> list=iUserService.selectUserByParam(paramMap);
				int totalnumber=iUserService.selectCountUserByParam(paramMap);
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
	@RequestMapping("/login")
	@ResponseBody
	public Map login(HttpServletRequest request,User user){
		Map resultMap=new HashMap();
		try {
			if(user.getUsername()!=null&&user.getPassword()!=null){
				
				Map paramMap=new HashMap();
				paramMap.put("fromPage",0);
				paramMap.put("toPage",1); 
				paramMap.put("username",user.getUsername());
				paramMap.put("nostate",-1);
				List<User> list=iUserService.selectUserByParam(paramMap);
				if(list.size()>0){
					if(list.get(0).getPassword().equals(MD5Encryption.getEncryption(user.getPassword()).toLowerCase())){
						String userToken= UUID.randomUUID().toString();
						 
						resultMap.put("status", "0");
						resultMap.put("token", userToken);
						resultMap.put("msg", list.get(0));
						logger.info("用户登录："+list.get(0).getUsername());
						TokenUtils.TokenBean tokenBean =new TokenUtils().new TokenBean();
						tokenBean.setTimesamp(System.currentTimeMillis()+"");
						tokenBean.setUsername(list.get(0).getUsername());
						tokenBean.setUserid(list.get(0).getId()+"");
	  					TokenUtils.add(userToken, tokenBean);
					}
					else{
						resultMap.put("status", "-1");
						resultMap.put("msg", "密码错误！");
					}
					
				}
				else{
					resultMap.put("status", "-1");
					resultMap.put("msg", "用户名不存在！");
				}
				
			}
			else{
				resultMap.put("status", "-1");
				resultMap.put("msg", "用户名或密码不能为空！");
			}
			 
			 
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "登录失败！");
			logger.info("登录失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/passwordUser")
	@ResponseBody
	public Map password(User user){
		Map resultMap=new HashMap();
		try {
			if(user.getId()==null){
				resultMap.put("status", "-1");
				resultMap.put("msg", "参数不能为空！");
			}
			else{
				User temp=iUserService.selectUserById(user.getId()+"");
				String pass=MD5Encryption.getEncryption(user.getPassword()).toLowerCase();
				if(!pass.equals(temp.getPassword().toLowerCase())){
					resultMap.put("status", "-1");
					resultMap.put("msg", "密码不一致！");
				}
				else{
					int resultUpdate=iUserService.updateUser(user);
					resultMap.put("status", "0");
					resultMap.put("msg", "更新成功！");
					logger.info("更新成功，主键："+user.getId());
				}
				
			}
		} catch (Exception e) {
			resultMap.put("status", "-1");
			resultMap.put("msg", "更新失败！");
			logger.info("更新失败！"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
}
