package com.baosight.buapx.cas.authentication.principal;

import org.jasig.cas.authentication.principal.AbstractPersonDirectoryCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Credentials;

public class BuapxUsernamePasswordCredentialsToPrincipalResolver extends AbstractPersonDirectoryCredentialsToPrincipalResolver{

	@Override
	public boolean supports(Credentials credentials) {
        return credentials != null&& BuapxUsernamePasswordCredentials.class.isAssignableFrom(credentials.getClass());
	}

	@Override
	protected String extractPrincipalId(Credentials credentials) {
		 final BuapxUsernamePasswordCredentials usernamePasswordCredentials = (BuapxUsernamePasswordCredentials) credentials;
	        return usernamePasswordCredentials.getUsername();
	}

}
