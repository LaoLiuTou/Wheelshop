package com.wheelshop.dao.timer;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.timer.Timer;
	public interface ITimerMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Timer selecttimerById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Timer> selecttimerByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCounttimerByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatetimer(Timer timer);
	/**
 	* 添加 
 	* @return
 */ 
	public  int addtimer(Timer timer);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladdtimer(List<Timer> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletetimer(String id);

}

