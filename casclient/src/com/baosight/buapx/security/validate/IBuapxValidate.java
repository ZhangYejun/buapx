package com.baosight.buapx.security.validate;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.baosight.buapx.security.exception.BuapxAuthException;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;
public interface IBuapxValidate {
	
	public SecurityUserInfo auth(HttpServletRequest request, HttpServletResponse response) throws BuapxAuthException;
	public void init(FilterConfig config);

}
