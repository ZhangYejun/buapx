/**
 * 
 */
package com.baosight.buapx.common;

/**
 * @author mai
 *
 */
public class RequestParamsUtil {
	/**
	 * Remove all the same name parameters from request query string.
	 * @param requestQueryString
	 * @param parameterName
	 * @return
	 */
	public static String removeParameter(final String requestQueryString, final String parameterName){
		String str = "&"+parameterName+"=(\\w)*|^"+parameterName+"=(\\w)*";
		return requestQueryString.replaceAll(str, "");
	}

}
