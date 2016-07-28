package com.baosight.buapx.security.properties;
/**
 * lizidi@baosight.com
 */
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.util.CommonUtils;


public class CasPropertyHelper {
	
 public static Map<String,String> constructAwaredPropertiesFromRequest(String[] awaredProperties,HttpServletRequest servletRequest){
	 Map<String,String> extraProperties=new TreeMap<String, String>();
	   
	  if(awaredProperties!=null){	
		  String temp=null;
		  for(String str:awaredProperties){
			  temp=servletRequest.getParameter(str);
			  if(!StringUtils.isEmpty(temp)){
				  extraProperties.put(str, temp); 
			  }
		  }
	  }
	  return extraProperties;
 }
 
 
 
}
