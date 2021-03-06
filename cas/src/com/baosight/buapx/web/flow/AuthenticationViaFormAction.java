package com.baosight.buapx.web.flow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.TicketCreationException;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.scope.FlowScope;

import com.baosight.buapx.cas.authentication.principal.BuapxUsernamePasswordCredentials;
import com.baosight.buapx.cas.logger.LoginLogger;
import com.baosight.buapx.cas.logout.LogoutController;
import com.baosight.buapx.common.IpRetriever;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.web.filter.ValidateById;
import com.baosight.buapx.web.support.ErrorCodeGenerator;


public class AuthenticationViaFormAction implements  ApplicationContextAware  {
	 /**
     * Binder that allows additional binding of form object beyond Spring
     * defaults.
     */
    private CredentialsBinder credentialsBinder;

    private IAuthManager authManager;

    /** Core we delegate to for handling all ticket related tasks. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    @NotNull
    private CookieGenerator warnCookieGenerator;
    
    @NotNull
    private CookieRetrievingCookieGenerator loginedUserCookieGenerator;

    public void setLoginedUserCookieGenerator(
			CookieRetrievingCookieGenerator loginedUserCookieGenerator) {
		this.loginedUserCookieGenerator = loginedUserCookieGenerator;
	}

	public CookieGenerator getWarnCookieGenerator() {
		return warnCookieGenerator;
	}

	public void setWarnCookieGenerator(CookieGenerator warnCookieGenerator) {
		this.warnCookieGenerator = warnCookieGenerator;
	}


	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}


	protected Logger logger = LoggerFactory.getLogger(getClass());





    public final void doBind(final RequestContext context, final Credentials credentials) throws Exception {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);

        if (this.credentialsBinder != null && this.credentialsBinder.supports(credentials.getClass())) {
            this.credentialsBinder.bind(request, credentials);
        }
    }



    public final String submit(final RequestContext context, final Credentials credentials, final MessageContext messageContext) throws Exception {
    	final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);

        final Service service = WebUtils.getService(context);
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        String userid=null;
        
        String casuserid = null;
        UsernamePasswordCredentials usernamePasswordCredentials =null;
        if(credentials instanceof UsernamePasswordCredentials){
        	usernamePasswordCredentials =(UsernamePasswordCredentials)credentials;
        	userid=usernamePasswordCredentials.getUsername();
        	//add by yikeke 2015-08-13 解决重复登录
            casuserid = authManager.queryUserByLoginAlias(userid); // 根据用户别名查询casuser
            //String logineduseridValueFromCookie = (String) context.getFlowScope().get("logineduserid");
            String logineduseridValueFromCookie = this.loginedUserCookieGenerator.retrieveCookieValue(request);
            if (logineduseridValueFromCookie==null)
    	    	logineduseridValueFromCookie="";
            if (!"".equals(logineduseridValueFromCookie) && !logineduseridValueFromCookie.equals(casuserid)){
          //  if (!"".equals(logineduseridValueFromCookie)){//只有之前过了，就不让再登录了
    	    	context.getMessageContext().addMessage(
    					new MessageBuilder().error().code("error.authentication.duplicatelogin.bad").defaultText("error.authentication.duplicatelogin.bad")
    							.build());
    			ErrorCodeGenerator.populateErrorCodeInFlow("error.authentication.duplicatelogin.bad",context);
    			return "error";
    	    }  
            //end add 
        }
   
        if (StringUtils.hasText(context.getRequestParameters().get("renew")) && ticketGrantingTicketId != null && service != null) {

            try {
                final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, credentials);
                WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
                
                
                putWarnCookieIfRequestParameterPresent(context);
                return "warn";
            } catch (final TicketException e) {
                if (e.getCause() != null && AuthenticationException.class.isAssignableFrom(e.getCause().getClass())) {
                    ErrorCodeGenerator.populateErrorCodeInFlow(e.getCode(),context);
                    populateErrorsInstance(e, messageContext);
                    return "error";
                }
                this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
                if (logger.isDebugEnabled()) {
                    logger.debug("Attempted to generate a ServiceTicket using renew=true with different credentials", e);
                }
            }
        }
  	 
        try {
            WebUtils.putTicketGrantingTicketInRequestScope(context, this.centralAuthenticationService.createTicketGrantingTicket(credentials));
            putWarnCookieIfRequestParameterPresent(context);
            //add by yikeke 2015-08-13 
            String newloginedid =usernamePasswordCredentials ==null?"":usernamePasswordCredentials.getUsername();
            context.getRequestScope().put("logineduserid",newloginedid);
            //end add
            
            LoginLogger.loginSuccessLog(casuserid,userid, IpRetriever.getIpThroughSwitch(request), "", "","");
            
            
            return "success";
        }
        catch (final TicketException e) {
             LoginLogger.loginFailureLog(casuserid,userid, IpRetriever.getIpThroughSwitch(request), "", "失败原因:"+e.getMessage(),"");

        	e.printStackTrace();
            ErrorCodeGenerator.populateErrorCodeInFlow(e.getCode(),context);
            populateErrorsInstance(e, messageContext);
            return "error";
        }
    }


    private void populateErrorsInstance(final TicketException e, final MessageContext messageContext) {

        try {
            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).defaultText(e.getCode()).build());
        } catch (final Exception fe) {
            logger.error(fe.getMessage(), fe);
        }
    }





    private void putWarnCookieIfRequestParameterPresent(final RequestContext context) {
        final HttpServletResponse response = WebUtils.getHttpServletResponse(context);

        if (StringUtils.hasText(context.getExternalContext().getRequestParameterMap().get("warn"))) {
            this.warnCookieGenerator.addCookie(response, "true");
        } else {
            this.warnCookieGenerator.removeCookie(response);
        }
    }

    public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    /**
     * Set a CredentialsBinder for additional binding of the HttpServletRequest
     * to the Credentials instance, beyond our default binding of the
     * Credentials as a Form Object in Spring WebMVC parlance. By the time we
     * invoke this CredentialsBinder, we have already engaged in default binding
     * such that for each HttpServletRequest parameter, if there was a JavaBean
     * property of the Credentials implementation of the same name, we have set
     * that property to be the value of the corresponding request parameter.
     * This CredentialsBinder plugin point exists to allow consideration of
     * things other than HttpServletRequest parameters in populating the
     * Credentials (or more sophisticated consideration of the
     * HttpServletRequest parameters).
     *
     * @param credentialsBinder the credentials binder to set.
     */
    public final void setCredentialsBinder(final CredentialsBinder credentialsBinder) {
        this.credentialsBinder = credentialsBinder;
    }

    ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
