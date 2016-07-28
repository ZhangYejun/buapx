package com.baosight.buapx.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.springframework.binding.message.MessageContext;
/**
 * 用于校验
 * @author mai
 *
 */
public class StringFieldUtils {
	private MessageContext messageContext;

	public StringFieldUtils(MessageContext messageContext){
		this.messageContext = messageContext;
	}
	public void rejectBlank(String field, String source, String defaultMessage){
		this.rejectField(StringUtils.isBlank(field), source, defaultMessage);
	}

	public void rejectField(boolean isFieldReject,String source, String defaultMessage){
		if(isFieldReject)
			messageContext.addMessage(MessageUtils.getErrorMessage(source, defaultMessage));
	}

}
