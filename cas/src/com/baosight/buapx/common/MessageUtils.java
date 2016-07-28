/**
 * 
 */
package com.baosight.buapx.common;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageResolver;

/**
 * @author mai
 *
 */
public class MessageUtils {
	
	public static MessageResolver getErrorMessage(String source, String defaultMessage){
		return new MessageBuilder().error().source(source).defaultText(defaultMessage).build();
	}

}
