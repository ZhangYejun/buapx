package com.baosight.buapx.security.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.baosight.buapx.security.userdetails.SecurityUserInfo;

public abstract class AbstractSpringSecurity3Handler implements IAuthPostHandler {

	public void handle(HttpServletRequest request,
			HttpServletResponse response, SecurityUserInfo userInfo,
			boolean success) {
		if(success){
			SecurityContext context=new  SecurityContextImpl();
			Authentication auth=new UsernamePasswordAuthenticationToken(loadUserByUsername(userInfo.getUserName()), null,loadUserAuthorities(userInfo.getUserName()));
			SecurityContextHolder.getContext().setAuthentication(auth);  
			context.setAuthentication(auth);
	        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);  	
		    updateSession(request, userInfo.getUserName());
		}

	}
	
	
	  public UserDetails loadUserByUsername(String username)  {
		  /*UserDetails user  = loadUserByUsernameIndeed(username);


	        if (user == null) {

	         throw new RuntimeException("cas post handler:user no found");
	        }*/

	        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

	        List<GrantedAuthority> userAuth=loadUserAuthorities(username);
	        if(userAuth!=null&&userAuth.size()!=0){
		          dbAuthsSet.addAll(userAuth);
	        }
            List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
	        if (dbAuths.size() == 0) {
	            throw new RuntimeException("User '" + username + "' has no authorities and will be treated as 'not found'");   
	        }


	        return createUserDetails(username,dbAuths);
	    
	  }


	    /**
	     * Executes the SQL <tt>usersByUsernameQuery</tt> and returns a list of UserDetails objects.
	     * There should normally only be one matching user.
	     */
	    private UserDetails loadUserByUsernameIndeed(String username) {   
	         return new User(username,"12345", true, true, true, true, AuthorityUtils.NO_AUTHORITIES);
	    }

	
	    protected List<GrantedAuthority> loadUserAuthorities(String username) {
	    	List<String> roles=loadRoles(username);
	    	List<GrantedAuthority> auth=new ArrayList<GrantedAuthority>();
	    	       for(String role:roles){
	    	    	auth.add(new SimpleGrantedAuthority(role));
	    	       }
	    	 return auth;
	    }

	    
	    protected UserDetails createUserDetails(String username, 
	            List<GrantedAuthority> combinedAuthorities) {
	        return new User(username, "12345",true,
	                true, true, true, combinedAuthorities);
	    }

	    
	    public abstract List<String> loadRoles(String userName);
	    
	    
	    public abstract void updateSession(HttpServletRequest request,String userName);

}
