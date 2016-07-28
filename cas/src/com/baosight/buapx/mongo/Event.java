package com.baosight.buapx.mongo;

import java.io.Serializable;

public class Event implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7418083643568522831L;
	private String _id; 
	private String endtime; 
	private String dversion; 
	private String dmf; 	
	private String starttime; 	
	private String desc; 
	private String deviceip; 
	private int count; 
	private String dname;
	private String type;
	private String engineid;
	private String username;
	private String dip;
	private String level;
	private String event;
	private String dtype;
	private String original;

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getDversion() {
		return dversion;
	}
	public void setDversion(String dversion) {
		this.dversion = dversion;
	}
	public String getDmf() {
		return dmf;
	}
	public void setDmf(String dmf) {
		this.dmf = dmf;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDeviceip() {
		return deviceip;
	}
	public void setDeviceip(String deviceip) {
		this.deviceip = deviceip;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEngineid() {
		return engineid;
	}
	public void setEngineid(String engineid) {
		this.engineid = engineid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDip() {
		return dip;
	}
	public void setDip(String dip) {
		this.dip = dip;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	} 

}
