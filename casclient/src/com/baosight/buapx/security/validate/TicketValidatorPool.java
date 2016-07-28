/**
 * 
 */
package com.baosight.buapx.security.validate;

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.util.StringUtils;

/**
 * @author mai
 *
 */
public class TicketValidatorPool implements ITicketValidator{
	private Map pool;
	public TicketValidatorPool(String[] patternValidatorServerList){
		pool = new HashMap(patternValidatorServerList.length);
		for(int i = 0 ; i < patternValidatorServerList.length; i++){
			String[] serverIpPair = patternValidatorServerList[i].split(SERVER_IP_DELIMITER);
			pool.put(serverIpPair[0], new SimpleTicketValidator(serverIpPair[1]));
		}
	}
	
	public TicketValidatorPool(String propertiesValidatorServer){
		this(StringUtils.tokenizeToStringArray(propertiesValidatorServer, SERVER_DELIMITER));
	}
	public Assertion validate(String ticket, String targetPlatformUrl) throws TicketValidationException {
		String[] ticketServer = ticket.split(VALIDATOR_SERVER_DELIMITER);
		ITicketValidator ticketValidator =(ITicketValidator) pool.get(ticketServer[ticketServer.length - 1]);
		return ticketValidator.validate(ticket, targetPlatformUrl);
	}
	
	private static String VALIDATOR_SERVER_DELIMITER = "-";
	
	private static String SERVER_DELIMITER = ",";
	
	private static String SERVER_IP_DELIMITER = "@";
	
	
	
}
