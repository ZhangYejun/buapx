package com.baosight.buapx.web.flow.remotelogin;

import javax.servlet.http.HttpServletRequest;

import ocx.AESWithJCE;

import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.baosight.buapx.cas.authentication.principal.BuapxUsernamePasswordCredentials;


public final class RemoteCredentialResolveAction extends AbstractAction {


    protected Event doExecute(final RequestContext context) throws Exception {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        BuapxUsernamePasswordCredentials credential=new BuapxUsernamePasswordCredentials();
        String useControls = request.getParameter("useControls");
        String password= null;
        if("1".equals(useControls)){
        	String encodedpwd =request.getParameter("password"); //获取base64 密码密文数据
        	String mcrypt_key_1=  (String)request.getSession().getAttribute("mcrypt_key");//获取session中随机因子
         	password=AESWithJCE.getResult(mcrypt_key_1,encodedpwd);//调用解密接口。mcrypt_key_1为获取的32位随机数，password1为密码的密文
         	request.getSession().removeAttribute("mcrypt_key");//解密后清除session中随机数，防止重放攻击
        }else {
        	password =request.getParameter("password");
        }
        	
          
         
        credential.setPassword(password);
        credential.setUsername(request.getParameter("username"));
        credential.setUserType(request.getParameter("usertype"));

        context.getFlowScope().put("credentials", credential);

        return null;
    }


}
