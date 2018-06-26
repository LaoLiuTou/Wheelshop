package com.wheelshop.dao.varieties;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.varieties.Varieties;
	public interface IVarietiesMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Varieties selectvarietiesById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Varieties> selectvarietiesByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountvarietiesByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatevarieties(Varieties varieties);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addvarieties(Varieties varieties);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdvarieties(List<Varieties> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletevarieties(String id);

}

