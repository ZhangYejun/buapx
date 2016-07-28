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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.baosight.buapx.cas.authentication.principal.BuapxWebServiceTicketServer;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 */
public class GetBuapxServiceTicketController extends AbstractController {

	private static final String SUCCESS_VIEW_NAME = "buapxServiceTicketSuccessView";

	private static final String FAILURE_VIEW_PAGE = "buapxServiceTicketFailureView";
	/** Instance of CentralAuthenticationService. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	@NotNull
	private String tgcCookieName;

	public void setTgcCookieName(String tgcCookieName) {
		this.tgcCookieName = tgcCookieName;
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
			final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, BuapxWebServiceTicketServer.createServiceFrom(request));

			if (StringUtils.isBlank(serviceTicketId)) {
				// TODO 添加code定义，便于国际化
				return generateErrorView("-1", "无法生成临时票据!", null);
			}
			ModelAndView success = new ModelAndView(this.SUCCESS_VIEW_NAME);
			success.addObject("ticket", serviceTicketId);
			
			String sysType=request.getParameter("cs");
			if(!StringUtils.isEmpty(sysType)){  //  c/s方式
				//获取协议名称
				success.addObject("cs", "true");
				String syscode = request.getParameter("sysCode");
				if(StringUtils.isBlank(syscode))
					return generateErrorView("-1", "接入系统代码[sysCode]不能为空!", null);
				success.addObject("sysCode", syscode);
			}else{    // 默认 b/s方式
				String sysUrl=request.getParameter("sysUrl");
				sysUrl=URLDecoder.decode(sysUrl,"UTF-8");
				sysUrl=sysUrl+(sysUrl.indexOf("?")==-1?"?":"&")+"ticket="+serviceTicketId;
				success.addObject("sysUrl",sysUrl);
			}
			
			return success;
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
