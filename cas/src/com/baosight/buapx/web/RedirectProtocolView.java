/**
 * 
 */
package com.baosight.buapx.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-5-23
 */
public  class RedirectProtocolView extends RedirectView {

	public RedirectProtocolView(String string) {
		super(string);
	}

	@Override
	protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetUrl, boolean http10Compatible) throws IOException {
		response.sendRedirect(response.encodeRedirectURL(targetUrl));
	}
	
}
