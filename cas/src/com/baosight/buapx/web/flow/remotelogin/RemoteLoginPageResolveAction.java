package com.baosight.buapx.web.flow.remotelogin;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.RequestContext;

import com.baosight.buapx.web.flow.AuthenticationViaFormAction;
import com.baosight.buapx.web.support.ErrorCodeGenerator;


public final class RemoteLoginPageResolveAction  {

	public void decodeThenAppendService(RequestContext context,String loginPage,String service) throws Exception {

		loginPage=URLDecoder.decode(loginPage, "UTF-8");
		if(service!=null){
			loginPage=loginPage+(loginPage.indexOf("?")<0?"?":"&")+"service="+URLEncoder.encode(service,"UTF-8");
		}
		context.getFlowScope().put("remoteLoginPage",loginPage);
	}

	public void appendErrorCode(RequestContext context,String loginPage) throws Exception {

		String errorCode= ErrorCodeGenerator.retrieveErrorCodeInFlow(context);

		if(errorCode!=null){
			loginPage=loginPage+(loginPage.indexOf("?")<0?"?":"&")+"error="+errorCode;
		}
		context.getFlowScope().put("remoteLoginPage",loginPage);
	}

	public void appendLoginFlag(RequestContext context,String loginPage) throws Exception {

		loginPage=loginPage+(loginPage.indexOf("?")<0?"?":"&")+"login=false";

		context.getFlowScope().put("remoteLoginPage",loginPage);
	}

}
