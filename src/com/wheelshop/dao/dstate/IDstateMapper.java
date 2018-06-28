package com.wheelshop.dao.dstate;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.dstate.Dstate;
	public interface IDstateMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Dstate selectdstateById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Dstate> selectdstateByParam(Map paramMap); 
 @SuppressWarnings("rawtypes")
 	public List<Dstate> selectalldstateByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountdstateByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatedstate(Dstate dstate);
	/**
 	* 添加 
 	* @return
 */ 
	public  int adddstate(Dstate dstate);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladddstate(List<Dstate> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletedstate(String id);

}

