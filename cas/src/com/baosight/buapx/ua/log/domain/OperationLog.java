package com.baosight.buapx.ua.log.domain;
/**
 * 操作日志,包括密码修改等。
 */
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baosight.buapx.log.domain.IDomainLog;

public class OperationLog implements IDomainLog,LoggerBase {
	/*
	 * 操作时间
	 */
	private Date operationTime;
    /*
     * 操作类型
     */
	private String operationType;	
	/*
	 * 操作明细
	 */
	private String operationDetail;
	/*
	 * 操作者ID
	 */
	private String operator="";
	/*
	 *被操作者ID 
	 */
	private String targetUser="";
	
	/*
	 * 被操作者用户姓名
	 */
	private String targetUserName="";
	/*
	 * 操作者IP
	 */
	private String operatorIp="";
	
	/*
	 * 目标系统，部分操作与接入系统相关(如子账号)
	 */
	private String targetSystem="";
	/*
	 * 描述
	 */
	private String desc="";
	
	public Date getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}
	


	public String getTargetUserName() {
		return targetUserName;
	}
	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getOperatorIp() {
		return operatorIp;
	}
	public void setOperatorIp(String operatorIp) {
		this.operatorIp = operatorIp;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getTargetSystem() {
		return targetSystem;
	}
	public void setTargetSystem(String targetSystem) {
		this.targetSystem = targetSystem;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperationDetail() {
		return operationDetail;
	}
	public void setOperationDetail(String operationDetail) {
		this.operationDetail = operationDetail;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
	}
	@Override
	public Map domainToMap() {
		Map map=new HashMap();
		map.put("desc", this.getDesc());
		map.put("operationDetail", this.getOperationDetail());
		map.put("operationTime", this.getOperationTime());
		map.put("operator", this.getOperator());
		map.put("operatorIp", this.getOperatorIp());
		map.put("targetSystem", this.getTargetSystem());
		map.put("targetUser", this.getTargetUser());
		map.put("operationType", this.getOperationType());
		map.put("targetUserName", this.getTargetUserName());
		return map;
	}

	
	
}
