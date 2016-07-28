/**
 * 
 */
package com.baosight.buapx.web.support;

import com.baosight.buapx.ua.auth.exception.BuapxTransferException;

/**
 * 转换用户名
 * 
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-4-17
 */
public interface UserTranslatorInf {

	public String getUser(String casUserName, String targetPlatFormName) throws BuapxTransferException;

}
