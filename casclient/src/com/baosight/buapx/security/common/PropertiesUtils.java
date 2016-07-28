package com.baosight.buapx.security.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-12-28
 */
public class PropertiesUtils {

	private static Log log = LogFactory.getLog(PropertiesUtils.class);
	public static String[] getValue(String fileName, String key) {
		InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
		try {
			PropertyResourceBundle rb = new PropertyResourceBundle(in);
			String[] s = rb.getString(key).split(",");
			in.close();
			return s;
		} catch (IOException e) {
			if(log.isDebugEnabled())
				log.debug(e.getMessage());
		}
		return new String[0];
	}

}
