/**
 * 
 */
package com.baosight.buapx.security.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class EncrypUtil {
	private static MessageDigest digest = null;

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
	
	public static void main(String[] args){
		System.out.println(codedPassword("ABCDE12345"));
	}
}
