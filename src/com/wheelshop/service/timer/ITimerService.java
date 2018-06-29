package com.wheelshop.service.timer;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.timer.Timer;
public interface ITimerService {
	/**
	* 通过id选取
	* @return
	*/
	public Timer selectTimerById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Timer> selectTimerByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountTimerByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateTimer(Timer timer);

	/**
	* 添加 
	* @return
	*/ 
	public int addTimer(Timer timer);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdTimer(List<Timer> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteTimer(String id);

}

