/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package com.baosight.buapx.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * Action that handles the TicketGrantingTicket creation and destruction. If the
 * action is given a TicketGrantingTicket and one also already exists, the old
 * one is destroyed and replaced with the new one. This action always returns
 * "success".
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public final class SendTicketGrantingTicketAction extends AbstractAction {
    
    @NotNull
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
    
    //add by yikeke 2015-08-13
    @NotNull
    private CookieRetrievingCookieGenerator loginedUserCookieGenerator;

    /** Instance of CentralAuthenticationService. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    protected Event doExecute(final RequestContext context) {
        final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context); 
        final String ticketGrantingTicketValueFromCookie = (String) context.getFlowScope().get("ticketGrantingTicketId");
        
        if (ticketGrantingTicketId == null) {
            return success();
        }
        
        this.ticketGrantingTicketCookieGenerator.addCookie(WebUtils.getHttpServletRequest(context), WebUtils
            .getHttpServletResponse(context), ticketGrantingTicketId);
        
        //add by yikeke 2015-08-13
        String logineduserid = (String)context.getRequestScope().get("logineduserid");
       
        this.loginedUserCookieGenerator.addCookie(WebUtils.getHttpServletRequest(context), WebUtils
            .getHttpServletResponse(context), logineduserid);
        logger.debug("now addCookie for loginedUserCookieGenerator value="+logineduserid);
        //end add
        if (ticketGrantingTicketValueFromCookie != null && !ticketGrantingTicketId.equals(ticketGrantingTicketValueFromCookie)) {
            this.centralAuthenticationService
                .destroyTicketGrantingTicket(ticketGrantingTicketValueFromCookie);
        }

        return success();
    }
    
    public void setTicketGrantingTicketCookieGenerator(final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator= ticketGrantingTicketCookieGenerator;
    }
    
    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

	public void setLoginedUserCookieGenerator(
			CookieRetrievingCookieGenerator loginedUserCookieGenerator) {
		this.loginedUserCookieGenerator = loginedUserCookieGenerator;
	}
}
