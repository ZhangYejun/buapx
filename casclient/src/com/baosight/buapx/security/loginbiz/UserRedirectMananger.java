package com.baosight.buapx.security.loginbiz;

import javax.servlet.http.HttpServletRequest;

public interface UserRedirectMananger {
   
	public String constructRedirectUrl(HttpServletRequest request,String userid,String defaultUrl);
}
