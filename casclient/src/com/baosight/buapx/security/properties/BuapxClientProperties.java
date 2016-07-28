/**
 *
 */
package com.baosight.buapx.security.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.PropertyResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.baosight.buapx.security.common.XmlUtils;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-12-28
 *
 *         change by lizidi@baosight,for using xml
 */
public class BuapxClientProperties {

	private static BuapxClientProperties buapxClientProperties;

	public String getPlatAddress() {
		return platAddress;
	}


	public String getRedirectAddress() {
		return redirectAddress;
	}


	public String getAppCallBack() {
		return appCallBack;
	}

	public String getAppDefaultTargetUrl() {
		return appDefaultTargetUrl;
	}

	public String getSendRenew() {
		return sendRenew;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public String[] getValidateUrl() {
		return validateUrl;
	}


	public String getEncoding() {
		return encoding;
	}

	public String getPlatName() {
		return platName;
	}

	public String getAuthErrorPage() {
		return authErrorPage;
	}

	public boolean getRedirectToOriginal() {
		return redirectToOriginal;
	}



	public String getClientType() {
		return clientType;
	}



	public static BuapxClientProperties getBuapxClientProperties() {
		return buapxClientProperties;
	}

	public String[] getSecurityFilterUrl() {
		return securityFilterUrl;
	}

	public String[] getExceptSecurityFilterUrl() {
		return exceptSecurityFilterUrl;
	}

	public String[] getExtOptions() {
		return extOptions;
	}



	public String[] getPostHandlers() {
		return postHandlers;
	}



	public String[] getAwaredServiceProperties() {
		return awaredServiceProperties;
	}

	public String getUserRedirectManager() {
		return userRedirectManager;
	}

	public boolean getIsCluster() {
		return isCluster;
	}

	public String getDomain() {
		return domain;
	}


	public String[] getExtParams() {
		return extParams;
	}




	public String getAppRelogin() {
		return appRelogin;
	}




	public String getWebPath() {
		return webPath;
	}


	public String getValidatePasswordAddress() {
		return validatePasswordAddress;
	}


	public String getEncodingFilter() {
		return encodingFilter;
	}



	public String getLoginPage() {
		return loginPage;
	}


	public boolean isRemoteLogin() {
		return remoteLogin;
	}










	private String platAddress;
	private String webPath;
	private String redirectAddress;
	private String appCallBack;
	private String appDefaultTargetUrl;
	private String authErrorPage;

	private String sendRenew;
	private String loginUrl;
	private String[] validateUrl;
	private String encoding;
	private String platName;
	private boolean redirectToOriginal;
    private String clientType;
    private  String[] securityFilterUrl;
	private  String[] exceptSecurityFilterUrl;
    private String[] extOptions;
    private String[] extParams;
    private String[] postHandlers;
    private String[] awaredServiceProperties;
    private String userRedirectManager;
    private boolean isCluster;
    private String domain;
    private String appRelogin;
    private String validatePasswordAddress;
    private String encodingFilter;
    private String loginPage;
    private boolean remoteLogin;
	private BuapxClientProperties() {
	}

	public synchronized static BuapxClientProperties getProperties(String fileName) throws IOException, DocumentException {
		if (buapxClientProperties != null)
			return buapxClientProperties;
		String str=null;
		 SAXReader saxReader = new SAXReader();
	     Document doc = saxReader.read(BuapxClientProperties.class.getClassLoader().getResourceAsStream(fileName));
	     Element root = doc.getRootElement();//获取根元素
		BuapxClientProperties bp = new BuapxClientProperties();

		bp.platAddress = root.element(BuapxClientKey.platAddress).getTextTrim();
	//	bp.webPath = root.element(BuapxClientKey.webPath).getTextTrim();
		bp.redirectAddress = root.element(BuapxClientKey.redirectAddress).getTextTrim();
		bp.appCallBack = root.element(BuapxClientKey.appCallBack).getTextTrim();
		bp.appDefaultTargetUrl = root.element(BuapxClientKey.appDefaultTargetUrl).getTextTrim();
		bp.authErrorPage = root.element(BuapxClientKey.authErrorPage).getTextTrim();
		bp.encoding = root.element(BuapxClientKey.encoding).getTextTrim();
		bp.loginUrl = root.element(BuapxClientKey.loginUrl).getTextTrim();
		bp.platAddress = root.element(BuapxClientKey.platAddress).getTextTrim();
		bp.platName = root.element(BuapxClientKey.platName).getTextTrim();
		bp.sendRenew = root.element(BuapxClientKey.sendRenew).getTextTrim();
		bp.appRelogin = root.element(BuapxClientKey.appRelogin).getTextTrim();

		bp.validatePasswordAddress = root.element(BuapxClientKey.validatePasswordAddress).getTextTrim();
		if(!StringUtils.isEmpty((str=root.element(BuapxClientKey.validateUrl).getTextTrim()))){
			bp.validateUrl=str.split(" ");
		}
		bp.clientType = root.element(BuapxClientKey.clientType).getTextTrim();
		bp.userRedirectManager = root.element(BuapxClientKey.userRedirectManager).getTextTrim();
		bp.domain = root.element(BuapxClientKey.domain).getTextTrim();
		bp.redirectToOriginal = "true".equalsIgnoreCase(root.element(BuapxClientKey.redirectToOriginal).getTextTrim())?true:false;
		bp.isCluster="true".equalsIgnoreCase(root.element(BuapxClientKey.isCluster).getTextTrim())?true:false;
		if(!StringUtils.isEmpty((str=root.element(BuapxClientKey.securityFilterUrl).getTextTrim()))){
			bp.securityFilterUrl=str.split(" ");
		}

		if(!StringUtils.isEmpty((str=root.element(BuapxClientKey.exceptSecurityFilterUrl).getTextTrim()))){
			bp.exceptSecurityFilterUrl=str.split(" ");
		}

		if(!StringUtils.isEmpty((str=root.element(BuapxClientKey.extOptions).getTextTrim()))){
			bp.extOptions=str.split(" ");
		}
		if(!StringUtils.isEmpty((str=root.element(BuapxClientKey.extParams).getTextTrim()))){
			bp.extParams=str.split(" ");
		}

		if(!StringUtils.isEmpty((str=root.element(BuapxClientKey.postHandlers).getTextTrim()))){
			bp.postHandlers=str.split(" ");
		}

		if(!StringUtils.isEmpty((str=root.element(BuapxClientKey.awaredServiceProperties).getTextTrim()))){
			bp.awaredServiceProperties=str.split(" ");
		}


		bp.encodingFilter = root.element(BuapxClientKey.encodingFilter).getTextTrim();

		bp.loginPage = root.element(BuapxClientKey.loginPage).getTextTrim();

		bp.remoteLogin = "true".equalsIgnoreCase(root.element(BuapxClientKey.remoteLogin).getTextTrim())?true:false;



		if (bp.getAppCallBack().equals(bp.appDefaultTargetUrl))
			throw new RuntimeException("appDefaultTargetUrl can NOT equals appCall");
		buapxClientProperties = bp;
		return buapxClientProperties;

	}

	private abstract class BuapxClientKey {
		/**
		 * 应用地址
		 */
		public static final String platAddress = "platAddress";

		/**
		 * 发布后的包路径，为了防止一些特殊情况取不到包的上下文
		 */
		public static final String webPath="webPath";
		/**
		 * 跳转地址
		 */
		public static final String redirectAddress="redirectAddress";
		/**
		 * 应用回调入口
		 */
		public static final String appCallBack = "appCallback";
		/**
		 * 验证成功跳转地址
		 */
		public static final String appDefaultTargetUrl = "appDefaultTargetUrl";
		/**
		 * 验证失败页面
		 */
		public static final String authErrorPage = "authErrorPage";
		/**
		 * 是否强制统一认证重新登录
		 */
		public static final String sendRenew = "sendRenew";
		/**
		 * 统一认证登录地址
		 */
		public static final String loginUrl = "loginUrl";
		/**
		 * 统一认证票据校验地址
		 */
		public static final String validateUrl = "validateUrl";

		/**
		 * 查询URL编码
		 */
		public static final String encoding = "encoding";
		/**
		 * 应用系统名
		 */
		public static final String platName = "platName";
		/**
		 * 验证成功后是否重定向到原始访问页面
		 */
		public static final String redirectToOriginal = "redirectToOriginal";
	/**
	 * 客户端类型
	 */
		public static final String clientType = "clientType";


		public static final String exceptSecurityFilterUrl="exceptSecurityFilterUrl";

		public static final String securityFilterUrl="securityFilterUrl";

		public static final String extOptions="options";

		public static final String postHandlers="postHandlers";

		public static final String awaredServiceProperties="awaredServiceProperties";

		public static final String userRedirectManager="userRedirectManager";

		public static final String isCluster="isCluster";

		public static final String domain="domain";

		public static final String extParams="extParams";

		public static final String appRelogin="appRelogin";

		public static final String validatePasswordAddress="validatePasswordAddress";

		public static final String encodingFilter="encodingFilter";

		public static final String loginPage="loginPage";

		public static final String remoteLogin="remoteLogin";

	}

	public static void main(String[] args) throws IOException, DocumentException {
		BuapxClientProperties b = BuapxClientProperties.getProperties("buapx_cas-client(7).xml");
	}

}
