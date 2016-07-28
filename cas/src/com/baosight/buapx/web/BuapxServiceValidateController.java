/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 *
 * modifyied by lizidi.Baosight. For User-Mapping supporting.
 */
package com.baosight.buapx.web;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.HttpBasedServiceCredentials;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.TicketValidationException;
import org.jasig.cas.ticket.proxy.ProxyHandler;
import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.ValidationSpecification;
import org.jasig.cas.validation.Cas20ProtocolValidationSpecification;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.baosight.buapx.cas.logger.LoginLogger;
import com.baosight.buapx.common.IpRetriever;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;
import com.baosight.buapx.web.exception.NoPermissionToSystemException;
import com.baosight.buapx.web.flow.AuthenticationViaFormAction;
import com.baosight.buapx.web.support.UserTranslatorInf;

/**
 * Process the /validate and /serviceValidate URL requests.
 * <p>
 * Obtain the Service Ticket and Service information and present them to the CAS
 * validation services. Receive back an Assertion containing the user Principal
 * and (possibly) a chain of Proxy Principals. Store the Assertion in the Model
 * and chain to a View to generate the appropriate response (CAS 1, CAS 2 XML,
 * SAML, ...).
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 *
 * modified by lizidi@baosight.com for buapx,Process the /serviceValidate  URL requests,do the user mapping
 */
public class BuapxServiceValidateController extends AbstractController {

    /** View if Service Ticket Validation Fails. */
    private static final String DEFAULT_SERVICE_FAILURE_VIEW_NAME = "casServiceFailureView";

    /** View if Service Ticket Validation Succeeds. */
    private static final String DEFAULT_SERVICE_SUCCESS_VIEW_NAME = "casServiceSuccessView";

    /** Constant representing the PGTIOU in the model. */
    private static final String MODEL_PROXY_GRANTING_TICKET_IOU = "pgtIou";

    /** Constant representing the Assertion in the model. */
    private static final String MODEL_ASSERTION = "assertion";

    /** The CORE which we will delegate all requests to. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    /** The validation protocol we want to use. */
    @NotNull
    private Class<?> validationSpecificationClass = Cas20ProtocolValidationSpecification.class;

    /** The proxy handler we want to use with the controller. */
    @NotNull
    private ProxyHandler proxyHandler;

    /** The view to redirect to on a successful validation. */
    @NotNull
    private String successView = DEFAULT_SERVICE_SUCCESS_VIEW_NAME;

    /** The view to redirect to on a validation failure. */
    @NotNull
    private String failureView = DEFAULT_SERVICE_FAILURE_VIEW_NAME;

    /** Extracts parameters from Request object. */
    @NotNull
    private ArgumentExtractor argumentExtractor;

    //added by lizidi,for get the syscode from serviceUrl
    private Pattern p=Pattern.compile("[?|&]syscode=\\w+");

    //added by lizidi, for validate user's permission to system
    private UserTranslatorInf userTranslator;

