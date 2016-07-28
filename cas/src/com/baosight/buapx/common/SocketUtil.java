package com.baosight.buapx.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

//import com.baosight.buapx.security.common.UrlConstructor.Encode;
//import com.baosight.iplat4j.logging.Logger;
//import com.baosight.iplat4j.logging.LoggerFactory;

/**
 * @author Mai ShangHai BAOSCAPE Information Technology Co., Ltd.
 * @date 2011-3-24
 */
public class SocketUtil {
	// private static final Logger log =
	// LoggerFactory.getLogger(SocketUtil.class);

	public static void main(String args[]) {
		String username = "huangyufei@baosight.com";
		String password = codedPassword("password");
		System.out.println(password);
		String syscode = "wangpan";
		String baseUrl = "http://10.25.76.157:8080/cas/buapxPasswordValidate";
		String queryUrl = baseUrl + "?username=" + username + "&password="
				+ password + "&syscode=" + syscode; // 新增了接入系统代号这一参数
		String response = getResponseFromServerUTF8(queryUrl).trim();

		System.out.println(response);
		if (response.indexOf("SUCCESS") != -1) {
			String[] strs = response.split(":");
			String[] results = strs[1].split(","); // 分离出认证结果和对应的用户账号
			String result = results[0];
			String sourceUsername = results[1];
			if (result.equals("true")) { // 用户名和密码正确
				System.out.println("验证成功，用户名和密码正确");
				if (sourceUsername.equals("null")) {
					System.out.println("但是此用户没有权限使用该系统");
				} else {
					System.out.println("该用户在本系统的对应账号为: " + sourceUsername);
				}
			} else {
				if(result.equals("notActived")){
				  System.out.println("验证成功，用户未激活");
				}else{
					if(result.equals("false")){
					System.out.println("验证成功，用户名或密码错误");
					}else{
						throw new RuntimeException("无法预知的返回结果");
					}
				}
			}
		} else {
			System.out.println("认证失败，返回结果如下");
			System.out.println(response);
		}

	}

	private static MessageDigest digest = null;

	public static String getResponseFromServer(String constructedUrl,
			String encoding) {
		URLConnection conn = null;
		try {
			URL url = new URL(constructedUrl);
			conn = url.openConnection();
			BufferedReader in;
			if (StringUtils.isEmpty(encoding))
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
			else {
				in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), encoding));
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
			// log.debug(e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			// log.debug(e);
			throw new RuntimeException(e);
		} finally {
			if ((conn != null) && ((conn instanceof HttpURLConnection)))
				((HttpURLConnection) conn).disconnect();
		}
	}

	public static String getResponseFromServerUTF8(String constructedUrl) {
		return getResponseFromServer(constructedUrl, "UTF-8");
	}

	public static final synchronized String codedPassword(String password) {
		if (digest == null)
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				nsae.getStackTrace();
			}
		try {
			digest.update(password.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.getStackTrace();
		}
		return encodeHex(digest.digest());
	}

	private static final String encodeHex(byte bytes[]) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 16)
				buf.append("0");
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return buf.toString();
	}

}
