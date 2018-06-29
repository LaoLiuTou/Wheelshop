package com.wheelshop.dao.prodnum;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.prodnum.Prodnum;
	public interface IProdnumMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Prodnum selectprodnumById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Prodnum> selectprodnumByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountprodnumByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updateprodnum(Prodnum prodnum);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addprodnum(Prodnum prodnum);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdprodnum(List<Prodnum> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deleteprodnum(String id);

}

