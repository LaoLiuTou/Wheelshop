package com.wheelshop.model.dstate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author LT
 */
public class Dstate {

	/** 设备状态 */
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
	/** 状态码 */
	private  String state;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	/** 持续的时间 */
	private  String duration;
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/** 添加时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private  Date adddate;
	public Date getAdddate() {
		return adddate;
	}
	public void setAdddate(Date adddate) {
		this.adddate = adddate;
	}
	
	/** 备注 */
	private  String comment;
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**  */
	private  String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

	private String devicename;
	public String getDevicename() {
		return devicename;
	}
	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
	 
	


}
