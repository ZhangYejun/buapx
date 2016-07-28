/**
 *
 */
package com.baosight.buapx.web;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.baosight.buapx.cas.authentication.principal.BuapxWebServiceTicketServer;


public class WebCommonSsoController extends AbstractController {

	private static final String SUCCESS_VIEW_NAME = "webCommonSsoSuccessView";

	private static final String FAILURE_VIEW_PAGE = "buapxServiceTicketFailureView";
	/** Instance of CentralAuthenticationService. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	@NotNull
	private String tgcCookieName;


    private TicketRegistry ticketRegistry;


	public void setTgcCookieName(String tgcCookieName) {
		this.tgcCookieName = tgcCookieName;
	}



	public TicketRegistry getTicketRegistry() {
		return ticketRegistry;
	}



	public void setTicketRegistry(TicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}



	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ticketGrantingTicketId = null;
		try {
			for (Cookie cookie : request.getCookies()) {
				if (tgcCookieName.equals(cookie.getName()))
					ticketGrantingTicketId = cookie.getValue();

			}
			if (StringUtils.isBlank(ticketGrantingTicketId)) {
				// TODO 添加code定义，便于国际化
				return generateErrorView("-1", "用户未登录!", null);
			}

			 TicketGrantingTicket tgc=(TicketGrantingTicket) ticketRegistry.getTicket(ticketGrantingTicketId);
        	 if(tgc==null){
 				return generateErrorView("-1", "用户未登录!", null);
        	 }


 			String userid=request.getParameter("userid");
 			String errorPage=URLDecoder.decode(request.getParameter("errorPage"),"UTF-8");
             String currentUser=tgc.getAuthentication().getPrincipal().getId();
             if(!userid.equals(currentUser)){
     			response.sendRedirect(errorPage);
             }

			final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, BuapxWebServiceTicketServer.createServiceFrom(request));

			if (StringUtils.isBlank(serviceTicketId)) {
				// TODO 添加code定义，便于国际化
				return generateErrorView("-1", "无法生成临时票据!", null);
			}

			ModelAndView success = new ModelAndView(this.SUCCESS_VIEW_NAME);

			String target=request.getParameter("target");
			target=URLDecoder.decode(target, "UTF-8");
			success.addObject("ticket", serviceTicketId);


			target=target+(target.indexOf("?")==-1?"?":"&")+"ticket="+serviceTicketId;
			success.addObject("target", target);

			response.sendRedirect(target);
			return null;
		} catch (Exception ex) {
			return generateErrorView("-1", ex.getMessage(), null);
		}

	}

	private ModelAndView generateErrorView(final String code, final String description, final Object[] args) {
		final ModelAndView modelAndView = new ModelAndView(this.FAILURE_VIEW_PAGE);
		final String convertedDescription = getMessageSourceAccessor().getMessage(description, args, description);
		modelAndView.addObject("code", code);
		modelAndView.addObject("description", convertedDescription);
		return modelAndView;
	}

}
