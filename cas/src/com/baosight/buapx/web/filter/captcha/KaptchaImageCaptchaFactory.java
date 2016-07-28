/**
 * 
 */
package com.baosight.buapx.web.filter.captcha;

import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.Random;

import com.octo.captcha.Captcha;
import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.CaptchaQuestionHelper;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.Gimpy;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-4-25
 */
public class KaptchaImageCaptchaFactory extends ImageCaptchaFactory {

	private Random myRandom;
	public WordToImage getWordToImage() {
		return wordToImage;
	}

	public WordGenerator getWordGenerator() {
		return wordGenerator;
	}

	private WordToImage wordToImage;
	private WordGenerator wordGenerator;
	private boolean caseSensitive;
	 public static final String BUNDLE_QUESTION_KEY = KaptchaImageCaptchaFactory.class.getName();

	@Override
	public ImageCaptcha getImageCaptcha() {

		return getImageCaptcha(Locale.getDefault());
	}

	@Override
	public ImageCaptcha getImageCaptcha(Locale locale) {
		Integer wordLength = getRandomLength();

		String word = getWordGenerator().getWord(wordLength, locale);

		BufferedImage image = null;
		try {
			image = getWordToImage().getImage(word);
		} catch (Throwable e) {
			throw new CaptchaException(e);
		}

		ImageCaptcha captcha = new KaptchaCaptcha(CaptchaQuestionHelper.getQuestion(locale, BUNDLE_QUESTION_KEY), image, word, this.caseSensitive);

		return captcha;
	}

	protected Integer getRandomLength() {
		int range = getWordToImage().getMaxAcceptedWordLength() - getWordToImage().getMinAcceptedWordLength();

		int randomRange = (range != 0) ? this.myRandom.nextInt(range + 1) : 0;
		Integer wordLength = new Integer(randomRange + getWordToImage().getMinAcceptedWordLength());

		return wordLength;
	}
	
	public KaptchaImageCaptchaFactory(WordGenerator wordGenerator,WordToImage word2Image,boolean caseSensitive){
		this.caseSensitive = caseSensitive;
		this.wordGenerator = wordGenerator;
		this.wordToImage = word2Image;
	}

}