    private IAuthManager authManager;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());

	public IAuthManager getAuthManager() {
		return authManager;
	}
	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	public void setUserTranslator(UserTranslatorInf userTranslator) {
		this.userTranslator = userTranslator;
	}
    /**
     * Overrideable method to determine which credentials to use to grant a
     * proxy granting ticket. Default is to use the pgtUrl.
     *
     * @param request the HttpServletRequest object.
     * @return the credentials or null if there was an error or no credentials
     * provided.
     */
    protected Credentials getServiceCredentialsFromRequest(final HttpServletRequest request) {
        final String pgtUrl = request.getParameter("pgtUrl");
        if (StringUtils.hasText(pgtUrl)) {
            try {
                return new HttpBasedServiceCredentials(new URL(pgtUrl));
            } catch (final Exception e) {
                logger.error("Error constructing pgtUrl", e);
            }
        }

        return null;
    }

    protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder) {
        binder.setRequiredFields("renew");
    }

    protected final ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

    	final WebApplicationService service = this.argumentExtractor.extractService(request);
        final String serviceTicketId = service != null ? service.getArtifactId() : null;

        if (service == null || serviceTicketId == null) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Could not process request; Service: %s, Service Ticket Id: %s", service, serviceTicketId));
            }
            return generateErrorView("INVALID_REQUEST", "INVALID_REQUEST", null);
        }

        try {
            final Credentials serviceCredentials = getServiceCredentialsFromRequest(request);
            String proxyGrantingTicketId = null;

            // XXX should be able to validate AND THEN use
            if (serviceCredentials != null) {
                try {
                    proxyGrantingTicketId = this.centralAuthenticationService
                        .delegateTicketGrantingTicket(serviceTicketId,
                            serviceCredentials);
                } catch (final TicketException e) {
                    logger.error("TicketException generating ticket for: "
                        + serviceCredentials, e);
                }
            }

            final Assertion assertion = this.centralAuthenticationService.validateServiceTicket(serviceTicketId, service);



            final ValidationSpecification validationSpecification = this.getCommandClass();
            final ServletRequestDataBinder binder = new ServletRequestDataBinder(validationSpecification, "validationSpecification");
            initBinder(request, binder);
            binder.bind(request);





            if (!validationSpecification.isSatisfiedBy(assertion)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("ServiceTicket [" + serviceTicketId + "] does not satisfy validation specification.");
                }
                return generateErrorView("INVALID_TICKET", "INVALID_TICKET_SPEC", null);
            }




            onSuccessfulValidation(serviceTicketId, assertion);

            final ModelAndView success = new ModelAndView(this.successView);
            success.addObject(MODEL_ASSERTION, assertion);

            // 获取业务系统代码，如果没有取到，则只返回主代码
			String sysCode = parseSysCode(assertion);

			// for usename mapping.取得子账户信息
			String mappedUser = validatePermissiontoSystem(assertion, sysCode);
			if (mappedUser == null) {
				return generateErrorView("INVALID_TICKET",
						"INVALID_TICKET_SPEC", null);
			}
			success.addObject("mappedUser", mappedUser);
			if (sysCode != null) {
				// add extra attribute
				addExtraParam(success, assertion);
			}
         


            if (serviceCredentials != null && proxyGrantingTicketId != null) {
                final String proxyIou = this.proxyHandler.handle(serviceCredentials, proxyGrantingTicketId);
                success.addObject(MODEL_PROXY_GRANTING_TICKET_IOU, proxyIou);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Successfully validated service ticket: %s", serviceTicketId));
            }

            return success;
        } catch (final TicketValidationException e) {
            return generateErrorView(e.getCode(), e.getCode(), new Object[] {serviceTicketId, e.getOriginalService().getId(), service.getId()});
        } catch (final TicketException te) {
            return generateErrorView(te.getCode(), te.getCode(),
                new Object[] {serviceTicketId});
        } catch (final UnauthorizedServiceException e) {
            return generateErrorView(e.getMessage(), e.getMessage(), null);
        }
    }

    protected void onSuccessfulValidation(final String serviceTicketId, final Assertion assertion) {



    }

    private ModelAndView generateErrorView(final String code, final String description, final Object[] args) {
        final ModelAndView modelAndView = new ModelAndView(this.failureView);
        final String convertedDescription = getMessageSourceAccessor().getMessage(description, args, description);
        modelAndView.addObject("code", code);
        modelAndView.addObject("description", convertedDescription);

        return modelAndView;
    }

    private ValidationSpecification getCommandClass() {
        try {
            return (ValidationSpecification) this.validationSpecificationClass.newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param centralAuthenticationService The centralAuthenticationService to
     * set.
     */
    public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public final void setArgumentExtractor(final ArgumentExtractor argumentExtractor) {
        this.argumentExtractor = argumentExtractor;
    }

    /**
     * @param validationSpecificationClass The authenticationSpecificationClass
     * to set.
     */
    public final void setValidationSpecificationClass(final Class<?> validationSpecificationClass) {
        this.validationSpecificationClass = validationSpecificationClass;
    }

    /**
     * @param failureView The failureView to set.
     */
    public final void setFailureView(final String failureView) {
        this.failureView = failureView;
    }

    /**
     * @param successView The successView to set.
     */
    public final void setSuccessView(final String successView) {
        this.successView = successView;
    }

    /**
     * @param proxyHandler The proxyHandler to set.
     */
    public final void setProxyHandler(final ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    //解析出传过来的业务系统代码sysCode
    protected String parseSysCode(Assertion assertion) throws UnsupportedEncodingException{
    	
    	 String serviceUrl=URLDecoder.decode(assertion.getService().getId(),"UTF-8");
         Matcher m=this.p.matcher(serviceUrl);
         String sysCode =null;
         if(m.find()) //找出serviceUrl中的sysCode,之前在登录成功，所生成票据对应的serviceUrl也已拼接了sysCode参数,因此无法伪造
         	 sysCode=m.group().split("=")[1];
         
         return sysCode;
    	
    }

  //added by lizidi,for usename mapping.
    protected String validatePermissiontoSystem(Assertion assertion,String sysCode) throws UnsupportedEncodingException, BuapxTransferException{

        final int authenticationChainSize = assertion.getChainedAuthentications().size();
        final Authentication authentication = assertion.getChainedAuthentications().get(
            authenticationChainSize - 1);
        String userid=authentication.getPrincipal().getId();       
        
        if (sysCode!=null){
        	LoginLogger.redirectSystemLog(userid, sysCode,"","");
        	String user = userTranslator.getUser(userid,sysCode);
        	return user;         	
        }else{
        	//没有传入sysCode的情况下，日志提示error，但还是返回当前的casUserID
        	logger.error("the parameter 'syscode' is not found");
        	return userid;
			//	throw new NoPermissionToSystemException("the parameter 'syscode' is not found");
        }
    }

private void addExtraParam(ModelAndView view,Assertion assertion) throws BuapxTransferException{
    final int authenticationChainSize = assertion.getChainedAuthentications().size();
    final Authentication authentication = assertion.getChainedAuthentications().get(
        authenticationChainSize - 1);
    String userid=authentication.getPrincipal().getId();
    view.addObject("casUser",userid);

    AuthUserInfo userInfo=authManager.queryUserInfo(userid);
    view.addObject("userType", userInfo.getUserType());
}

}



