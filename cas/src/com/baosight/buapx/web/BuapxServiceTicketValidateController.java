package com.baosight.buapx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.ticket.InvalidTicketException;
import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.Cas20ProtocolValidationSpecification;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.baosight.buapx.cas.authentication.principal.BuapxWebServiceTicketServer;
import com.baosight.buapx.web.support.UserTranslatorInf;
/**
 * for /buapxServiceValidate Path. for C/S ticket validate
 * @author Administrator
 *
 */ 
public class BuapxServiceTicketValidateController extends AbstractController {

	public static final String ST = "ST";
	
	public static final String SYSCODE = "sysCode";

	private static final String USER = "user";

	private static final String ERROR_MSG = "ERROR_MSG";

	private static final String ERROR_CODE = "ERROR_CODE";

	public static final String SUCCESS_VIEW = "buapxServiceTicketValidateSuccessView";

	public static final String FAILURE_VIEW = "buapxServiceTicketValidateFailureView";


	/** The CORE which we will delegate all requests to. */
	@NotNull
	private CentralAuthenticationService centralAuthenticationService;

	public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			final String serviceTicket = request.getParameter(BuapxServiceTicketValidateController.ST);
			final Assertion assertion = centralAuthenticationService.validateServiceTicket(serviceTicket, BuapxWebServiceTicketServer.createServiceFrom(request));
			String userName = assertion.getChainedAuthentications().get(assertion.getChainedAuthentications().size() - 1).getPrincipal().getId();
			String sysCode = request.getParameter(this.SYSCODE);
			if(StringUtils.isBlank(sysCode))
				return generateErrorView("-100", "接入系统代码[sysCode]不能为空!", null);
			String user = userTranslator.getUser(userName, sysCode);
			if(StringUtils.isBlank(user)){
				return generateErrorView("-300", "接入系统["+sysCode+"]用户无法找到", null);
			}
			final ModelAndView success = new ModelAndView(this.SUCCESS_VIEW);
			success.addObject(USER, user);
			return success;
		} catch(InvalidTicketException e){
			return generateErrorView("-200","票据不合法!", null);
		}
		catch (Exception e) {
			return generateErrorView("-1", e.getMessage(), null);
		}
	}

	private ModelAndView generateErrorView(final String code, final String description, final Object[] args) {
		final ModelAndView modelAndView = new ModelAndView(this.FAILURE_VIEW);
		final String convertedDescription = getMessageSourceAccessor().getMessage(description, args, description);
		modelAndView.addObject(ERROR_MSG, description);
		modelAndView.addObject(ERROR_CODE, code);
		return modelAndView;
	}
	
	private UserTranslatorInf userTranslator;

	public void setUserTranslator(UserTranslatorInf userTranslator) {
		this.userTranslator = userTranslator;
	}

}
