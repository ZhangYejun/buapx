package com.baosight.buapx.security.handler.iplat4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

import com.baosight.buapx.security.handler.IAuthPostHandler;
import com.baosight.buapx.security.userdetails.SecurityUserInfo;
import com.baosight.iplat4j.security.AuthenticationPostHandler;

public class AuthenticationPostHandlerAdapter  implements IAuthPostHandler{
    private AuthenticationPostHandler handler;
    public static final String ACEGI_SECURITY_CONTEXT_KEY = "ACEGI_SECURITY_CONTEXT";

	public AuthenticationPostHandlerAdapter(AuthenticationPostHandler postHandler) {
		this.handler=postHandler;
	}
	
	public void handle(HttpServletRequest request,
			HttpServletResponse response, SecurityUserInfo userInfo,
			boolean success) {
		SecurityContext sc=(SecurityContext)request.getSession().getAttribute(ACEGI_SECURITY_CONTEXT_KEY);		
		Authentication au=null;
		if(sc!=null){
			au=sc.getAuthentication();
		}
		handler.handle(request, response,au, success);
		
	}

}
