package com.baosight.buapx.cas.authentication.principal.validator;

import javax.naming.ldap.LdapContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;




import com.baosight.buapx.cas.authentication.principal.BuapxUsernamePasswordCredentials;
import com.baosight.buapx.common.StringFieldUtils;
/**
 * 验证表单
 * @author mai
 *
 */
public class BuapxUsernamePasswordCredentialsValidator {
	final static Logger log = LoggerFactory.getLogger(BuapxUsernamePasswordCredentialsValidator.class);
	/**
	 *
	 * @param certUser
	 * @param context
	 */
	public void validateViewLoginForm(BuapxUsernamePasswordCredentials certUser, ValidationContext context) {
		MessageContext messages = context.getMessageContext();
		StringFieldUtils fieldUtil = new StringFieldUtils(messages);
		boolean result=false;
		boolean isBlank=StringUtils.isEmpty(certUser.getUsername());
				 
        if(isBlank){
        	fieldUtil.rejectBlank("", "required.username", "输入不能为空");
        }

		log.debug("{}",certUser);

	}

	public void validateViewMobileForm(BuapxUsernamePasswordCredentials certUser, ValidationContext context) {
		MessageContext messages = context.getMessageContext();
		StringFieldUtils fieldUtil = new StringFieldUtils(messages);
		boolean result=false;
		boolean isBlank=StringUtils.isEmpty(certUser.getUsername())||StringUtils.isEmpty(certUser.getUsername());
        if(isBlank){
        	fieldUtil.rejectBlank("", "required.username", "输入不能为空");
        }

		log.debug("{}",certUser);

	}




	public void validateViewIframeForm(BuapxUsernamePasswordCredentials certUser, ValidationContext context) {
		validateViewLoginForm(certUser, context);
	}

	public void validateViewMixForm(BuapxUsernamePasswordCredentials certUser, ValidationContext context) {
		validateViewLoginForm(certUser, context);
	}





}
