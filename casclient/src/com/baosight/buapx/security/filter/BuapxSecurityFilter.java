package com.baosight.buapx.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.jasig.cas.client.util.CommonUtils;

import com.baosight.buapx.security.common.AntPathMatcher;
import com.baosight.buapx.security.common.ConstString;
import com.baosight.buapx.security.common.PathMatcher;
import com.baosight.buapx.security.common.UrlConstructor;
import com.baosight.buapx.security.properties.BuapxClientProperties;
import com.baosight.buapx.security.properties.CasPropertyHelper;
import com.baosight.buapx.security.properties.PropertiesManagerContainer;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-12-29
 */
public class BuapxSecurityFilter implements Filter {

	private Log log = LogFactory.getLog(BuapxSecurityFilter.class);
	private PathMatcher pathMarch = new AntPathMatcher();


	public void destroy() {

	}

	/**
	 * 过滤
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if(!StringUtils.isEmpty(buapxClientProperties.getEncodingFilter())){
			servletRequest.setCharacterEncoding(buapxClientProperties.getEncodingFilter());
		}

		//从请求中获取额外需要保留的请求参数，作为servie的一部分
		  Map awareServiceProperties=CasPropertyHelper.constructAwaredPropertiesFromRequest(buapxClientProperties.getAwaredServiceProperties(),request);
		  Map extProperties=CasPropertyHelper.constructAwaredPropertiesFromRequest(buapxClientProperties.getExtOptions(),request);

		  String servletPath = request.getServletPath();
		  String pathInfo = request.getPathInfo();
		  if (pathInfo != null && pathInfo.length() > 0) {
	        	servletPath = servletPath + pathInfo;
		  }
		  
		  if(StringUtils.isEmpty(servletPath)){
			 servletPath=request.getRequestURI();
		  }
		String queryUrl=StringUtils.isEmpty(request.getQueryString())?servletPath:servletPath+"?"+request.getQueryString();

		if (this.isUserLogined(request)||isUrlInExceptFilterList(queryUrl) || isSpecialNoNeedFilterList(request)) {
			log.debug("the url is  UserLogined or InExceptFilterList or SpecialNoNeedFilter! url="+queryUrl);
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		} else
		// 假若在安全列表中，则尝试使用统一认证登录
		if (isUrlNeedFilter(queryUrl)) {
			// 未登录
			if (log.isDebugEnabled())
				log.debug("用户没有登录:" + request.getRemoteAddr() + "，跳转到统一认证登录");
			// 重定向到登录页
			log.debug("the url need filter,url="+queryUrl);
			String originalTargetUri =buapxClientProperties.getRedirectAddress()+ servletPath + (CommonUtils.isBlank(request.getQueryString()) ? "" : "?" + request.getQueryString());
			redirectToUrl(response, originalTargetUri,awareServiceProperties,extProperties);
		} else {
			// 三不沾，都不管，直接放掉。
			log.debug("nothing with the url,ignore.... url="+queryUrl);
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

	}

	public void init(FilterConfig config) throws ServletException {
		if (log.isDebugEnabled())
			log.debug("初始化配置属性实体" );
		try {
			this.buapxClientProperties = (BuapxClientProperties) PropertiesManagerContainer.getProperties(BuapxClientProperties.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}



	private BuapxClientProperties buapxClientProperties;

	private boolean isUserLogined(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		SecurityUserInfo assertion = (session != null) ? (SecurityUserInfo) session.getAttribute(ConstString.SESSION_URL) : null;
		return assertion == null ? false : true;
	}

	/**
	 * 重定向到统一认证
	 *  
	 * @param response
	 * @throws IOException
	 */
	private void redirectToUrl(HttpServletResponse response, String originalTargetUri,Map awareServiceProperties,Map extProperties) throws IOException {
		String url = UrlConstructor.constructCasLoginUrlUTF8(buapxClientProperties,extProperties, awareServiceProperties, originalTargetUri);
	 		log.debug("未登录,重定向到统一认证地址...not logined,redirect to url =" + url);
		
		response.sendRedirect(url);
	}


	private boolean isUrlNeedFilter(String url) {
	 String[] filterUrlPattern=buapxClientProperties.getSecurityFilterUrl();
		for (int i = 0; i < filterUrlPattern.length; i++) {
			if (pathMarch.match(filterUrlPattern[i], url.toLowerCase()))
				return true;
		}
		return false;
	}
	//判断是哦福在拦截规则内的url
	private boolean isUrlInExceptFilterList(String url) {
		String[] exceptFilterUrlPattern=buapxClientProperties.getExceptSecurityFilterUrl();
		for (int i = 0; i < exceptFilterUrlPattern.length; i++) {
			if (pathMarch.match(exceptFilterUrlPattern[i], url.toLowerCase()))
				return true;
		}
		return false;
	}


/*	private boolean isUrlInExceptEncodingFilterList(String url) {
		String[] exceptFilterUrlPattern=buapxClientProperties.getEncodingFilterExcludeUrl();
		for (int i = 0; i < exceptFilterUrlPattern.length; i++) {
			if (pathMarch.match(exceptFilterUrlPattern[i], url.toLowerCase()))
				return true;
		}
		return false;
	}
	*/
	//判断是否特殊的不需要拦截的url
	private boolean isSpecialNoNeedFilterList(HttpServletRequest request) {
		String noneed = (String) request.getAttribute("noNeedFilter");
		return "false".equals(noneed);
	}

	public static void main(String args){
		String[] filterUrlPattern={"index","index2"};
		String url="";
		PathMatcher pathMarch = new AntPathMatcher();
		for (int i = 0; i < filterUrlPattern.length; i++) {
			if (pathMarch.match(filterUrlPattern[i], url.toLowerCase()))
				System.out.println(true);
		}
		System.out.println(false);
	}
}
