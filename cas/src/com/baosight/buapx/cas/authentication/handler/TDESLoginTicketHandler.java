package com.baosight.buapx.cas.authentication.handler;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.UnknownUsernameAuthenticationException;

import com.baosight.buapx.cas.authentication.principal.TicketCredentials;
import com.baosight.buapx.sso.ILoginTicketAnalyser;
import com.baosight.buapx.sso.TDESLoginTicketAnalyser;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;


public class TDESLoginTicketHandler extends AbstractTicketHandler {
	private ILoginTicketAnalyser  loginTicketAnalyser;
	private IAuthManager authManager;

	
	@Override
	protected boolean authNow(TicketCredentials ticketCredentials) throws AuthenticationException {
		String ticket=ticketCredentials.getTicket();
        String username = ticketCredentials.getUserName();
		if (StringUtils.isEmpty(username)) {
			throw new UnknownUsernameAuthenticationException();
		}
		String casUserid = authManager.queryUserByLoginAlias(username); // 用户别名登录
		AuthUserInfo userInfo = null;
		try {
			userInfo = authManager.queryUserInfo(casUserid);
		} catch (BuapxTransferException e) {			 
			e.printStackTrace();
		}

		if (userInfo == null) {
			throw new UnknownUsernameAuthenticationException();
		} else {
			ticketCredentials.setUserName(userInfo.getUserid());
		} 
		 boolean result= loginTicketAnalyser.validate(ticket, username);
		 if (!result )
			 throw new BadCredentialsAuthenticationException("票据验证失败,请通过用户名密码登录");
		 return result;
	 
	}

	public void setLoginTicketAnalyser(ILoginTicketAnalyser loginTicketAnalyser) {
		this.loginTicketAnalyser = loginTicketAnalyser;
	}
	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}
	

	
 

}
