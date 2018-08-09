package com.wheelshop.service.production;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.production.Production;
public interface IProductionService {
	/**
	* 通过id选取
	* @return
	*/
	public Production selectProductionById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Production> selectProductionByParam(Map paramMap); 
	@SuppressWarnings("rawtypes")
	public List<Production> selectProductionByParam2(Map paramMap); 
	@SuppressWarnings("rawtypes")
	public List<Production> selectproductionInIds(String ids); 
	@SuppressWarnings("rawtypes")
	public List<Production> selectAllProductionByParam(Map paramMap); 
	@SuppressWarnings("rawtypes")
	public List<Production> selectStatisticproductionByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountProductionByParam(Map paramMap); 
	@SuppressWarnings("rawtypes")
	public int selectCountProductionByParam2(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateProduction(Production production);

	/**
	* 添加 
	* @return
	*/ 
	public int addProduction(Production production);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdProduction(List<Production> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteProduction(String id);

}

