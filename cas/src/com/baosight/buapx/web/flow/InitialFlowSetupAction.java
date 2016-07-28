package com.baosight.buapx.web.flow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Class to automatically set the paths for the CookieGenerators.
 * <p>
 * Note: This is technically not threadsafe, but because its overriding with a
 * constant value it doesn't matter.
 * <p>
 * Note: As of CAS 3.1, this is a required class that retrieves and exposes the
 * values in the two cookies for subclasses to use.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 *
 * edit by lizidi@baosight.com  for web-link proxy cookie problem 2013.3.3
 */
public final class InitialFlowSetupAction extends AbstractAction {

    /** CookieGenerator for the Warnings. */
    @NotNull
    private CookieRetrievingCookieGenerator warnCookieGenerator;

    /** CookieGenerator for the TicketGrantingTickets. */
    @NotNull
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
    
    //add by yikeke 2015-08-13
    @NotNull
    private CookieRetrievingCookieGenerator loginedUserCookieGenerator;

    
    /** Extractors for finding the service. */
    @NotNull
    @Size(min=1)
    private List<ArgumentExtractor> argumentExtractors;

    /** Boolean to note whether we've set the values on the generators or not. */
    private boolean pathPopulated = false;

    protected Event doExecute(final RequestContext context) throws Exception {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        if (!this.pathPopulated) {
            final String contextPath = context.getExternalContext().getContextPath();
            final String cookiePath = StringUtils.hasText(contextPath) ? contextPath + "/" : "/";
            logger.info("Setting path for cookies to: "
                + cookiePath);
            this.warnCookieGenerator.setCookiePath(cookiePath);
            this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
            
            this.loginedUserCookieGenerator.setCookiePath(cookiePath);//
            
            this.pathPopulated = true;
        }
       String tgt=this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
       if(tgt!=null&&tgt.indexOf("TGT-")!=-1){
    	   context.getFlowScope().put(
    	            "ticketGrantingTicketId", tgt);
       }else{
    	   context.getFlowScope().put(
   	            "ticketGrantingTicketId", null);
       }
       //add
       //String logineduserid=this.loginedUserCookieGenerator.retrieveCookieValue(request);
       //context.getFlowScope().put( "logineduserid", logineduserid);
      //end add
       //add by yikeke 2015-12-18 remove cookie  日照用
       this.loginedUserCookieGenerator.removeCookie(WebUtils.getHttpServletResponse(context));
       //end add
       
        context.getFlowScope().put(
            "warnCookieValue",
            Boolean.valueOf(this.warnCookieGenerator.retrieveCookieValue(request)));

        final Service service = WebUtils.getService(this.argumentExtractors,
            context);

        if (service != null && logger.isDebugEnabled()) {
            logger.debug("Placing service in FlowScope: " + service.getId());
        }

        context.getFlowScope().put("service", service);

        return result("success");
    }

    public void setTicketGrantingTicketCookieGenerator(
        final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public void setWarnCookieGenerator(final CookieRetrievingCookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }

    public void setArgumentExtractors(
        final List<ArgumentExtractor> argumentExtractors) {
        this.argumentExtractors = argumentExtractors;
    }

	public void setLoginedUserCookieGenerator(
			final CookieRetrievingCookieGenerator loginedUserCookieGenerator) {
		this.loginedUserCookieGenerator = loginedUserCookieGenerator;
	}
}
