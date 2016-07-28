package com.baosight.buapx.log.example;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

import com.baosight.buapx.log.domain.IDomainLog;

public class UserInfo implements IDomainLog, Serializable {
 /**
	 * 
	 */
private static final long serialVersionUID = -6365489685495097860L;
private String name="lizidi";
private Date time=new Date();

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}



public Date getTime() {
	return time;
}

public void setTime(Date time) {
	this.time = time;
}

public static void main(String args[]){
	Logger logger=Logger.getLogger(UserInfo.class);
	logger.warn(new UserInfo());
	System.out.println("日志调用结束");
}
}
