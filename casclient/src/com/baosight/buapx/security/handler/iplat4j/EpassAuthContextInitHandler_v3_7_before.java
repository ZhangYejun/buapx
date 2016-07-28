package com.baosight.buapx.security.handler.iplat4j;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;

import com.baosight.buapx.security.handler.IAuthPostHandler;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;
/*import com.baosight.epass2.base.AuthenticationFactory;
import com.baosight.epass2.base.IAuthenToken;*/
import com.baosight.iplat4j.security.IpAuthenticationToken;
import com.baosight.iplat4j.security.bridge.SecurityBridge;
import com.baosight.iplat4j.security.bridge.SecurityBridgeFactory;
import com.baosight.iplat4j.security.epass.GrantedEpassAuthority;

public class EpassAuthContextInitHandler_v3_7_before implements IAuthPostHandler {
	private final static String ROLE_EPASS = "ROLE_EPASS";
   public static final String ACEGI_SECURITY_CONTEXT_KEY = "ACEGI_SECURITY_CONTEXT";

	public void handle(HttpServletRequest request,
			HttpServletResponse response, SecurityUserInfo userInfo,
			boolean success) {
		/*if(success){
			Authentication au = null;
			    IAuthenToken token = AuthenticationFactory.getAuthenToken(userInfo.getUserName());
	        GrantedEpassAuthority authority  = new GrantedEpassAuthority(ROLE_EPASS, token);
			IpAuthenticationToken ipToken = new IpAuthenticationToken(userInfo.getUserName(), "",new HashMap());
			ipToken.addAuthorities(new GrantedAuthority[] { authority });
			ipToken.setAuthenticated(true);
			SecurityContextHolder.getContext().setAuthentication(ipToken);
			au=ipToken;
			SecurityContext sc=new SecurityContextImpl();
			sc.setAuthentication(au);
			request.getSession().setAttribute(ACEGI_SECURITY_CONTEXT_KEY, sc);
		}*/
		

	}

}
