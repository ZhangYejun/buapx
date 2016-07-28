package com.baosight.buapx.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtil{
private static final String DES_ALGORITHM = "DES";

  /**
   * DES加密
   * @param plainData
   * @param secretKey
   * @return
   * @throws Exception
   */
  public static String encrypt(String plainData, String secretKey){
	  Security.insertProviderAt(
				new com.sun.crypto.provider.SunJCE(),1);
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance(DES_ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));
      // 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length must be multiple of 8 when decrypting with padded cipher异常，
      // 不能把加密后的字节数组直接转换成字符串
      byte[] buf = cipher.doFinal(plainData.getBytes("UTF-8"));
      return parseByte2HexStr(buf);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("BadPaddingException", e);
    }

  }

  /**
   * DES解密
   * @param secretData
   * @param secretKey
   * @return
   * @throws Exception
   */
  public static String decrypt(String secretData, String secretKey) {
	  Security.insertProviderAt(
				new com.sun.crypto.provider.SunJCE(),1);
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance(DES_ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));
      System.out.println("=================secrectData   "+secretData);
      byte[] buf = cipher.doFinal(parseHexStr2Byte((secretData)));
      return new String(buf);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("BadPaddingException", e);
    }
  }


  /**
   * 获得秘密密钥
   *
   * @param secretKey
   * @return
   * @throws NoSuchAlgorithmException
   */
 /* private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException{



    // 为我们选择的DES算法生成一个KeyGenerator对象
    KeyGenerator kg = null;
    try {
    	SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(secretKey.getBytes("UTF-8"));
      kg = KeyGenerator.getInstance(DES_ALGORITHM);
      kg.init(secureRandom);
      return kg.generateKey();
    } catch (Exception e) {
    	e.printStackTrace();
    	throw new RuntimeException(e);
    }
    //kg.init(56, secureRandom);

    // 生成密钥
  }*/

  /**
	 * 获得密钥
	 *
	 * @param secretKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 */
	private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException,InvalidKeyException,InvalidKeySpecException{

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM,new com.sun.crypto.provider.SunJCE());
		DESKeySpec keySpec;
		try {
			keySpec = new DESKeySpec(secretKey.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
	    	throw new RuntimeException(e);
		}
		keyFactory.generateSecret(keySpec);
		return keyFactory.generateSecret(keySpec);
	}

  /*public static void main(String[] a) throws Exception{
    String input = "cy11Xlbrmzyh:604:301:1353064296";
    String key = "37d5aed075525d4fa0fe635231cba447";

    DESEncryptTest des = new DESEncryptTest();

    String result = des.encryption(input, key);
    System.out.println(result);

    System.out.println(des.decryption(result, key));

  }*/

	/**
	 * 将二进制转换成16进制
	 *
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}