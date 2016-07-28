package com.baosight.buapx.cas.authentication.handler;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;

import com.baosight.buapx.cas.authentication.principal.TicketCredentials;
 
public abstract class AbstractTicketHandler extends  AbstractPreAndPostProcessingAuthenticationHandler{

	

	/** Default class to support if one is not supplied. */
    private static final Class<TicketCredentials> DEFAULT_CLASS = TicketCredentials.class;

    /** Class that this instance will support. */
    private Class< ? > classToSupport = DEFAULT_CLASS;

    /**
     * Boolean to determine whether to support subclasses of the class to
     * support.
     */
    private boolean supportSubClasses = true;
	
	@Override
	public boolean supports(Credentials credentials) {
		  return credentials != null
          && (this.classToSupport.equals(credentials.getClass()) || (this.classToSupport
              .isAssignableFrom(credentials.getClass()))
              && this.supportSubClasses);
	}

	@Override
	protected boolean doAuthentication(Credentials credentials)
			throws AuthenticationException {
        TicketCredentials ticketCredentials=(TicketCredentials) credentials;
        String ticket=ticketCredentials.getTicket();
        String username = ticketCredentials.getUserName();
        if(ticket==null){
        	return false;
        }else{
        	boolean result= authNow(ticketCredentials);
        	 
        	 
        	return result;
        }
	}
	protected abstract boolean authNow(TicketCredentials ticketCredentials) throws AuthenticationException ;


}
