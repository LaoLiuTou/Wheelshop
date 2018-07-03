package com.wheelshop.service.dstate;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.dstate.Dstate;
public interface IDstateService {
	/**
	* 通过id选取
	* @return
	*/
	public Dstate selectDstateById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Dstate> selectDstateByParam(Map paramMap); 
	@SuppressWarnings("rawtypes")
	public List<Dstate> selectAllDstateByParam(Map paramMap); 
	@SuppressWarnings("rawtypes")
	public List<Dstate> selectStatisticdstateByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountDstateByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateDstate(Dstate dstate);

	/**
	* 添加 
	* @return
	*/ 
	public int addDstate(Dstate dstate);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdDstate(List<Dstate> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteDstate(String id);

}

