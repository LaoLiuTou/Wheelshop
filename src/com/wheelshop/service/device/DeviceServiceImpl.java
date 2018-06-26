package com.wheelshop.service.device;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.wheelshop.dao.device.IDeviceMapper;
import com.wheelshop.model.device.Device;
public class DeviceServiceImpl  implements IDeviceService {

	@Autowired
	private IDeviceMapper iDeviceMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Device selectDeviceById(String id){
		return iDeviceMapper.selectdeviceById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Device> selectDeviceByParam(Map paramMap){ 
		return iDeviceMapper.selectdeviceByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountDeviceByParam(Map paramMap){ 
		return iDeviceMapper.selectCountdeviceByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateDevice(Device device){
		return iDeviceMapper.updatedevice(device);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addDevice(Device device){
		return iDeviceMapper.adddevice(device);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdDevice(List<Device> list){
		return iDeviceMapper.muladddevice(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteDevice(String id){
		return iDeviceMapper.deletedevice(id);
	}

}

