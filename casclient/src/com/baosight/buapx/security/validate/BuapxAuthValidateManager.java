package com.baosight.buapx.security.validate;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;

import com.baosight.buapx.security.common.ConstString;
import com.baosight.buapx.security.common.UrlConstructor;
import com.baosight.buapx.security.properties.BuapxClientProperties;
import com.baosight.buapx.security.properties.CasPropertyHelper;
import com.baosight.buapx.security.properties.PropertiesManagerContainer;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;

public class BuapxAuthValidateManager extends AuthValidateManager implements IBuapxValidate {

	//private final static String HANDLER_KEY = "handlerConfig";

	private Log log = LogFactory.getLog(BuapxAuthValidateManager.class);

	/**
	 * Filter初始化
	 */
	public void init(FilterConfig config) {

		if (log.isInfoEnabled()) {
			log.info("初始化统一认证验证器环境...");
		}

		try {
			this.buapxClientProperties = (BuapxClientProperties) PropertiesManagerContainer.getProperties(BuapxClientProperties.class);
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);

		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (log.isTraceEnabled())
			log.trace("配置认证者.");
		this.ticketValidator = TicketValidatorFactory.getTicketValidator((buapxClientProperties.getValidateUrl()));



	}









	private ITicketValidator ticketValidator;




	private BuapxClientProperties buapxClientProperties;





	/**
	 * 验证票据
	 */
	protected SecurityUserInfo authNow(HttpServletRequest request) throws TicketValidationException {
		String ticket = request.getParameter(TICKET);
		String originalTargetUrl = request.getParameter(UrlConstructor.ORIGINAL_TARGET_URI);

		if(log.isDebugEnabled())
			log.debug("票据验证:"+ticket);

		 Map awareServiceProperties=CasPropertyHelper.constructAwaredPropertiesFromRequest(buapxClientProperties.getAwaredServiceProperties(),request);
		String service=UrlConstructor.constructServiceUrl(buapxClientProperties, awareServiceProperties, originalTargetUrl, "UTF-8");
		Assertion assertion=null;
		try{
			assertion = this.ticketValidator.validate(ticket, service);
		}catch(Exception e){
			e.printStackTrace();
			throw new TicketValidationException(e);
		}
		if (log.isDebugEnabled()) {
			log.debug("票据验证成功，当前统一认证登录用户为" + assertion.getPrincipal().getName());
			log.debug("尝试获得映射用户...");
		}

		String user= assertion.getPrincipal().getName();
		String casUser= (String) assertion.getPrincipal().getAttributes().get(ConstString.CAS_USER_ATTRIBUTE_NAME);
		String userType= (String) assertion.getPrincipal().getAttributes().get(ConstString.CAS_USERTYPE_ATTRIBUTE_NAME);
		SecurityUserInfo sc = new SecurityUserInfo(user,casUser,userType);
		return sc;


	}



	/**
	 * 推送到预定的错误页面
	 *//*
	protected String loadErrorPage() {
		return this.buapxClientProperties.getAuthErrorPage();
	}
*/
}
