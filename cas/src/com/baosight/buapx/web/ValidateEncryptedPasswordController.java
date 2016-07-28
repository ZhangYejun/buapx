/**
 *
 */
package com.baosight.buapx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.baosight.buapx.common.TDESUtil;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.common.EncryptUtils;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;

/**
 * @author ecocoa 2015-09-16
 * 
 */
public class ValidateEncryptedPasswordController extends AbstractController {

	private static final String SUCCESS_VIEW_PAGE = "buapxEncPasswordSuccessView";
	private String secKey;

	private IAuthManager authManager;

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	public String getSecKey() {
		return secKey;
	}

	public void setSecKey(String secKey) {
		this.secKey = secKey;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {

			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				return generateErrorView("用户名或者密码为空");
			}

			String casUser = authManager.queryUserByLoginAlias(username);
			AuthUserInfo userInfo = authManager.queryUserInfo(casUser);

			if (userInfo == null) {
				return generateSuccessView(false, "用户不存在", casUser);
			} else {

				String plainPassword = TDESUtil.decrypt(this.secKey, password);

				String codedPassword = EncryptUtils
						.passwordEncode(plainPassword);

				boolean result = authManager.hasPermissionToCas(userInfo
						.getUserid(), codedPassword);

				return generateSuccessView(result, "执行认证成功", casUser);

			}
		} catch (Exception ex) {
			return generateErrorView(ex.getMessage());

		}

	}

	private ModelAndView generateErrorView(final String description) {
		final ModelAndView modelAndView = new ModelAndView(
				this.SUCCESS_VIEW_PAGE);
		modelAndView.addObject("result", false);
		modelAndView.addObject("success", false);
		modelAndView.addObject("description", description);

		return modelAndView;
	}

	private ModelAndView generateSuccessView(final boolean result,
			final String description, String casUser) {
		final ModelAndView modelAndView = new ModelAndView(
				this.SUCCESS_VIEW_PAGE);
		modelAndView.addObject("result", result);
		modelAndView.addObject("success", true);
		modelAndView.addObject("description", description);
		modelAndView.addObject("casUser", casUser);
		return modelAndView;
	}

}
