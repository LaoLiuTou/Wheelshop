package com.wheelshop.service.varieties;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.varieties.Varieties;
public interface IVarietiesService {
	/**
	* 通过id选取
	* @return
	*/
	public Varieties selectVarietiesById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Varieties> selectVarietiesByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountVarietiesByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateVarieties(Varieties varieties);

	/**
	* 添加 
	* @return
	*/ 
	public int addVarieties(Varieties varieties);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdVarieties(List<Varieties> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteVarieties(String id);

}

