package com.baosight.buapx.security.userdetails;

import java.io.Serializable;

/**
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-12-27
 */
public class SecurityUserInfo  implements Serializable {
	/**
	 *
	 */

	private static final long serialVersionUID = -1648911781590843152L;
	public long getLoginTime() {
		return loginTime;
	}
	public String getUserName() {
		return userName;
	}

	public String getCasUser() {
		return casUser;
	}


	public String getUserType() {
		return userType;
	}


	private final String userName;
	private final long loginTime;
	private final String casUser;
	private final String userType;

	public SecurityUserInfo(final String userName,final String casUser,final String userType) {
		this.userName = userName;
		this.loginTime = System.currentTimeMillis();
		this.casUser=casUser;
		this.userType=userType;
	}
}
