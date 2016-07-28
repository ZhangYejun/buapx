package com.baosight.iplat4j.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acegisecurity.Authentication;

public abstract interface AuthenticationPostHandler
{
  public abstract String getDescription();

  public abstract void handle(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Authentication paramAuthentication, boolean paramBoolean);
}