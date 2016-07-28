/**
 *
 */
package com.baosight.buapx.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.baosight.buapx.cas.authentication.principal.BuapxWebServiceTicketServer;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.common.EncryptUtils;

/**
 * @author lizidi@baosight.com
 *         2012-9-20
 */
public class ValidatePasswordController extends AbstractController {

	private static final String SUCCESS_VIEW_PAGE = "buapxPasswordSuccessView";

	private static final String FAILURE_VIEW_PAGE = "buapxPasswordFailureView";
	/** Instance of CentralAuthenticationService. */

	IAuthManager authManager;
	 private String activeUrl;


	public void setActiveUrl(String activeUrl) {
		this.activeUrl = activeUrl;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String sysCode=request.getParameter("syscode");
		try {


			if (StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
				 
				return generateErrorView("-1", "用户名或者密码为空", null);
			}

			String casUser=authManager.queryUserByLoginAlias(username);
			boolean isAuth=authManager.hasPermissionToCas(casUser,EncryptUtils.passwordEncode(password));
			String result=null;
			//暂时不判断激活状态
			/*if(isAuth){
				if(!authManager.isUserActived(casUser)){
					result="notActived"+",";
				}
			}
			if(result==null){
				result=isAuth+",";
			}*/
			result = isAuth+","+casUser;

			result=result+","+authManager.querySubAccount(casUser, sysCode);
			ModelAndView success = new ModelAndView(this.SUCCESS_VIEW_PAGE);
			success.addObject("result",result);
			return success;
		} catch (Exception ex) {
			return generateErrorView("-1", ex.getMessage(), null);
		}

	}

	private ModelAndView generateErrorView(final String code, final String description, final Object[] args) {
		final ModelAndView modelAndView = new ModelAndView(this.FAILURE_VIEW_PAGE);
		final String convertedDescription = getMessageSourceAccessor().getMessage(description, args, description);
		modelAndView.addObject("code", code);
		modelAndView.addObject("description", convertedDescription);
		return modelAndView;
	}

}
