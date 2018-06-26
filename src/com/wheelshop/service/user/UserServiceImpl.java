package com.wheelshop.service.user;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.wheelshop.dao.user.IUserMapper;
import com.wheelshop.model.user.User;
public class UserServiceImpl  implements IUserService {

	@Autowired
	private IUserMapper iUserMapper;
	/**
	* 通过id选取
	* @return
	*/
	public User selectUserById(String id){
		return iUserMapper.selectuserById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<User> selectUserByParam(Map paramMap){ 
		return iUserMapper.selectuserByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountUserByParam(Map paramMap){ 
		return iUserMapper.selectCountuserByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateUser(User user){
		return iUserMapper.updateuser(user);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addUser(User user){
		return iUserMapper.adduser(user);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdUser(List<User> list){
		return iUserMapper.muladduser(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteUser(String id){
		return iUserMapper.deleteuser(id);
	}

}

