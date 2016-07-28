package com.baosight.buapx.security.filter;

import java.io.IOException;
import java.util.List;

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

import com.baosight.buapx.security.common.ConstString;
import com.baosight.buapx.security.common.EncrypUtil;
import com.baosight.buapx.security.common.SocketUtil;
import com.baosight.buapx.security.exception.ClassInstanceException;
import com.baosight.buapx.security.handler.HandlerUtils;
import com.baosight.buapx.security.handler.IAuthPostHandler;
import com.baosight.buapx.security.properties.BuapxClientProperties;
import com.baosight.buapx.security.properties.PropertiesManagerContainer;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;

/**
 * @author lizidi@baosight.com
 */
public class BuapxReloginFilter implements Filter {

	private Log log = LogFactory.getLog(BuapxReloginFilter.class);


	public void destroy() {

	}

	/**
	 * 过滤
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setCharacterEncoding("UTF-8");

		String username=request.getParameter("buapx_username");
		String password=request.getParameter("buapx_password");

		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
			response.getWriter().println("FAIL:用户名或密码为空");
			return;
		}
		String result="SUCCESS:认证成功";
		try {
			String souceLoginName=auth(username, password);
			SecurityUserInfo userInfo=new SecurityUserInfo(souceLoginName,username,null);
			request.getSession().setAttribute(ConstString.SESSION_URL, userInfo);
			onSuccessAuth(request, response, userInfo);
			request.setAttribute("reloginResult", result);
			//doFilter(servletRequest, servletResponse, filterChain);
		} catch (Exception e) {
			e.printStackTrace();
			result="FAIL:"+e.getMessage();

		}
		response.getWriter().println(result);


	}

	private void onSuccessAuth(HttpServletRequest request, HttpServletResponse response,SecurityUserInfo userInfo) throws IOException{

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

	}

	public void init(FilterConfig config) throws ServletException {
		if (log.isDebugEnabled())
			log.debug("初始化配置属性实体" );
		try {
			this.buapxClientProperties = (BuapxClientProperties) PropertiesManagerContainer.getProperties(BuapxClientProperties.class);
			initHandlers(buapxClientProperties.getPostHandlers());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}





    private String auth(String username,String password) throws Exception{
    	String baseUrl = this.buapxClientProperties.getValidatePasswordAddress();
		String queryUrl = baseUrl
		                  + "?username=" + username
		                  + "&password="+ EncrypUtil.codedPassword(password)
		                  + "&syscode=" + this.buapxClientProperties.getPlatName();

		String response = SocketUtil.getResponseFromServerUTF8(queryUrl).trim();

		if (response.indexOf("SUCCESS") != -1) {
			String[] strs = response.split(":")[1].split(","); // 分离出认证结果和对应的用户账号

			String result = strs[0];
			String sourceUsername = strs[1];
			if (result.equals("true")&&!sourceUsername.equals("null")) { // 用户名和密码正确
				return sourceUsername;
			} else {
				if(result.equals("notActived")){
				  throw new Exception("用户未激活");
				}
				if(result.equals("false")){
					throw new Exception("用户或密码错误");
				}
				if(sourceUsername.equals("null")){
					throw new Exception("此账号没有该系统使用权限");
				}
				throw new Exception("未知认证结果");
			}
		} else {
			throw new Exception("重登陆内部错误");
		}

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
			this.postHandlers = HandlerUtils.getHandler(strHandlers, IAuthPostHandler.class);

		} catch (ClassInstanceException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage());
			throw new RuntimeException(e);
		}


		if (log.isDebugEnabled()) {
			log.debug("post handlers:" + this.postHandlers.size());
		}

	}

    private List<IAuthPostHandler> postHandlers;

	private BuapxClientProperties buapxClientProperties;






}
