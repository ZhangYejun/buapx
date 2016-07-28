/**
 * 
 */
package com.baosight.buapx.web.filter.captcha;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.code.kaptcha.util.Config;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.ComposeDictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;

/**
 * 
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-4-25
 */
public class KaptchaEngine extends ListImageCaptchaEngine {

	@Override
	protected void buildInitialFactories() {
		InputStream in = KaptchaEngine.class.getResourceAsStream("/properties" + File.separator + "theme" + File.separator + "kaptcha.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Config config = new Config(properties);

		WordGenerator dictionnaryWords = new ComposeDictionaryWordGenerator(new FileDictionary("toddlist"));

		WordToImage word2image = new KaptchaWordToImage(null);

		addFactory(new KaptchaImageCaptchaFactory(dictionnaryWords, word2image, true));
	}



}
