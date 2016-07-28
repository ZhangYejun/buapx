/**
 * 
 */
package com.baosight.buapx.web.filter;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.image.gimpy.DefaultGimpyEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-4-6
 */
public class ImageCapchaFilter extends OncePerRequestFilter implements ValidateById {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public void setCaptchaSotre(CaptchaStore captchaSotre) {
		this.captchaSotre = captchaSotre;
	}

	public void setMinGuarantedStorageDelayInSeconds(int minGuarantedStorageDelayInSeconds) {
		this.minGuarantedStorageDelayInSeconds = minGuarantedStorageDelayInSeconds;
	}

	public void setMaxCaptchaStoreSize(int maxCaptchaStoreSize) {
		this.maxCaptchaStoreSize = maxCaptchaStoreSize;
	}

	public void setCaptchaStoreLoadBeforeGarbageCollection(int captchaStoreLoadBeforeGarbageCollection) {
		this.captchaStoreLoadBeforeGarbageCollection = captchaStoreLoadBeforeGarbageCollection;
	}

	public void setCaptchaEngine(CaptchaEngine captchaEngine) {
		this.captchaEngine = captchaEngine;
	}

	private CaptchaStore captchaSotre = new FastHashMapCaptchaStore();

	private int minGuarantedStorageDelayInSeconds = 180;

	private int maxCaptchaStoreSize = 100000;

	private int captchaStoreLoadBeforeGarbageCollection = 75000;

	private CaptchaEngine captchaEngine = new DefaultGimpyEngine();

	private GenericManageableCaptchaService service;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		response.setDateHeader("Expires", 0L);

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		response.setHeader("Pragma", "no-cache");

		response.setContentType("image/jpeg");

		BufferedImage bi = service.getImageChallengeForID(request.getSession(true).getId());
		ServletOutputStream out = response.getOutputStream();

		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		if (service == null) {
			this.service = new GenericManageableCaptchaService(captchaEngine, captchaStoreLoadBeforeGarbageCollection, captchaStoreLoadBeforeGarbageCollection,
					captchaStoreLoadBeforeGarbageCollection);
		}
	}

	public boolean doValidate(String id, String userCaptchaResponse) {
		boolean validated = false;
		try {
			validated = service.validateResponseForID(id, userCaptchaResponse).booleanValue();
		} catch (CaptchaServiceException e) {
			log.error("Error",e);
		}
		return validated;
	}

}
