/**
 *
 */
package com.baosight.buapx.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;

/**
 * @author lizidi@baosight.com
 *         2012-6-7
 */

public class SingleSourceUserTranslator implements UserTranslatorInf {

	private Logger log = LoggerFactory.getLogger(SingleSourceUserTranslator.class);

	public IAuthManager getAuthManager() {
		return authManager;
	}

	private IAuthManager authManager;

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.baosight.buapx.web.support.UserTranslatorInf#getUser(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public String getUser(String casUserName, String targetPlatFormName) throws BuapxTransferException {

			String userName;
			userName = this.authManager.querySubAccount(casUserName, targetPlatFormName);
			return userName;


	}

}
