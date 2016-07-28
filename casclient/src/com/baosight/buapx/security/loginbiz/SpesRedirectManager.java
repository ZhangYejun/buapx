package com.baosight.buapx.security.loginbiz;

/*import com.baosight.buapx.security.common.StringUtils;
import com.baosight.spes.pub2.utility.UserManager;
import com.baosight.spes.system.LoginForward;
import com.baosight.spes.util.EncryptUtil;*/
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


public class SpesRedirectManager implements UserRedirectMananger {
	private static final long serialVersionUID = 1L;


public String constructRedirectUrl(HttpServletRequest request,String userid,String portalHomePage){
	   /* String url = "";
		String pwd = request.getParameter("j_password");		
		String gotoUrl = request.getParameter("j_type");
		try {
			String bmiSys = request.getParameter("sysid");
			if ((bmiSys != null) && ("BMI".equals(bmiSys))) {
				pwd = EncryptUtil.decrypt(pwd);
			}

		} catch (Exception ex) {
		}

		try {
			//AuthHandler.login(userid, pwd, request);

			if ((gotoUrl != null) && (!"false".equals(gotoUrl))) {
				return request.getContextPath() + gotoUrl;
			}

			try {
				request.getSession().setAttribute("intlPassword",
						EncryptUtil.encrypt(pwd));
				String intlRedirect = request.getParameter("j_intlRedirect");
				String intlReturnRedirect = request
						.getParameter("j_intlRedirectUrl");
				if ((intlRedirect != null)
						&& (intlRedirect.equals("http://www.baointl.info"))) {
					return intlReturnRedirect;
				}
			} catch (Exception es) {
			}
		} catch (Exception e) {
			try {
				request.getSession().setAttribute("intlPassword",
						EncryptUtil.encrypt(pwd));
				String intlRedirect = request.getParameter("j_intlRedirect");
				String intlRetrunRedirect = request
						.getParameter("j_intlRetrunRedirect");
				if ((intlRedirect != null)
						&& (intlRedirect.equals("http://www.baointl.info"))) {
					return intlRetrunRedirect;
				}
			} catch (Exception es) {
			}
			try {
				String retrunRedirect = request.getParameter("retrunRedirect");
				if ((retrunRedirect != null) && (!"".equals(retrunRedirect))) {
					return retrunRedirect;
				}
			} catch (Exception e1) {
			}
			return null; //返回null时，response应该置为403
		}

		System.out.println("-----------------------------");

		String redirectto = request.getParameter("redirectto");
		try {
			if ((redirectto != null) && (!redirectto.equals(""))) {
				url = redirectto;
				String key = "userMap_" + userid + "_Defatul" + "_FirstPage";
				Map userMap = UserManager.getSessionMap(request);
				if (userMap == null) {
					userMap = new HashMap();
					request.getSession().setAttribute("pub2", userMap);
				}
				userMap.put(key, redirectto);
			} else {
				url = LoginForward.goToWeb(userid, portalHomePage,
						request);
			}
			request.setAttribute("redirectto", url);
             System.out.println("com.baosight.buapx.spes.LoginBiz的redirectto的值为: "+url);
			if(!url.startsWith("http://")&!url.startsWith("https://")){
				if(url.indexOf("/")>0){
					url="/"+url;
			      }
			}
		
			return url;
			
		} catch (Exception e) {
			return null;//返回null时，response应该置为403
		}*/
	
	return null;
	}

	

	
}

