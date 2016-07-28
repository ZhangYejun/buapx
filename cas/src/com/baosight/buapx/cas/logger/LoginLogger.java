package com.baosight.buapx.cas.logger;

 

import org.apache.log4j.Logger;

import com.baosight.buapx.ua.log.api.LoginLogFactory;

/**
 * use to distinguish the syslog from other log,which define by the class of
 * syslog appender
 * 
 * @author lizidi@baosight.com
 * 
 */
public class LoginLogger {
	protected static Logger logger = Logger.getLogger(LoginLogger.class);
	protected static Logger mongoLog = Logger.getLogger(LoginLogger.class);

	public static String loginSuccessLog(String casuserid, String loginname,String ip,
			String hardwareInfo, String description,String userType) {
		String result = loginSuccessLog2file(casuserid,loginname, ip, hardwareInfo, description);//记录文件
		loginSuccessLog2mongodb(casuserid, loginname,ip, hardwareInfo, description,userType);//记录mongo
		return result;
	}

	private static String loginSuccessLog2file(String casuserid, String loginname,  String ip,
			String hardwareInfo, String description) {
		StringBuffer log = new StringBuffer("认证:登录成功");
		log.append("  用户名:" + loginname);
		log.append("  主代码:" + casuserid);
		log.append("  姓名:");
		log.append("  接入系统:");
		log.append("  IP:" + ip);
		log.append("  硬件信息:" + hardwareInfo);
		log.append("  描述:" + description);
		String result = log.toString();
		if (logger.isInfoEnabled()) {
			logger.info(result);
		}

		return result;
	}
	
	private static  void loginSuccessLog2mongodb(String casuserid, String loginname,String ip,
			String hardwareInfo, String description,String userType) {
		if (mongoLog.isInfoEnabled()) {
			mongoLog.info( LoginLogFactory.getInstance().createLoginSuccessLog(casuserid,loginname, casuserid,ip, description,userType));
		}
		
	}

	public static String loginFailureLog(String casuserid,String loginname, String ip,
			String hardwareInfo, String description,String userType) {
		String result = loginFailureLog2file(casuserid,loginname, ip, hardwareInfo, description);
		loginFailureLog2mongo(casuserid,loginname, ip, hardwareInfo, description,userType);
		return result;
	}

	private static String loginFailureLog2file(String casuserid,String loginname, String ip,
			String hardwareInfo, String description) {
		StringBuffer log = new StringBuffer("认证:登录失败");
		log.append("  用户名:" + loginname);
		log.append("  主代码:" + casuserid);
		log.append("  姓名:");
		log.append("  接入系统:");
		log.append("  IP:" + ip);
		log.append("  硬件信息:" + hardwareInfo);
		log.append("  描述:" + description);
		String result = log.toString();
		if (logger.isInfoEnabled()) {
			logger.info(result);
		}
		return result;
	}
	
	private static void loginFailureLog2mongo(String casuserid, String loginname,String ip,
			String hardwareInfo, String description,String userType) {
		if (mongoLog.isInfoEnabled()) {
			mongoLog.info( LoginLogFactory.getInstance().createLoginFailLog(casuserid,loginname,casuserid, ip, description,userType));
		}
	}

	public static String logoutSuccessLog(String casuserid, String ip,
			String hardwareInfo, String description,String userType) {
		String result = logoutSuccessLog2file(casuserid, ip, hardwareInfo, description);
		logoutSuccessLog2mongo(casuserid, ip, hardwareInfo, description,userType);
		return result;
	}

	private static String logoutSuccessLog2file(String casuserid, String ip,
			String hardwareInfo, String description) {
		StringBuffer log = new StringBuffer("认证:用户登出");
		log.append("  用户名:" + casuserid);
		log.append("  主代码:" + casuserid);
		log.append("  姓名:");
		log.append("  接入系统:");
		log.append("  IP:" + ip);
		log.append("  硬件信息:" + hardwareInfo);
		log.append("  描述:" + description);
		String result = log.toString();
		if (logger.isInfoEnabled()) {
			logger.info(result);
		}
		return result;
	}
	
	private static void logoutSuccessLog2mongo(String casuserid, String ip,
			String hardwareInfo, String description,String userType) {
		if (mongoLog.isInfoEnabled()) {
			mongoLog.info( LoginLogFactory.getInstance().createLogoutLog(casuserid,casuserid, ip, description,userType));
		}
	}

	public static String redirectSystemLog(String casuserid, String sysCode,
			String description,String userType) {
		
		String result= redirectSystemLog2file(casuserid, sysCode, description);
		redirectSystemLog2mongo(casuserid, sysCode, description,userType);
		
		return result;
	}

	private static String redirectSystemLog2file(String casuserid, String sysCode,
			String description) {
		StringBuffer log = new StringBuffer("认证:用户跳转");
		log.append("  用户名:" + casuserid);
		log.append("  主代码:" + casuserid);
		log.append("  姓名:");
		log.append("  接入系统:" + sysCode);
		log.append("  IP:");
		log.append("  硬件信息:");
		log.append("  描述:" + description);
		String result = log.toString();
		if (logger.isInfoEnabled()) {
			logger.info(result);
		}
		return result;
	}
	
	private static void redirectSystemLog2mongo(String casuserid, String sysCode,
			String description,String userType) {
		if (mongoLog.isInfoEnabled()) {
			mongoLog.info( LoginLogFactory.getInstance().createRedirectLog(casuserid,casuserid, sysCode, description,userType));
		}
	}
}
