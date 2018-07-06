package com.wheelshop.model.varieties;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author LT
 */
public class Varieties {

	/** 品种 */
	private  Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/** 品种 */
	private  String variety;
	public String getVariety() {
		return variety;
	}
	public void setVariety(String variety) {
		this.variety = variety;
	}
	/** 计划产量 */
	private  String yield;
	public String getYield() {
		return yield;
	}
	public void setYield(String yield) {
		this.yield = yield;
	}
	/** 节拍 */
	private  String rhythm;
	public String getRhythm() {
		return rhythm;
	}
	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}
	/** 生产线编号 */
	private  String prodnum;
	
	public String getProdnum() {
		return prodnum;
	}
	public void setProdnum(String prodnum) {
		this.prodnum = prodnum;
	}
	/** 生产线 */
	private  String production;
	public String getProduction() {
		return production;
	}
	public void setProduction(String production) {
		this.production = production;
	}
	/** 产能 */
	private  String capacity;
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	/** 换模时间 */
	private  String changtime;
	public String getChangtime() {
		return changtime;
	}
	public void setChangtime(String changtime) {
		this.changtime = changtime;
	}
	/** 所需设备 */
	private  String required;
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	/** 添加人 */
	private  String creater;
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
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
	/** 类型 */
	private  String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**  */
	private  Long flag;
	public Long getFlag() {
		return flag;
	}
	public void setFlag(Long flag) {
		this.flag = flag;
	}



}
