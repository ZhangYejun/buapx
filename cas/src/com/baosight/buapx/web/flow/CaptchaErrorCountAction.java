package com.baosight.buapx.web.flow;

import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 用以记录登录失败的次数
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-7-25
 */
public class CaptchaErrorCountAction extends AbstractAction {

	@Override
	protected Event doExecute(RequestContext context) throws Exception {
		int count;
		try {
			count = context.getFlowScope().getInteger("loginErrorCount");
		} catch (Exception e) {
			count = 0;
		}
		count++;
		context.getFlowScope().put("loginErrorCount", count);
		return success();
	}

}
