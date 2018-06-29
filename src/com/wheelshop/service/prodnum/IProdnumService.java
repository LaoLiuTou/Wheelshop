package com.wheelshop.service.prodnum;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.prodnum.Prodnum;
public interface IProdnumService {
	/**
	* 通过id选取
	* @return
	*/
	public Prodnum selectProdnumById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Prodnum> selectProdnumByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountProdnumByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateProdnum(Prodnum prodnum);

	/**
	* 添加 
	* @return
	*/ 
	public int addProdnum(Prodnum prodnum);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdProdnum(List<Prodnum> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteProdnum(String id);

}

