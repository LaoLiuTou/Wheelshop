package com.wheelshop.model.device;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author LT
 */
public class Device {

	/** 编号对应关系 */
	private  Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/** 生产线 */
	private  String production;
	public String getProduction() {
		return production;
	}
	public void setProduction(String production) {
		this.production = production;
	}
	/** 设备真实编号 */
	private  String deviceno;
	public String getDeviceno() {
		return deviceno;
	}
	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}
	/** 平台虚拟设备编号 */
	private  String nodeno;
	public String getNodeno() {
		return nodeno;
	}
	public void setNodeno(String nodeno) {
		this.nodeno = nodeno;
	}
	/**  */
	private  String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}



}
