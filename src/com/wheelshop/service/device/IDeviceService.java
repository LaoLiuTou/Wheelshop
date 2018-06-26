package com.wheelshop.service.device;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.device.Device;
public interface IDeviceService {
	/**
	* 通过id选取
	* @return
	*/
	public Device selectDeviceById(String id);

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Device> selectDeviceByParam(Map paramMap); 

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountDeviceByParam(Map paramMap); 

	/**
	* 更新 
	* @return 
	*/ 
	public int updateDevice(Device device);

	/**
	* 添加 
	* @return
	*/ 
	public int addDevice(Device device);

	/**
	* 批量添加 
	* @return
	*/ 
	public int muladdDevice(List<Device> list);

	/**
	* 删除 
	* @return 
	*/ 
	public int deleteDevice(String id);

}

