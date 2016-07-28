/**
 * 
 */
package com.baosight.buapx.web.flow;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.baosight.buapx.web.captcha.SimpleCaptchaGenerator;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 校验验证码
 * 
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-7-25
 */
public class CaptchaCheckController implements Controller, InitializingBean {

	@NotNull
	//private ImageCaptchaService jcaptchaService=new DefaultManageableImageCaptchaService();
	private String captchaValidationParameter = "_captcha_parameter";

	/*public void setJcaptchaService(ImageCaptchaService jcaptchaService) {
		this.jcaptchaService = jcaptchaService;
	}
*/
	public CaptchaCheckController() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String imgNum = request.getParameter("num");
		String sessionNum = (String)request.getSession().getAttribute(captchaValidationParameter);
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0L);
		
		ServletOutputStream responseOutputStream = response.getOutputStream();
		if(sessionNum.equals(imgNum)){
			responseOutputStream.print("true");
		}else{
			responseOutputStream.print("false");
		}
		responseOutputStream.flush();
		responseOutputStream.close();
		return null;
		
	}

}
