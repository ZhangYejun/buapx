package com.baosight.buapx.security.handler;
/**
 * lizid@baosight.com
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baosight.buapx.security.userdetails.SecurityUserInfo;

public interface IAuthPostHandler {
	public void handle(HttpServletRequest request,HttpServletResponse response,SecurityUserInfo userInfo, boolean success);

}
