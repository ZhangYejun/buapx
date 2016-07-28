package com.baosight.buapx.ua.log.domain;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;

import com.baosight.buapx.log.domain.IDomainLog;

public class TransferLog implements IDomainLog,LoggerBase {
	/**
	 * 传输是否成功
	 */
	private boolean success=true;
	/*
	 * 操作时间
	 */
	private Date transferTime;
    /*
     * 传输类型
     */
	private String transferType;
	
   /*
    * 同步的数据 
    */
	private Map<String,Object> transferData;
	/*
	 * 描述
	 */
	private String desc="";
	
	public Date getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	
	
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	
	public Map<String, Object> getTransferData() {
		return transferData;
	}
	public void setTransferData(Map<String, Object> transferData) {
		this.transferData = transferData;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	@Override
	public Map domainToMap() {
		Map map=new HashedMap();
		map.put("desc", this.getDesc());
		map.put("transferTime", this.getTransferTime());
		map.put("transferType", this.getTransferType());
		map.put("transferData", this.getTransferData());
		map.put("success", this.isSuccess());
		return map;
	}
	
	

	
}
