package com.baosight.buapx.cas.authentication.handler;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BlockedCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * 测试用handler
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-7-17
 */
public class BuapxAlwaysTrueHandler extends AbstractUsernamePasswordAuthenticationHandler{

	protected boolean authenticateUsernamePasswordInternal(UsernamePasswordCredentials credentials) throws AuthenticationException {
		return true;
	}
	


}
