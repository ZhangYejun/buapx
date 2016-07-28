/**
 * 
 */
package com.baosight.buapx.security.validate;

import org.springframework.util.StringUtils;

/**
 * @author mai
 *
 */
public class TicketValidatorFactory {
	
	private TicketValidatorFactory(){}
	
	public static ITicketValidator getTicketValidator(String[] serverList){
		//String[] serverList = StringUtils.tokenizeToStringArray(propertyValidatorServerString, ",");
		if(serverList.length == 1 && (serverList[0].indexOf("@") == -1)){
			return new SimpleTicketValidator(serverList[0]);
		}
		if(serverList.length >=1){
			return new TicketValidatorPool(serverList);
		}
		throw new RuntimeException("初始化统一认证验证地址失败");

		
	}

}
