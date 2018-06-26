package com.wheelshop.dao.user;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.user.User;
	public interface IUserMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public User selectuserById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<User> selectuserByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountuserByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updateuser(User user);
	/**
 	* 添加 
 	* @return
 */ 
	public  int adduser(User user);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladduser(List<User> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deleteuser(String id);

}

