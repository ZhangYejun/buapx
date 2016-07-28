/**
 * 
 */
package com.baosight.buapx.web.flow;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

public class MobileJudgementAction extends AbstractAction {
	private Logger logger=Logger.getLogger(MobileJudgementAction.class);
private String mobileReg = "\\b(applewebkit.*mobile.*)\\b";
private Pattern mobilePat = Pattern.compile(mobileReg,Pattern.CASE_INSENSITIVE);

	protected Event doExecute(final RequestContext context) {
		HttpServletRequest  request=WebUtils.getHttpServletRequest(context);
		boolean isMobile =isMobileWithRegrex(request)&&(isIphone(request)||isAndroid(request));
		if (isMobile) {
			context.getFlowScope().put("loginType", "mobileLogin");
		}

		return success();
	}

	private boolean isIphone(HttpServletRequest request) {
		boolean isIphone = false;
		if (request.getHeader("User-Agent") != null) {
				if (request.getHeader("User-Agent").toLowerCase()
						.indexOf("iphone") >= 0) {
					isIphone = true;
		}
	}
		return isIphone;
	}	
		private boolean isAndroid(HttpServletRequest request) {
			boolean isAndroid = false;
			if (request.getHeader("User-Agent") != null) {
					if (request.getHeader("User-Agent").toLowerCase()
							.indexOf("android") >= 0) {
						isAndroid = true;
			}
		}
			return isAndroid;

		}
		
		private boolean isMobileWithRegrex(HttpServletRequest request) {
			if (request.getHeader("User-Agent") != null) {
				Matcher matcherMobile = mobilePat.matcher(request.getHeader("User-Agent").toLowerCase());
                return matcherMobile.find();
	        }else{
	        	return false;

		 }
		}		
			private boolean isMobile(HttpServletRequest request) {
				boolean isMobile = false;
				if (request.getHeader("User-Agent") != null) {
						if (request.getHeader("User-Agent").toLowerCase()
								.indexOf("appbewebkit") >= 0) {
							isMobile= true;
				}
			}
				return isMobile;
		
	}

	
	
	
}