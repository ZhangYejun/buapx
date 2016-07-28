/**
 * lizidi@baosight.com
 */
package com.baosight.buapx.web.flow;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;



import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;


public class PwdExpireCheckAction extends AbstractAction {
private IAuthManager authManager;
private String pwdChangePage;
private static String CALLBACK_URL="/cas_callback";
private Pattern pTargetUri=Pattern.compile("[?|&]originalTargetUri=[^?&]+");




	public IAuthManager getAuthManager() {
	return authManager;
}

public void setAuthManager(IAuthManager authManager) {
	this.authManager = authManager;
}



	public String getPwdChangePage() {
	return pwdChangePage;
}

public void setPwdChangePage(String pwdChangePage) {
	this.pwdChangePage = pwdChangePage;
}

	protected Event doExecute(final RequestContext context) {

        Credentials credentials=(Credentials) context.getFlowScope().get("credentials");
        if(credentials instanceof UsernamePasswordCredentials){
        	String userid=((UsernamePasswordCredentials) credentials).getUsername();
            boolean ifPwdExpire=false;
			try {
				ifPwdExpire = authManager.isUserPwdExpire(userid);
				if(ifPwdExpire){
					String sourceSystem=(String) context.getFlowScope().get("redirectUrl");
					if(StringUtils.isEmpty(sourceSystem)){
						sourceSystem=getUrlFromService(context);
				   }

					if(!StringUtils.isEmpty(sourceSystem)){
						sourceSystem=URLEncoder.encode(sourceSystem,"UTF-8");
						String forceRedirectUrl=pwdChangePage+(pwdChangePage.indexOf("?")==-1?"?":"&")+"sourceSystem="+sourceSystem;
						context.getFlowScope().put("pwdExpiryPage",forceRedirectUrl);
					}
				}
		  	} catch (BuapxTransferException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if(ifPwdExpire){
				return result("true");
			}else{
				return result("false");
			}
        }else{
        	return result("false");
        }
	}

	private String getUrlFromService(RequestContext context) throws UnsupportedEncodingException{
		String targetUri="";
		SimpleWebApplicationServiceImpl service=(SimpleWebApplicationServiceImpl) context.getFlowScope().get("service");
		if(service!=null){
			String callBackUrl=service.getResponse(context.getRequestScope().getString("serviceTicketId")).getUrl();
			//callBackUrl=URLDecoder.decode(callBackUrl,"UTF-8");

			String baseUrl=callBackUrl.split(CALLBACK_URL+"?")[0];
			Matcher mTargetUri=this.pTargetUri.matcher(callBackUrl);
	        if(mTargetUri.find()){
	        	targetUri=mTargetUri.group().split("=")[1];
	        	targetUri=URLDecoder.decode(targetUri,"UTF-8");
		  }
	    }
		return targetUri;
	}

	public static void main(String args[]) throws UnsupportedEncodingException{
		Pattern pTargetUri=Pattern.compile("[?|&]originalTargetUri=[^?&]+");
		String targetUri="";
		String callBackUrl="http://10.30.28.153:8080/buap/cas_callback.jsp?syscode=buap&originalTargetUri=%2Findex.jsp?ddf&xx";
		//callBackUrl=URLDecoder.decode(callBackUrl,"UTF-8");
		Matcher mTargetUri=pTargetUri.matcher(callBackUrl);
        if(mTargetUri.find()){
        	targetUri=mTargetUri.group().split("=")[1];
	}

		String baseUrl=callBackUrl.split(CALLBACK_URL+"?")[0];

        System.out.println(baseUrl);
	}
}
