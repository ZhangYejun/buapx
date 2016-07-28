package com.baosight.buapx.ua.auth.common;


import org.apache.commons.codec.digest.DigestUtils;


/**
 * 相关密码算法
 *
 * @author eCocoa
 *
 */
public class EncryptUtils {


	/**
	 * MD5加密
	 *
	 * @param originalString
	 * @return
	 */
	private static String md5Hex(final String originalString) {
		return DigestUtils.md5Hex(originalString);
	}

	/**
	 * Sha算法加密
	 *
	 * @param originalString
	 * @return
	 */
	private static String shaHex(final String originalString) {
		return DigestUtils.shaHex(originalString);
	}

	/**
	 * 加密原始密码
	 *
	 * @param originalPassword
	 * @return
	 */
	public static String passwordEncode(final String originalPassword) {
		return shaHex(md5Hex(originalPassword));
	}

	public static void main(String args[]){
		System.out.println(passwordEncode("ABCDE12345"));
	}

}
