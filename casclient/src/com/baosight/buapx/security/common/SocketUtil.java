package com.baosight.buapx.security.common;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.CommonUtils;

import com.baosight.buapx.security.common.UrlConstructor.Encode;


/**
 * @author Mai ShangHai BAOSCAPE Information Technology Co., Ltd.
 * @date 2011-3-24
 */
public class SocketUtil {
	private static final Log log = LogFactory.getLog(SocketUtil.class);

	public static String getResponseFromServer(String constructedUrl, String encoding) {
		URLConnection conn = null;
		try {
			URL url = new URL(constructedUrl);
			conn = url.openConnection();
			BufferedReader in;
			if (CommonUtils.isEmpty(encoding))
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			else {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			}

			StringBuilder stringBuffer = new StringBuilder(255);
			String line;
			while ((line = in.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			String str1 = stringBuffer.toString();
			return str1;
		} catch (MalformedURLException e) {
			log.debug(e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.debug(e);
			throw new RuntimeException(e);
		} finally {
			if ((conn != null) && ((conn instanceof HttpURLConnection)))
				((HttpURLConnection) conn).disconnect();
		}
	}

	public static String getResponseFromServerUTF8(String constructedUrl) {
		return getResponseFromServer(constructedUrl, Encode.UTF8);
	}

}
