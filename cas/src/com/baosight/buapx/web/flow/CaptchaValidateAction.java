/**
 *
 */
package com.baosight.buapx.web.flow;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.web.support.ErrorCodeGenerator;
 

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-7-25
 */
public class CaptchaValidateAction extends AbstractAction {
	
	 private final Logger logger = LoggerFactory.getLogger(CaptchaValidateAction.class);

	private String captchaValidationParameter = "_captcha_parameter";
	
	 

	 

	protected Event doExecute(final RequestContext context) {
 	
		String captcha_response = context.getRequestParameters().get(
				captchaValidationParameter);
		logger.debug("-------------------get validate code from page 页面上取的验证码:"+captcha_response);
		boolean valid = false;
		HttpSession session = WebUtils.getHttpServletRequest(context)
				.getSession(); 
		String catcha = (String) session
				.getAttribute(captchaValidationParameter);
		logger.debug("-------------------in Session sessionid="+session.getId()+"   in session validate code="+catcha);
		session.removeAttribute(captchaValidationParameter);
		if (captcha_response != null) {
			String id = session.getId();
			if (id != null && !StringUtils.isEmpty(captcha_response)) {
				valid = captcha_response.equalsIgnoreCase(catcha);

			}
		}
		//valid = true;
		if (valid) {
			return success();
		}
		context.getMessageContext().addMessage(
				new MessageBuilder().error().code("error.authentication.validatecode.bad").defaultText("error.authentication.validatecode.bad")
						.build());
		ErrorCodeGenerator.populateErrorCodeInFlow("error.authentication.validatecode.bad",context);
		return error();
	}

	public void setCaptchaValidationParameter(String captchaValidationParameter) {
		this.captchaValidationParameter = captchaValidationParameter;
	}

	public static void main(String args[]){
      System.out.println("ab2d".equalsIgnoreCase("AB2D"));
	}

}
