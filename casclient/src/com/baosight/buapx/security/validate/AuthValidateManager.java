package com.baosight.buapx.security.validate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.validation.TicketValidationException;

import com.baosight.buapx.security.common.ConstString;
import com.baosight.buapx.security.common.UrlConstructor;
import com.baosight.buapx.security.exception.BuapxAuthException;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;

public abstract class AuthValidateManager {
	protected Log log = LogFactory.getLog(AuthValidateManager.class);

	protected final String TICKET = "ticket";

	/**
	 * 验证
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws BuapxAuthException 
	 * 
	 */
	public SecurityUserInfo auth(HttpServletRequest request, HttpServletResponse response) throws BuapxAuthException {
		if (log.isDebugEnabled())
			log.debug("回调入口进入，尝试登录认证...");
		try {
			// 试图从当前Session中获得登录信息
			HttpSession session = request.getSession(true);
			SecurityUserInfo assertion = (session != null) ? (SecurityUserInfo) session.getAttribute(ConstString.SESSION_URL) : null;
			if (assertion != null) {
				if (log.isDebugEnabled())
					log.debug("Session中已有登录信息，无需票据验证.");
				return assertion;
			}

			if (log.isDebugEnabled())
				log.debug("Session中无法获得登录信息，尝试进行票据验证...");

			assertion = this.authNow(request);
			session.setAttribute(ConstString.SESSION_URL, assertion);
			return assertion;

		} catch (TicketValidationException e) {
			// 票据验证失败!
			if (log.isDebugEnabled())
				log.debug("票据验证失败!");
			throw new BuapxAuthException("票据验证失败");
		}

	}


	/***
	 * 验证票据
	 * 
	 * @param ticket
	 * @return
	 * @throws TicketValidationException
	 */
	protected abstract SecurityUserInfo authNow(HttpServletRequest request) throws TicketValidationException;

}