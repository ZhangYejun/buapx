/**
 * 
 */
package com.baosight.buapx.cas.authentication.principal;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.Response;
import org.jasig.cas.authentication.principal.Service;

/**
 * 
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-5-24
 */
public class BuapxWebServiceTicketServer implements Service {

	private static final String CONST_PARAM_SERVICE = "service";

	public BuapxWebServiceTicketServer(String id) {
		this.id = id;
	}

	private static final long serialVersionUID = 1L;

	private String id;
	private boolean loggedOut = false;

	public String getArtifactId() {
		return null;
	}

	public Response getResponse(String ticketId) {
		return null;
	}

	public boolean logOutOfService(final String sessionIdentifier) {
		this.loggedOut = true;
		return false;
	}

	public boolean isLoggedOut() {
		return this.loggedOut;
	}

	public void setPrincipal(Principal principal) {
		// nothing to do
	}

	public Map<String, Object> getAttributes() {
		return null;
	}

	public String getId() {
		return id;
	}

	public boolean matches(final Service service) {
		return true;
	}

	public static BuapxWebServiceTicketServer createServiceFrom(final HttpServletRequest request, final String serviceType) {
		String service = serviceType;
		if(serviceType == null)
			service = request.getParameter(CONST_PARAM_SERVICE);
		
		if (StringUtils.isBlank(service))
			service = SERVICE_TYPE_HTTP;
		return new BuapxWebServiceTicketServer(service);

	}
	
	public static BuapxWebServiceTicketServer createServiceFrom(final HttpServletRequest request){
		return createServiceFrom(request,null);
	}

	public static final String SERVICE_TYPE_HTTP = "http://www.baosight.com";

	public static final String SERVICE_TYPE_HTTPS = "https://www.baosight.com";
}
