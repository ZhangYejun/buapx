package com.baosight.buapx.security.validate;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;

/**
 * 统一认证校验RPC接口
 * 
 * @author mai
 * 
 */
public interface ITicketValidator {
	public Assertion validate(String ticket, String targetPlatformUrl) throws TicketValidationException;
}
