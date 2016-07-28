/**
 *
 */
package com.baosight.buapx.security.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.DocumentException;
import org.jasig.cas.client.util.CommonUtils;

import com.baosight.buapx.security.properties.BuapxClientProperties;
import com.baosight.buapx.security.properties.PropertiesManagerContainer;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-12-19
 */
public class UrlConstructor {

	public static final String ORIGINAL_TARGET_URI = "originalTargetUri";
	public static final String FORCE_TARGET_URI = "forceTargetUri";

	public static String constructServiceExternal(
			final String originalTargetUri) {
		BuapxClientProperties buapxClientProperties;
		try {
			buapxClientProperties = (BuapxClientProperties) PropertiesManagerContainer
					.getProperties(BuapxClientProperties.class);

			String serviceUrl = constructServiceUrl(buapxClientProperties,
					new HashMap(), originalTargetUri, buapxClientProperties.getEncoding());
			return URLEncoder.encode(serviceUrl, buapxClientProperties.getEncoding());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String constructCasLoginUrl(
			final BuapxClientProperties buapxClientProperties,
			final Map casExtOptions, final Map awareServiceProperties,
			final String originalTargetUri, final String encode) {

		StringBuffer strBuf = new StringBuffer(255);
		try {
			String serviceStr = constructServiceUrl(buapxClientProperties,
					awareServiceProperties, originalTargetUri, encode);

			strBuf.append("service=" + URLEncoder.encode(serviceStr, encode));

			if (casExtOptions != null) {
				Iterator it = casExtOptions.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry) it.next();
					strBuf.append("&")
							.append(e.getKey() + "=")
							.append(URLEncoder.encode((String) e.getValue(),
									encode));
				}
			}

			if (buapxClientProperties.getExtParams() != null) {
				String[] extParams = buapxClientProperties.getExtParams();
				for (int i = 0; i < extParams.length; i++) {
					strBuf.append("&").append(extParams[i]);
				}
			}

			String loginUrl = buapxClientProperties.getLoginUrl();
			String result = loginUrl
					+ (loginUrl.indexOf("?") != -1 ? "&" : "?")
					+ strBuf.toString();
			if(buapxClientProperties.isRemoteLogin()){
				String remoteLoginPage=buapxClientProperties.getPlatAddress()+buapxClientProperties.getLoginPage();
				result=result+"&remoteLoginPage="+URLEncoder.encode(remoteLoginPage, "UTF-8");
			}
			return result;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	public static String constructServiceUrl(
			final BuapxClientProperties buapxClientProperties,
			final Map<String, String> extraServiceProperties,
			final String originalTargetUri, String encode) {
		String service = buapxClientProperties.getPlatAddress()
				+ buapxClientProperties.getAppCallBack();
		service = service + "?syscode=" + buapxClientProperties.getPlatName();
		if (extraServiceProperties != null) {
			for (Map.Entry<String, String> pro : extraServiceProperties
					.entrySet()) {
				service = service + (service.indexOf("?") != -1 ? "&" : "?")
						+ pro.getKey() + "=" + pro.getValue();
			}
		}

		try {
			service = service + (service.indexOf("?") != -1 ? "&" : "?")
					+ ORIGINAL_TARGET_URI + "="
					+ URLEncoder.encode(originalTargetUri, encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return service;
	}

	public static String constructCasLoginUrlUTF8(
			final BuapxClientProperties buapxClientProperties,
			final Map casExtOptions, final Map awareServiceProperties,
			final String originalTargetUri) {
		return constructCasLoginUrl(buapxClientProperties, casExtOptions,
				awareServiceProperties, originalTargetUri, Encode.UTF8);
	}

	public static class Encode {
		public static final String UTF8 = "UTF-8";
	}

	public static String constructUrl(String url, Map<String, String> parameters) {
		if (parameters != null) {
			for (Map.Entry<String, String> pro : parameters.entrySet()) {
				url = url + (url.indexOf("?") != -1 ? "&" : "?") + pro.getKey()
						+ "=" + pro.getValue();

			}
		}
		return url;
	}

	public static void main(String args[]) throws UnsupportedEncodingException {
		/*
		 * String service1=
		 * "http%3A%2F%2Fpt.baogang.info%2Fcas_callback.jsp%3Fsyscode%3Dbsw%26sendToSource%3Dtrue%26originalTargetUri%3Dhttp%253A%252F%252Fsp.baogang.info%253A9082%252FDispatchAction.do%253FefFormEname%253DSPSQ0731%2526serviceName%253DSPSQ0001%2526methodName%253DqueryPageHome%2526shudiParam%253DshudiParam%2526sendToSource%253Dtrue"
		 * ; String original=
		 * "http%3A%2F%2Fsp.baogang.info%3A9082%2FDispatchAction.do%3FefFormEname%3DSPSQ0731%26serviceName%3DSPSQ0001%26methodName%3DqueryPageHome%26shudiParam%3DshudiParam%26sendToSource%3Dtrue"
		 * ; String redirectUrl=
		 * "https://cas.baogang.info/cas/login?service=http%3A%2F%2Fwww.baogang.info%2Fcas_callback.jsp%3Fsyscode%3Dbsw%26originalTargetUri%3Dhttp%253A%252F%252Fwww.baogang.info%253A9080%252Findex2.jsp"
		 * ; System.out.println(URLDecoder.decode(redirectUrl,"UTF-8"));
		 */
		PropertiesManagerContainer.init("buapx_cas-client.xml");
		String url = UrlConstructor.constructServiceExternal(
				"http://www.sina.com/test?param1=1&param2=2");
		System.out.println(URLEncoder.encode("http://localhost:8080/test/illegalUserPage","UTF-8"));
	}
}
