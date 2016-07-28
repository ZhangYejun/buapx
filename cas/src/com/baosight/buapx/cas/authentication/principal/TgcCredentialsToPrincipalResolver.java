package com.baosight.buapx.cas.authentication.principal;

import org.jasig.cas.authentication.principal.AbstractPersonDirectoryCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Credentials;

public class TgcCredentialsToPrincipalResolver extends AbstractPersonDirectoryCredentialsToPrincipalResolver{

	@Override
	public boolean supports(Credentials credentials) {
        return credentials != null&& TgcCredentials.class.isAssignableFrom(credentials.getClass());
	}

	@Override
	protected String extractPrincipalId(Credentials credentials) {
		 final TgcCredentials tgcCredentials = (TgcCredentials) credentials;
	        return tgcCredentials.getUserName();
	}

}
