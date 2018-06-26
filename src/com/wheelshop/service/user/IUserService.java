package com.wheelshop.service.user;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.user.User;
public interface IUserService {
	/**
	* 通过id选取
	* @return
	*/
	public User selectUserById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<User> selectUserByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountUserByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateUser(User user);

	/**
	* 添加 
	* @return
	*/ 
	public int addUser(User user);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdUser(List<User> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteUser(String id);

}

