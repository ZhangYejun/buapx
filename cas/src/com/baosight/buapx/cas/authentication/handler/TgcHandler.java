package com.baosight.buapx.cas.authentication.handler;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.BlockedCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;

import com.baosight.buapx.cas.authentication.principal.TgcCredentials;
/*
 * lizidi@baosight.com
 */
public class TgcHandler extends  AbstractPreAndPostProcessingAuthenticationHandler{

	private TicketRegistry ticketRegistry;
	
	

    public TicketRegistry getTicketRegistry() {
		return ticketRegistry;
	}

	public void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}

	/** Default class to support if one is not supplied. */
    private static final Class<TgcCredentials> DEFAULT_CLASS = TgcCredentials.class;

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
        TgcCredentials tgcCredentials=(TgcCredentials) credentials;
        String tgcId=tgcCredentials.getTgc();
        if(tgcId==null){
        	return false;
        }else{
        	TicketGrantingTicket tgc=(TicketGrantingTicket) ticketRegistry.getTicket(tgcId);
        	if(tgc!=null){
        		tgcCredentials.setUserName(tgc.getAuthentication().getPrincipal().getId());
        		return true;
        	}else{
				throw new BadCredentialsAuthenticationException("获取UC登录状态失败");
        	}
        }
	}

	


}
