package com.baosight.buapx.web.filter;

import com.octo.captcha.service.CaptchaServiceException;

/**
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-4-6
 */
public interface ValidateById {

	/**
	 * @param id
	 * @param targetId
	 * @return
	 * @throws CaptchaServiceException
	 */
	boolean doValidate(String id, String targetId);
}
