package com.wheelshop.dao.device;
import java.util.List;
import java.util.Map;
import com.wheelshop.model.device.Device;
	public interface IDeviceMapper {
	/**
 	* 通过id选取
 	* @return
 	*/
	public Device selectdeviceById(String id);
	/**
 	* 通过查询参数获取信息
 	* @return
 */ 
 @SuppressWarnings("rawtypes")
	public List<Device> selectdeviceByParam(Map paramMap); 
	/**
		* 通过查询参数获取总条数
	    * @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountdeviceByParam(Map paramMap); 
	/**
 	* 更新 
 	* @return 
 */ 
	public  int updatedevice(Device device);
	/**
 	* 添加 
 	* @return
 */ 
	public  int adddevice(Device device);
	/**
 	* 批量添加 
 	* @return
 */ 
	public  int muladddevice(List<Device> list);
	/**
 	* 删除 
 	* @return 
 */ 
public  int deletedevice(String id);

}

