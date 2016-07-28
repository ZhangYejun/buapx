package com.baosight.buapx.security.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if ((!(ignoreEmptyTokens)) || (token.length() > 0)) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return ((String[]) collection.toArray(new String[collection.size()]));
	}
	
	public static void main(String[] s){
		String[] sx = tokenizeToStringArray("Hello:world!,xxx:dfe",",");
		for(int i = 0 ;  i < sx.length; i++)
		System.out.println(sx[i]);
	}

}
