package com.baosight.buapx.ua.log.api;

import java.util.Date;

import com.baosight.buapx.ua.log.constants.LoginType;
import com.baosight.buapx.ua.log.domain.LoginLog;

public class LoginLogFactory {
  private static LoginLogFactory factory=new LoginLogFactory();
  private LoginLogFactory(){
	  
  }
  
  public static LoginLogFactory getInstance(){
	  return factory;
  }
  
  public LoginLog createLoginSuccessLog(String casUserid,String loginName,String displayName,String loginIp,String desc,String userType){
	  LoginLog log=new LoginLog();
	 
	  log.setCasUserid(casUserid);
	  log.setLoginName(loginName);
	  log.setLoginType(LoginType.LOGIN_SUCCESS);
	  log.setLoginTime(new Date());
	  log.setDisplayName(displayName);
	  log.setDesc(desc==null?"":desc);
	  log.setLoginIp(loginIp==null?"":loginIp);
	  log.setUserType(userType);
	  return log;
  }
  
  public LoginLog createLoginFailLog(String casUserid,String loginName,String displayName,String loginIp,String desc,String userType){
	  LoginLog log=new LoginLog();
	 
	  log.setCasUserid(casUserid);
	  log.setLoginName(loginName);
	  log.setLoginType(LoginType.LOGIN_FAIL);
	  log.setLoginTime(new Date());
	  log.setDisplayName(displayName);
	  log.setDesc(desc==null?"":desc);
	  log.setLoginIp(loginIp==null?"":loginIp);
	  log.setUserType(userType);
	  return log;
  }
  
  public LoginLog createLogoutLog(String casUserId,String displayName,String loginIp,String desc,String userType){
	  LoginLog log=new LoginLog();
	  log.setDesc(desc==null?"":desc);
	  log.setLoginIp(loginIp==null?"":loginIp);
	  log.setCasUserid(casUserId);
	  log.setLoginName(casUserId);
	  log.setLoginType(LoginType.LOGOUT);
	  log.setLoginTime(new Date());
	  log.setDisplayName(displayName);
	  log.setUserType(userType);
	  return log;
  }

  
  
  public LoginLog createRedirectLog(String casUserId,String displayName,String targetSystem,String desc,String userType){
	  LoginLog log=new LoginLog();
	  log.setDesc(desc==null?"":desc);
	  log.setCasUserid(casUserId);
	  log.setLoginName(casUserId);
	  log.setTargetSystem(targetSystem);
	  log.setLoginType(LoginType.REDIRECT);
	  log.setLoginTime(new Date());
	  log.setDisplayName(displayName);
	  log.setUserType(userType);
	  return log;
  }
}
