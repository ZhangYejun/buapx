package com.baosight.buapx.web.filter.captcha;

import java.awt.image.BufferedImage;

import com.octo.captcha.image.ImageCaptcha;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-4-25
 */
public class KaptchaCaptcha extends ImageCaptcha {

	KaptchaCaptcha(String question, BufferedImage challenge, String response, boolean caseSensitive) {
		super(question, challenge);
		this.response = response;
		this.caseSensitive = caseSensitive;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String response;
	private boolean caseSensitive;

	public final Boolean validateResponse(Object response) {
		return (((null != response) && (response instanceof String)) ? validateResponse((String) response) : Boolean.FALSE);
	}

	private final Boolean validateResponse(String response) {
		return Boolean.valueOf((this.caseSensitive) ? response.equals(this.response) : response.equalsIgnoreCase(this.response));
	}

}
