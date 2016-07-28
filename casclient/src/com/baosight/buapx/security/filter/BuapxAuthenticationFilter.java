package com.baosight.buapx.security.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.jasig.cas.client.util.CommonUtils;

import com.baosight.buapx.security.common.AntPathMatcher;
import com.baosight.buapx.security.common.ClassUtils;
import com.baosight.buapx.security.common.ConstString;
import com.baosight.buapx.security.common.CookieUtil;
import com.baosight.buapx.security.common.PathMatcher;
import com.baosight.buapx.security.common.UrlConstructor;
import com.baosight.buapx.security.handler.HandlerUtils;
import com.baosight.buapx.security.handler.IAuthPostHandler;
import com.baosight.buapx.security.loginbiz.UserRedirectMananger;
import com.baosight.buapx.security.exception.BuapxAuthException;
import com.baosight.buapx.security.exception.ClassInstanceException;
import com.baosight.buapx.security.properties.BuapxClientProperties;
import com.baosight.buapx.security.properties.PropertiesManagerContainer;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;
import com.baosight.buapx.security.validate.BuapxAuthValidateManager;
import com.baosight.buapx.security.validate.IBuapxValidate;

/**
 *
 * @author lizidi@baosight.com
 *
 */
public class BuapxAuthenticationFilter implements Filter {
	private Log log = LogFactory.getLog(BuapxAuthenticationFilter.class);

	public void destroy() {

	}

	public void doFilter(ServletRequest _request, ServletResponse _response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) _request;
		HttpServletResponse response = (HttpServletResponse) _response;

