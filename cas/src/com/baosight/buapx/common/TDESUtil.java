package com.baosight.buapx.common;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class TDESUtil {
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

	public static void main1(String[] args) throws Exception{
		String value="110819welmwlerk;l";
		Date date=new Date();
		//value=value+";"+String.valueOf(date.getTime());
		value="admin123@buapx@"+System.currentTimeMillis();
		String key="BGJSKM-OA";
		String encoded=encrypt(key,value);
		System.out.println(encoded);
		String decoded=decrypt(key,encoded);
		System.out.println(decoded);
	}

	
	public static void main(String[] args) throws Exception{
		String pkey="110819welmwlerk;l";
		Date date=new Date();
		//value=value+";"+String.valueOf(date.getTime());
		//String pwd="cocoa1";
		 String pwd ="1234qwer";
		String encoded=encrypt(pkey,pwd);
		System.out.println("encoded="+encoded);
		String plain=decrypt(pkey,encoded);
		System.out.println("plain="+plain);
	}
	/**
      * DES算法，加密
      *
      * @param data 待加密字符串
      * @param key  加密私钥，长度不能够小于8位
      * @return 加密后的字节数组，一般结合Base64编码使用
      * @throws CryptException 异常
      */

	public static String encrypt(String key,String data) {
		try {
			return encode(key, data.getBytes("UTF-8"));
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * DES算法，加密
	 *
	 * @param data 待加密字符串
	 * @param key  加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws CryptException 异常
	 */
	public static String encode(String key,byte[] data) throws Exception  {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			//key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			byte[] x = new byte[]{ (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,(byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF } ;
			IvParameterSpec iv = new IvParameterSpec(x);
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
			byte[] bytes = cipher.doFinal(data);
			return StringUtils.toHexString(bytes);
		} catch (Exception e)  {
			throw new Exception(e);
		}
	}
	/**
	 * DES算法，解密
	 *
	 * @param data 待解密字符串
	 * @param key  解密私钥，长度不能够小于8位
	 * @return 解密后的字节数组
	 * @throws Exception 异常
	 */

	public static byte[] decode(String key,byte[] data) throws Exception {
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			//key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			byte[] x = new byte[]{ (byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78,(byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF } ;
			IvParameterSpec iv = new IvParameterSpec(x);
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey,paramSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/**
	 * 获取编码后的值
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */

	public static String decrypt(String key,String data) {
		byte[] datas;
		String value = null;
		try {
			datas = decode(key, StringUtils.fromHexString(data));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		}
		value = new String(datas);
		if (value.equals("")){
			throw new RuntimeException("value null");
		}
		return value;
	}

}
