package com.wheelshop.model.production;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author LT
 */
public class Production {

	/** 生产时间信息 */
	private  Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/** 生产线编号 */
	private  String prodnum;
	 
	public String getProdnum() {
		return prodnum;
	}
	public void setProdnum(String prodnum) {
		this.prodnum = prodnum;
	}
	/** 生产线名称 */
	private  String production;
	public String getProduction() {
		return production;
	}
	public void setProduction(String production) {
		this.production = production;
	}
	/** 变化点 */
	private  String changed;
	public String getChanged() {
		return changed;
	}
	public void setChanged(String changed) {
		this.changed = changed;
	}
	/** 计划产量 */
	private  String yield;
	public String getYield() {
		return yield;
	}
	public void setYield(String yield) {
		this.yield = yield;
	}
	/** 生产停台 */
	private  String prodstop;
	public String getProdstop() {
		return prodstop;
	}
	public void setProdstop(String prodstop) {
		this.prodstop = prodstop;
	}
	/** 产能 */
	private  String power;
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	/** 综合可动率=实际完成/产能*100% */
	private  String rate;
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	/** 品种 */
	private  String variety;
	public String getVariety() {
		return variety;
	}
	public void setVariety(String variety) {
		this.variety = variety;
	}
	/** 节拍 */
	private  String rhythm;
	public String getRhythm() {
		return rhythm;
	}
	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}
	/** 每件时间 */
	private String itemtime;
	public String getItemtime() {
		return itemtime;
	}
	public void setItemtime(String itemtime) {
		this.itemtime = itemtime;
	}
	/** 所需设备 */
	private  String required;
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	/** 换模时间*/
	private  String changtime;
	 
	public String getChangtime() {
		return changtime;
	}
	public void setChangtime(String changtime) {
		this.changtime = changtime;
	}
	/** 换模开始时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private  Date startctime;
	public Date getStartctime() {
		return startctime;
	}
	public void setStartctime(Date startctime) {
		this.startctime = startctime;
	}
	/** 计划完成 */
	private  String plancomp;
	public String getPlancomp() {
		return plancomp;
	}
	public void setPlancomp(String plancomp) {
		this.plancomp = plancomp;
	}
	/** 设备停台 */
	private  String equipstop;
	public String getEquipstop() {
		return equipstop;
	}
	public void setEquipstop(String equipstop) {
		this.equipstop = equipstop;
	}
	/** 开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private  Date starttime;
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	/** 结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date endtime;
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	/** 开始状态 */
	private String startstatus;
	public String getStartstatus() {
		return startstatus;
	}
	public void setStartstatus(String startstatus) {
		this.startstatus = startstatus;
	}
	/** 实际完成 */
	private  String actualcomp;
	public String getActualcomp() {
		return actualcomp;
	}
	public void setActualcomp(String actualcomp) {
		this.actualcomp = actualcomp;
	}
	/** 工装停台 */
	private  String toolstop;
	public String getToolstop() {
		return toolstop;
	}
	public void setToolstop(String toolstop) {
		this.toolstop = toolstop;
	}
	/** 加班时长 */
	private  String overtime;
	public String getOvertime() {
		return overtime;
	}
	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}
	/** 生产状态 */
	private  String prodstate;
	public String getProdstate() {
		return prodstate;
	}
	public void setProdstate(String prodstate) {
		this.prodstate = prodstate;
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
	/**  */
	private  String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	/** 生产时间 */
	private  String prodtime;
	public String getProdtime() {
		return prodtime;
	}
	public void setProdtime(String prodtime) {
		this.prodtime = prodtime;
	}
	/** 停台记录 */
	private  String stops;
	public String getStops() {
		return stops;
	}
	public void setStops(String stops) {
		this.stops = stops;
	}
	 
	private String allactualcomp;
	private String allplancomp;
	public String getAllactualcomp() {
		return allactualcomp;
	}
	public void setAllactualcomp(String allactualcomp) {
		this.allactualcomp = allactualcomp;
	}
	public String getAllplancomp() {
		return allplancomp;
	}
	public void setAllplancomp(String allplancomp) {
		this.allplancomp = allplancomp;
	}
	
	private String ids;
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	/**  班次*/
	private String bc;
	public String getBc() {
		return bc;
	}
	public void setBc(String bc) {
		this.bc = bc;
	}
	
	 
	/**  开机时间*//*
	private Date stime;
	public Date getStime() {
		return stime;
	}
	public void setStime(Date stime) {
		this.stime = stime;
	}
	*/

}