		// 判断是否是回调地址
		if (this.isCallBackUri(request.getServletPath())) {
			log.debug("in call_back.jsp.......................,para="+request.getQueryString());
			// 验证成功，则直接客户端重定向到默认首页
			SecurityUserInfo userInfo;
			try {
				if (buapxClientProperties.getClientType().toLowerCase()
						.equals(ConstString.CLIENT_TYPE_LTPA)) {
					String user = request.getRemoteUser();
					if (StringUtils.isEmpty(user)) {
						throw new BuapxAuthException("获取LTPA用户失败");
					}
					userInfo = new SecurityUserInfo(user, null,null);

					request.getSession().setAttribute(ConstString.SESSION_URL,
							userInfo);

				} else {
					log.debug("Prepare to connect to CAS auth tickect.....");
					userInfo = buapxValidate.auth(request, response);
					log.debug("CAS auth tickect ok,return casuser="+userInfo.getCasUser()+" bizsysUser="+userInfo.getUserName());
				}
 
			} catch (BuapxAuthException e) {
				onFailureAuth(request, response);
				return;
			}
  
			onSuccessAuth(request, response, userInfo);
			return;

		} else {
			if (isReloginUri(request.getServletPath())) {
				reloginFilter.doFilter(request, response, chain);
			} else {
				String servletPath = request.getServletPath();
				if (StringUtils.isEmpty(servletPath)) {
					servletPath = request.getRequestURI();
				}
				log.debug("preppare to execute  securityFilter.doFilter");
				securityFilter.doFilter(_request, _response, chain);
				return;
			}

		}
		return;

	}

	private void onSuccessAuth(HttpServletRequest request,
			HttpServletResponse response, SecurityUserInfo userInfo)
			throws IOException {
		log.debug("in onSuccessAuth, to execute.....");
		for (int i = 0; i < this.postHandlers.size(); ++i) {
			IAuthPostHandler handler = (IAuthPostHandler) this.postHandlers
					.get(i);
			try {
				handler.handle(request, response, userInfo, true);
			} catch (Exception e) {
				log.debug("登录后处理[" + handler.getClass().getName() + "]发生异常");
				e.printStackTrace();
			}
		}

		if (buapxClientProperties.getIsCluster()) {
			CookieUtil.addCookie(response,
					this.buapxClientProperties.getDomain(),
					this.buapxClientProperties.getPlatName()
							+ ConstString.SESSION_URL, (new Date()).toString(),
					0);
		}
		CookieUtil.addCookie(response, this.buapxClientProperties.getDomain(),
				this.buapxClientProperties.getPlatName()
						+ ConstString.CAS_USER_ATTRIBUTE_NAME,
				userInfo.getCasUser(), 0);
		sendRedirectOnSuccess(request, response, userInfo);
	}

	private void onFailureAuth(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		for (int i = 0; i < this.postHandlers.size(); ++i) {
			IAuthPostHandler handler = (IAuthPostHandler) this.postHandlers
					.get(i);
			try {
				handler.handle(request, response, null, false);
			} catch (Exception e) {
				log.debug("登录后处理[" + handler.getClass().getName() + "]发生异常");
				e.printStackTrace();
			}
		}
		forwardToErrorPage(request, response);

	}

	/**
	 * 验证成功，重定向到目标地址
	 *
	 * @param request
	 * @param response
	 * @param url
	 * @throws IOException
	 */
	protected void sendRedirectOnSuccess(HttpServletRequest request,
			HttpServletResponse response, SecurityUserInfo userInfo)
			throws IOException {
		String url = this.buapxClientProperties.getAppDefaultTargetUrl();
		String targetUri = request
				.getParameter(UrlConstructor.ORIGINAL_TARGET_URI);
		// if (!CommonUtils.isBlank(targetUri))
		// targetUri = URLDecoder.decode(targetUri, "UTF-8");
		if ((!(url.startsWith("http://"))) && (!(url.startsWith("https://")))) {
			url = request.getContextPath() + url;
		}
		if ((!(targetUri.startsWith("http://")))
				&& (!(targetUri.startsWith("https://")))) {
			targetUri = request.getContextPath() + targetUri;
		}

		if (buapxClientProperties.getRedirectToOriginal()) {
			//response.sendRedirect(response.encodeRedirectURL(targetUri));
			//return;
			url=response.encodeRedirectURL(targetUri);
		}

		String sendToSource = request.getParameter("sendToSource");
		if (!StringUtils.isEmpty(sendToSource)
				&& sendToSource.equalsIgnoreCase("true")) {
			response.sendRedirect(response.encodeRedirectURL(targetUri));
			return;
		}

		/*
		 * //获取在handle中强制规定的跳转地址 Object
		 * forceTargetUri=request.getSession().getAttribute
		 * (UrlConstructor.FORCE_TARGET_URI); if(forceTargetUri!=null){
		 * url=(String)forceTargetUri; }
		 */

		// 业务跳转逻辑
		String bizUrl = userRedirectManager.constructRedirectUrl(request,
				userInfo.getUserName(), url);

		// 是否已在集群其他节点登录。若是，则不需要走登录跳转逻辑
		if (buapxClientProperties.getIsCluster()) {
			String hasLoginInClusterCookie = CookieUtil.getCookieByName(
					request, this.buapxClientProperties.getPlatName()
							+ ConstString.SESSION_URL);
			boolean hasLoginInCluster = StringUtils
					.isEmpty(hasLoginInClusterCookie) ? false : true;
			if (!hasLoginInCluster) {
				if (!StringUtils.isEmpty(bizUrl)) {
					url = bizUrl;
				}
			}
		} else {
			if (!StringUtils.isEmpty(bizUrl)) {
				url = bizUrl;
			}
		}

		Map<String, String> awaredServiceProperties = constructAwaredServicePropertiesFromRequest(
				buapxClientProperties, request);
		url = UrlConstructor.constructUrl(url, awaredServiceProperties);
		log.debug("Lastly  response.sendRedirect="+url);
		response.sendRedirect(response.encodeRedirectURL(url));
	}

	/**
	 * 测试是否有返回地址
	 *
	 * @param servletPath
	 * @return
	 */
	private boolean isCallBackUri(String servletPath) {
		return this.callBackUrl.equals(servletPath);

	}

	private boolean isReloginUri(String servletPath) {
		return this.reloginUri.equals(servletPath);

	}

	public void init(FilterConfig config) throws ServletException {
		log.trace("初始化统一认证客户端...");
		String fileName = config.getInitParameter("configFile");
		fileName = CommonUtils.isBlank(fileName) ? "buapx_cas-client.xml"
				: fileName;
		log.trace("尝试使用配置文件：" + fileName);
		PropertiesManagerContainer.init(fileName);

		try {
			buapxClientProperties = (BuapxClientProperties) PropertiesManagerContainer
					.getProperties(BuapxClientProperties.class);
		} catch (IOException e) {
			if (log.isErrorEnabled())
				log.error(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			e.printStackTrace();
			if (log.isErrorEnabled())
				log.error(e);
			throw new RuntimeException(e);
		}

		// 初始化callbackUri
		if (log.isTraceEnabled())
			log.trace("配置回调入口.");
		this.callBackUrl = buapxClientProperties.getAppCallBack();

		if (log.isTraceEnabled())
			log.trace("配置重登陆入口.");
		this.reloginUri = buapxClientProperties.getAppRelogin();

		if (log.isDebugEnabled())
			log.debug("callback url:" + this.callBackUrl);

		this.buapxValidate = new BuapxAuthValidateManager();
		log.trace("初始化验证器...");
		this.buapxValidate.init(config);

		this.securityFilter = new BuapxSecurityFilter();
		log.trace("初始化安全过滤器...");
		this.securityFilter.init(config);

		this.reloginFilter = new BuapxReloginFilter();
		log.trace("初始化重登陆过滤器...");
		this.reloginFilter.init(config);

		initHandlers(buapxClientProperties.getPostHandlers());

		initRedirectManager(buapxClientProperties.getUserRedirectManager());

		log.trace("统一认证客户端初始化完成.");

	}

	/**
	 * Handler初始化
	 *
	 * @param fileName
	 */
	private void initHandlers(final String[] strHandlers) {

		// 初始化验证失败调用的handler
		if (log.isTraceEnabled())
			log.trace("初始化post handlers...");
		try {
			this.postHandlers = HandlerUtils.getHandler(strHandlers,
					IAuthPostHandler.class);

		} catch (ClassInstanceException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage());
			throw new RuntimeException(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("post handlers:" + this.postHandlers.size());
		}

	}

	private void initRedirectManager(String manager) {

		if (log.isTraceEnabled())
			log.trace("初始化userRedirectMananger...");
		try {
			this.userRedirectManager = (UserRedirectMananger) ClassUtils
					.getInstance(manager, UserRedirectMananger.class);

		} catch (ClassInstanceException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage());
			throw new RuntimeException(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("post handlers:" + this.postHandlers.size());
		}

	}

	private void forwardToErrorPage(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String url = buapxClientProperties.getAuthErrorPage();
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> constructAwaredServicePropertiesFromRequest(
			BuapxClientProperties buapxClientProperties,
			HttpServletRequest servletRequest) {
		Map<String, String> extraServiceProperties = new TreeMap<String, String>();
		// 额外放入service属性的参数值
		String[] awaredServieProperties = buapxClientProperties
				.getAwaredServiceProperties();

		if (awaredServieProperties != null) {
			String temp = null;
			for (String str : awaredServieProperties) {
				temp = servletRequest.getParameter(str);
				if (!StringUtils.isEmpty(temp)) {
					extraServiceProperties.put(str, temp);
				}
			}
		}
		return extraServiceProperties;
	}

	private IBuapxValidate buapxValidate;

	private Filter securityFilter;

	private Filter reloginFilter;

	private BuapxClientProperties buapxClientProperties;

	private String callBackUrl;

	private String reloginUri;

	private List<IAuthPostHandler> postHandlers;

	private UserRedirectMananger userRedirectManager;

	private PathMatcher pathMarch = new AntPathMatcher();

/*	private boolean isUrlInExceptEncodingFilterList(String url) {
		if(StringUtils.isEmpty(buapxClientProperties.getEncodingFilter())){
			return false;
		}
		String[] exceptFilterUrlPattern = buapxClientProperties
				.getEncodingFilterExcludeUrl();
		for (int i = 0; i < exceptFilterUrlPattern.length; i++) {
			if (pathMarch.match(exceptFilterUrlPattern[i], url.toLowerCase()))
				return true;
		}
		return false;
	}*/

}
