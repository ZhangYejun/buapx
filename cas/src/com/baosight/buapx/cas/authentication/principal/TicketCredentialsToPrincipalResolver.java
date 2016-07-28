package com.baosight.buapx.cas.authentication.principal;

import org.jasig.cas.authentication.principal.AbstractPersonDirectoryCredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Credentials;

public class TicketCredentialsToPrincipalResolver extends AbstractPersonDirectoryCredentialsToPrincipalResolver{

	@Override
	public boolean supports(Credentials credentials) {
        return credentials != null&& TicketCredentials.class.isAssignableFrom(credentials.getClass());
	}

	@Override
	protected String extractPrincipalId(Credentials credentials) {
		 final TicketCredentials ticketCredentials = (TicketCredentials) credentials;
	        return ticketCredentials.getUserName();
	}

}
