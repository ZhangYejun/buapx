package com.baosight.buapx.web.support;

/**
 * 简单用户映射转换，适用于无需转换的系统或测试
 * @author Mai
 * BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 * 2011-4-17
 */
public class SimpleUserTranslator implements UserTranslatorInf{

	@Override
	public String getUser(String casUserName, String targetPlatFormName) {
		return casUserName;
	}

}
