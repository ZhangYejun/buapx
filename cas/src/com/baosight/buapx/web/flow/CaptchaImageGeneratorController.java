/**
 * 
 */
package com.baosight.buapx.web.flow;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.baosight.buapx.web.captcha.SimpleCaptchaGenerator;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 生成验证码
 * 
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-7-25
 */
public class CaptchaImageGeneratorController implements Controller, InitializingBean {
	
	   private final Logger logger = LoggerFactory.getLogger(CaptchaImageGeneratorController.class);

	@NotNull
	//private ImageCaptchaService jcaptchaService=new DefaultManageableImageCaptchaService();
	private String captchaValidationParameter = "_captcha_parameter";

	/*public void setJcaptchaService(ImageCaptchaService jcaptchaService) {
		this.jcaptchaService = jcaptchaService;
	}
*/
	public CaptchaImageGeneratorController() {
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
		/*byte captchaChallengeAsJpeg[] = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		// 获得sessionID
		String captchaId = request.getSession().getId();
		java.awt.image.BufferedImage challenge = jcaptchaService.getImageChallengeForID(captchaId, request.getLocale());
		JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
		jpegEncoder.encode(challenge);
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();*/
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0L);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		HttpSession session =request.getSession(); 
		String validateCode =SimpleCaptchaGenerator.getCertPic(60, 21, responseOutputStream);
		logger.debug("now---------------generate validateCode sessionid="+session.getId()+" ,generate valiCode="+validateCode);
		session.setAttribute(captchaValidationParameter,validateCode);
		responseOutputStream.flush();
		responseOutputStream.close();
		return null;
	}

}
