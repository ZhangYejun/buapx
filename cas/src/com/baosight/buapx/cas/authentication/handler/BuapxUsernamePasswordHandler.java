package com.baosight.buapx.cas.authentication.handler;

import org.apache.axis.utils.StringUtils;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.BadPasswordAuthenticationException;
import org.jasig.cas.authentication.handler.UnknownUsernameAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baosight.buapx.cas.authentication.principal.BuapxUsernamePasswordCredentials;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.common.EncryptUtils;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;

public class BuapxUsernamePasswordHandler extends
		AbstractUsernamePasswordAuthenticationHandler {
	private static Logger logger = LoggerFactory
			.getLogger(BuapxUsernamePasswordHandler.class);

	@Override
	protected boolean authenticateUsernamePasswordInternal(
			UsernamePasswordCredentials credentials)
			throws AuthenticationException {

		try {

			boolean result = authByManager(credentials, authManager);

			return result;
		} catch (BuapxTransferException ex) {
			throw new RuntimeException("后台认证错误");
		}

	}

	private boolean authByManager(UsernamePasswordCredentials credentials,
			IAuthManager authManager)
			throws BadCredentialsAuthenticationException,
			BuapxTransferException {

		BuapxUsernamePasswordCredentials ouyeelCredentials = (BuapxUsernamePasswordCredentials) credentials;

		// 本不该在此进行输入为空判断,但欧冶的登录流程中，需要区分是来自业务系统登陆页的请求还是业务系统客户端过滤器的请求。后者不传递用户名密码，直接跳转回原登陆页；前者需要进行校验
		if (StringUtils.isEmpty(credentials.getUsername())
				|| StringUtils.isEmpty(credentials.getPassword())) {
			throw new UnknownUsernameAuthenticationException();
		}
		String casUserid = authManager.queryUserByLoginAlias(credentials
				.getUsername()); // 用户别名登录
		AuthUserInfo userInfo = authManager.queryUserInfo(casUserid);

		if (userInfo == null) {
			throw new UnknownUsernameAuthenticationException();
		} else {
			credentials.setUsername(userInfo.getUserid());
		}

		try {
			boolean result = authManager.hasPermissionToCas(userInfo
					.getUserid(), EncryptUtils.passwordEncode(credentials
					.getPassword()));

			if (result == false) { // 直接抛异常返回，是为了在AuthenticationViaFormAction里记录用户登录日志时，获得明确的错误原因及获取登录IP
				throw new BadPasswordAuthenticationException();
			}
			return result;

		} catch (BadPasswordAuthenticationException e) {
			e.printStackTrace();
			throw new BadPasswordAuthenticationException(e);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private IAuthManager authManager;

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

}
