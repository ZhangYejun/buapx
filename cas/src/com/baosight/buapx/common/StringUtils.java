package com.baosight.buapx.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringUtils {
	public static long ConvertToLong(String s) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException ne) {
			return 0;
		}
	}

	public static int converToInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException ne) {
			return 0;
		}
	}

	public static byte[] toUTF8Bytes(String s) {
		if (s == null)
			return new byte[0];
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new byte[0];
		}
	}

	public static String fromUTF8Bytes(byte[] b) {
		try {
			String s = new String(b, 0, b.length, "UTF-8");
			return s;
		} catch (UnsupportedEncodingException e) {
			return "";
		}

	}

	public static String toBase64String(byte[] b) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(b);
	}

	public static byte[] fromBase64String(String s) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(s);
		} catch (IOException e) {
			return new byte[0];
		}
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			if ((b[i] & 255) < 16)
				sb.append("0");
			sb.append(Long.toString(b[i] & 255, 16));
		}
		return sb.toString();
	}

	public static byte[] fromHexString(String s) {
		char[] c = s.toCharArray();
		byte[] b = new byte[c.length / 2];
		for (int i = 0; i < b.length; i++) {
			int newByte = 0;
			newByte |= Character.digit(c[2*i], 16);
			newByte <<= 4;
			newByte |= Character.digit(c[2*i+1], 16);
			b[i] = (byte) newByte;
		}
		return b;
	}

	public static void main(String[] args) {
		byte[] t = new byte[] { (byte) 0x0F,
				(byte) 0xAA };
		String x = StringUtils.toHexString(t);
		byte[] b = StringUtils.fromHexString(x);
		for(int i=0;i<t.length;i++){

			System.out.print(Integer.toHexString(b[i]));
			System.out.print(",");
			System.out.println(Integer.toHexString(t[i]));
		}
	}
}