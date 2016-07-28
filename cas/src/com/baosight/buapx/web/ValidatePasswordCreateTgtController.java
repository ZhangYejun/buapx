/**
 * 
 */
package com.baosight.buapx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.ticket.TicketException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.baosight.buapx.cas.authentication.principal.BuapxUsernamePasswordCredentials;
import com.baosight.buapx.ua.auth.api.IAuthManager;

/**
 * @author lizidi@baosight.com 2013-9-10
 */
public class ValidatePasswordCreateTgtController extends AbstractController {

	private static final String SUCCESS_VIEW_PAGE = "buapxPasswordSuccessView";

	private static final String FAILURE_VIEW_PAGE = "buapxPasswordFailureView";
	/** Instance of CentralAuthenticationService. */

	IAuthManager authManager;

	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	public void setCentralAuthenticationService(
			CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sysCode = request.getParameter("syscode");
		try {
			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				return generateErrorView("-1", "用户名或者密码为空", null);
			}

			String casUser = authManager.queryUserByLoginAlias(username);
			BuapxUsernamePasswordCredentials credentials = new BuapxUsernamePasswordCredentials();
			credentials.setUsername(username);
			credentials.setPassword(password);
			credentials.setPlain(false);
			String tgt = null;
			try {
				tgt = centralAuthenticationService
						.createTicketGrantingTicket(credentials);
			} catch (TicketException e) {
				e.printStackTrace();
			}

			String result = null;
			String  subAccount = null;
			if (  StringUtils.isBlank(sysCode)) {
				subAccount = casUser;
			}else {
				subAccount = authManager.querySubAccount(casUser, sysCode);
			}
			if (!StringUtils.isEmpty(tgt)){
				if(!authManager.isUserActived(casUser)){
					result = "notActived" + ","
					+ subAccount
					+ ","+tgt;
				}else{
					result = true + ","
					+ subAccount
					+ ","+tgt;
				}
			}else{
				result = false + ","
				+ subAccount
				+ ","+tgt;	
			}

			ModelAndView success = new ModelAndView(this.SUCCESS_VIEW_PAGE);
			success.addObject("result", result);
			return success;
		} catch (Exception ex) {
			return generateErrorView("-1", ex.getMessage(), null);
		}

	}

	private ModelAndView generateErrorView(final String code,
			final String description, final Object[] args) {
		final ModelAndView modelAndView = new ModelAndView(
				this.FAILURE_VIEW_PAGE);
		final String convertedDescription = getMessageSourceAccessor()
				.getMessage(description, args, description);
		modelAndView.addObject("code", code);
		modelAndView.addObject("description", convertedDescription);
		return modelAndView;
	}
	
	

}
