package com.baosight.buapx.ua.log.domain;

/**
 * 登录日志,包括登录成功，登录失败，跳转系统，用户登出。
 */
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baosight.buapx.log.domain.IDomainLog;

public class LoginLog implements IDomainLog, LoggerBase {
	/*
	 * 日志时间
	 */
	private Date loginTime;
	/*
	 * 登录类型,包括登录成功，登录失败，跳转系统，用户登出。
	 */
	private String loginType;

	/*
	 * 主代码ID
	 */
	private String casUserid = "";
	/*
	 * 登录用户ID,记录登录别名
	 */
	private String loginName = "";
	/*
	 * 登录用户姓名
	 */
	private String displayName = "";
	/*
	 * 登录用户Ip
	 */
	private String loginIp = "";
	/*
	 * 用户单点登录某系统时，该系统的系统编码
	 */
	private String targetSystem = "";
	/*
	 * 描述
	 */
	private String desc = "";
	/**
	 * 用户类型
	 */
	private String userType = "";

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
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

	public String getCasUserid() {
		return casUserid;
	}

	public void setCasUserid(String casUserid) {
		this.casUserid = casUserid;
	}
	@Override
	public Map domainToMap() {
		Map map = new HashMap();
		map.put("desc", this.getDesc());
		map.put("displayName", this.getDisplayName());
		map.put("loginIp", this.getLoginIp());
		map.put("loginName", this.getLoginName());
		map.put("loginTime", this.getLoginTime());
		map.put("loginType", this.getLoginType());
		map.put("targetSystem", this.getTargetSystem());
		map.put("casUserid", this.getCasUserid());
		map.put("userType", this.getUserType());
		return map;
	}

	

}
