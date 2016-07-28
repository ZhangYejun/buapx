package com.baosight.buapx.web.support;

import org.jasig.cas.ticket.TicketException;
import org.springframework.webflow.execution.RequestContext;

public class ErrorCodeGenerator {
    public static  void populateErrorCodeInFlow(String code, final RequestContext requestContext) {
        requestContext.getFlowScope().put("errorCode",code);
}

public static  String retrieveErrorCodeInFlow(final RequestContext requestContext) {
   return  (String) requestContext.getFlowScope().get("errorCode");
}


}
