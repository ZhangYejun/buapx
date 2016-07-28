package com.baosight.buapx.security.loginbiz;

import javax.servlet.http.HttpServletRequest;

public class DefaultRedirectManager implements UserRedirectMananger {

	public String constructRedirectUrl(HttpServletRequest request,
			String userid, String defaultUrl) {
		return defaultUrl;
	}

}
