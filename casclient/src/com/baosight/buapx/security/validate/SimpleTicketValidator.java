package com.baosight.buapx.security.validate;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

/**
 * @author mai
 *
 */
public class SimpleTicketValidator implements ITicketValidator {

	TicketValidator ticketValidator;

	public SimpleTicketValidator(String validateUrl) {
		this.ticketValidator = new Cas20ServiceTicketValidator(validateUrl);
	}

	public Assertion validate(String ticket, String targetPlatformUrl) throws TicketValidationException {
		return this.ticketValidator.validate(ticket, targetPlatformUrl);
	}

}
