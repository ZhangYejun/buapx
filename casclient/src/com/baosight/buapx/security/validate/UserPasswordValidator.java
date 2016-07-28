package com.baosight.buapx.security.validate;

import java.io.IOException;

import org.dom4j.DocumentException;

import com.baosight.buapx.security.common.EncrypUtil;
import com.baosight.buapx.security.common.SocketUtil;
import com.baosight.buapx.security.properties.BuapxClientProperties;
import com.baosight.buapx.security.properties.PropertiesManagerContainer;

public class UserPasswordValidator {
	private BuapxClientProperties buapxClientProperties;
    private static UserPasswordValidator validator;
	private UserPasswordValidator(){
	}

	public static UserPasswordValidator  getInstance(){
		if(validator==null){
			synchronized(UserPasswordValidator.class){
				if(validator==null){
					validator=new UserPasswordValidator();
					try {
						BuapxClientProperties properties = (BuapxClientProperties) PropertiesManagerContainer
								.getProperties(BuapxClientProperties.class);
						validator.setBuapxClientProperties(properties);
					} catch (Exception e){
						throw new RuntimeException(e);
					}

				}
			}
		}
		return validator;

	}

	public BuapxClientProperties getBuapxClientProperties() {
		return buapxClientProperties;
	}

	public void setBuapxClientProperties(BuapxClientProperties buapxClientProperties) {
		this.buapxClientProperties = buapxClientProperties;
	}


	public boolean auth(String username,String password) throws Exception{
			return authNow(username, password);
	}



    private boolean authNow(String username,String password) throws Exception{
    	String baseUrl = this.buapxClientProperties.getValidatePasswordAddress();
		String queryUrl = baseUrl
		                  + "?username=" + username
		                  + "&password="+ password
		                  + "&syscode=" + this.buapxClientProperties.getPlatName();

		String response = SocketUtil.getResponseFromServerUTF8(queryUrl).trim();

		if (response.indexOf("SUCCESS") != -1) {
			String[] strs = response.split(":")[1].split(","); // 分离出认证结果和对应的用户账号

			String result = strs[0];
			if (result.equals("true")) { // 用户名和密码正确
				return true;
			} else {
				if(result.equals("notActived")){
				  return true;
				}
				if(result.equals("false")){
					return false;
				}
				throw new Exception("未知认证结果");
			}
		} else {
			throw new Exception("统一认证内部错误");
		}

    }



}
