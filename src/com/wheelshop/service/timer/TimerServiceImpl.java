package com.wheelshop.service.timer;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.wheelshop.dao.timer.ITimerMapper;
import com.wheelshop.model.timer.Timer;
public class TimerServiceImpl  implements ITimerService {

	@Autowired
	private ITimerMapper iTimerMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Timer selectTimerById(String id){
		return iTimerMapper.selecttimerById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Timer> selectTimerByParam(Map paramMap){ 
		return iTimerMapper.selecttimerByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountTimerByParam(Map paramMap){ 
		return iTimerMapper.selectCounttimerByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateTimer(Timer timer){
		return iTimerMapper.updatetimer(timer);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addTimer(Timer timer){
		return iTimerMapper.addtimer(timer);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdTimer(List<Timer> list){
		return iTimerMapper.muladdtimer(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteTimer(String id){
		return iTimerMapper.deletetimer(id);
	}

}

