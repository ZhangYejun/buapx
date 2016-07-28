/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package com.baosight.buapx.web.flow;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.baosight.buapx.ua.auth.api.IAuthManager;

/**
 * Action to generate a service ticket for a given Ticket Granting Ticket and
 * Service.
 *
 * @author Scott Battaglia  modifed by lizidi@baosight.com for Baogang
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public final class GenerateServiceTicketAction extends AbstractAction {
	 protected final Logger log = LoggerFactory.getLogger(getClass());
    private String activeUrl;
    private IAuthManager authManager;
    private TicketRegistry ticketRegistry;
    private Pattern pTargetUri=Pattern.compile("[?|&]originalTargetUri=[^?&]+");



    public String getActiveUrl() {
		return activeUrl;
	}

	public void setActiveUrl(String activeUrl) {
		this.activeUrl = activeUrl;
	}






	public TicketRegistry getTicketRegistry() {
		return ticketRegistry;
	}

	public void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}





	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}





	/** Instance of CentralAuthenticationService. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    protected Event doExecute(final RequestContext context) {




        final Service service = WebUtils.getService(context);
        final String ticketGrantingTicket = WebUtils.getTicketGrantingTicketId(context);

        String serviceUrl=service.getId();
        String sourceUrl=getSourceUrlFromService(serviceUrl);
      //判断是否需要检查用户的激活状态  
      if(needCheckUserActiveStatus (this.activeUrl)){ 
	       if(sourceUrl.indexOf(this.activeUrl)!=0){ //若不等于激活页面，则检查激活状态。（防止死循环)
	        	 TicketGrantingTicket tgc=(TicketGrantingTicket) ticketRegistry.getTicket(ticketGrantingTicket);
	        	 if(tgc==null){
	        		 log.error("Error,ticketGrantingTicket:"+ticketGrantingTicket+" not found in Registry.");
	        		 return error();
	        	 }
	             String userid=tgc.getAuthentication().getPrincipal().getId();
	             try {
					if(!authManager.isUserActived(userid)){
					  	  try {
							context.getFlowScope().put("activePage", this.activeUrl+(this.activeUrl.indexOf("?")<0?"?":"&")+"sourceSystem="+URLEncoder.encode(sourceUrl,"UTF-8"));
						} catch (Exception e){
							throw new RuntimeException("encode source url error:",e);
						}
						return result("active");
					 }
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("generateServiceTicketAction: cassandra read error",e);
				}
	        }
      }
        try {
            final String serviceTicketId = this.centralAuthenticationService
                .grantServiceTicket(ticketGrantingTicket,
                    service);
            WebUtils.putServiceTicketInRequestScope(context,
                serviceTicketId);
            return success();
        } catch (final TicketException e) {
            e.printStackTrace();
            if (isGatewayPresent(context)) {
                return result("gateway");
            }
        }

        return error();
    }

    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    protected boolean isGatewayPresent(final RequestContext context) {
        return StringUtils.hasText(context.getExternalContext()
            .getRequestParameterMap().get("gateway"));
    }



    private String getSourceUrlFromService(String serviceUrl){
		String targetUri="";
			Matcher mTargetUri=this.pTargetUri.matcher(serviceUrl);
	        if(mTargetUri.find()){
	        	targetUri=mTargetUri.group().split("=")[1];
	        	try {
					targetUri=URLDecoder.decode(targetUri,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		  }

		return targetUri;
	}
    //根据配置的activeUrl来判断是否需要检查用户激活状态
  private boolean  needCheckUserActiveStatus(String activeUrl){
	  if (activeUrl==null || "".equals(activeUrl)|| "null".equalsIgnoreCase(activeUrl))
		  return false;
	  else 
		  return true;
  }


public static void main(String[] args) {
	String serurl ="http://localhost:8080/cas_client_all/cas_callback.jsp?syscode=rtx&originalTargetUri=http%3A%2F%2Flocalhost%3A8080%2Fcas_client_all%2Findex.jsp&a=111";
	String ori =new GenerateServiceTicketAction().getSourceUrlFromService(serurl);
	System.out.println("ori="+ori);
}

}
