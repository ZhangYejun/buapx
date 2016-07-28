package com.baosight.buapx.cas.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import com.baosight.buapx.cas.logger.LoginLogger;
import com.baosight.buapx.common.IpRetriever;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;

/**
 * Controller to delete ticket granting ticket cookie in order to log out of
 * single sign on. This controller implements the idea of the ESUP Portail's
 * Logout patch to allow for redirecting to a url on logout. It also exposes a
 * log out link to the view via the WebConstants.LOGOUT constant.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 * modify by lizidi. 2012.3.31  add loginType parameter
 */
public final class LogoutController extends AbstractController {

    /** The CORE to which we delegate for all CAS functionality. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    /** CookieGenerator for TGT Cookie */
    @NotNull
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    /** CookieGenerator for Warn Cookie */
    @NotNull
    private CookieRetrievingCookieGenerator warnCookieGenerator;
    
    @NotNull
    private CookieRetrievingCookieGenerator loginedUserCookieGenerator;

    /** Logout view name. */
    @NotNull
    private String logoutView;

    private TicketRegistry ticketRegistry;

    private IAuthManager authManager;


    public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	/**
     * Boolean to determine if we will redirect to any url provided in the
     * service request parameter.
     */
    private boolean followServiceRedirects;


    public LogoutController() {
        setCacheSeconds(0);
    }

    protected ModelAndView handleRequestInternal(
        final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        final String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
        final String service = request.getParameter("service");

        if (ticketGrantingTicketId != null){
        		TicketGrantingTicket tgc=(TicketGrantingTicket) this.ticketRegistry.getTicket(ticketGrantingTicketId,TicketGrantingTicket.class);
        		if(tgc!=null){
        			String userid=tgc.getAuthentication().getPrincipal().getId();
            		LoginLogger.logoutSuccessLog(userid, IpRetriever.getIpThroughSwitch(request), "", "","") ;
        		}

        }


        if (ticketGrantingTicketId != null) {
            this.centralAuthenticationService
                .destroyTicketGrantingTicket(ticketGrantingTicketId);

            this.ticketGrantingTicketCookieGenerator.removeCookie(response);
            this.warnCookieGenerator.removeCookie(response);
            //add by yikeke 2015-08-13 remove cookie
            this.loginedUserCookieGenerator.removeCookie(response);
            //end add
        }
        
        if (this.followServiceRedirects && service != null) {
            return new ModelAndView(new RedirectView(service));
        }

        //modify by lizidi, added two parameters
        ModelAndView mav= new ModelAndView(this.logoutView);
        mav.addObject("loginType", request.getParameter("loginType"));
        mav.addObject("redirectUrl", request.getParameter("redirectUrl"));
        mav.addObject("windowType", request.getParameter("windowType"));

        return mav;
    }

    public void setTicketGrantingTicketCookieGenerator(
        final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public void setWarnCookieGenerator(final CookieRetrievingCookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }

    /**
     * @param centralAuthenticationService The centralAuthenticationService to
     * set.
     */
    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void setFollowServiceRedirects(final boolean followServiceRedirects) {
        this.followServiceRedirects = followServiceRedirects;
    }

    public void setLogoutView(final String logoutView) {
        this.logoutView = logoutView;
    }

	public void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}

	public void setLoginedUserCookieGenerator(
			CookieRetrievingCookieGenerator loginedUserCookieGenerator) {
		this.loginedUserCookieGenerator = loginedUserCookieGenerator;
	}


}
