package com.baosight.buapx.web.filter.captcha;

import java.awt.image.BufferedImage;

import com.google.code.kaptcha.Producer;
import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.image.wordtoimage.WordToImage;

/**
 * 利用相应的转换器，转换字符到图像。默认实现为KaptchaWordToImage
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-4-25
 */
public class KaptchaWordToImage implements WordToImage {
	
	private Producer procedure;
	@Override
	public int getMaxAcceptedWordLength() {
		return 0;
	}

	@Override
	public int getMinAcceptedWordLength() {
		return 0;
	}

	@Override
	public int getImageHeight() {
		return 0;
	}

	@Override
	public int getImageWidth() {
		return 0;
	}

	@Override
	public int getMinFontSize() {
		return 0;
	}

	@Override
	public BufferedImage getImage(String paramString) throws CaptchaException {
		return procedure.createImage(paramString);
	}
	
	public KaptchaWordToImage(Producer procedure) {
		this.procedure = procedure;
	}
	



}
