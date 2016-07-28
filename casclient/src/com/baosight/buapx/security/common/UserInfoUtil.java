package com.baosight.buapx.security.common;

import javax.servlet.http.HttpServletRequest;

import com.baosight.buapx.security.userdetails.SecurityUserInfo;

public class UserInfoUtil {
public static SecurityUserInfo getUserInfo(HttpServletRequest request){
	return (SecurityUserInfo) request.getSession().getAttribute(ConstString.SESSION_URL);
}
}
