package com.wheelshop.dao.production;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.production.Production;
	public interface IProductionMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Production selectproductionById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Production> selectproductionByParam(Map paramMap); 
 @SuppressWarnings("rawtypes")
 	public List<Production> selectallproductionByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountproductionByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updateproduction(Production production);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addproduction(Production production);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdproduction(List<Production> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deleteproduction(String id);

}

