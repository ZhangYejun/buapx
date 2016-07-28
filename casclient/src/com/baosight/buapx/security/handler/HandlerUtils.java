package com.baosight.buapx.security.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baosight.buapx.security.common.ClassUtils;
import com.baosight.buapx.security.exception.ClassInstanceException;
import com.baosight.buapx.security.handler.iplat4j.AuthenticationPostHandlerAdapter;
import com.baosight.iplat4j.security.AuthenticationPostHandler;


public class HandlerUtils {
	
	private static Log log = LogFactory.getLog(HandlerUtils.class);
	
	public static List getHandler(String[] className,Class inf) throws ClassInstanceException{
       if(className==null){
    	   return new ArrayList(0);
       }
		List ls = new ArrayList(className.length);
		for(int i = 0; i < className.length; i++){
			String clzName = className[i];
			if(log.isDebugEnabled())
				log.debug((i+1)+"."+clzName);
			
			Object o=null;
			Object result=null;
			if(ClassUtils.isInstanceOf(inf, clzName)){
				 o = ClassUtils.getInstance(clzName.trim(), inf);
				 result=o;
			}else{
				if(ClassUtils.isInstanceOf(AuthenticationPostHandler.class, clzName)){
					 o = ClassUtils.getInstance(clzName.trim(), AuthenticationPostHandler.class);
						result=new AuthenticationPostHandlerAdapter((AuthenticationPostHandler)o);
				}else{
					throw new ClassInstanceException("找不到匹配的Handler类型");
				}
			}
			ls.add(result);
		}

		return ls;
		
	}

}
