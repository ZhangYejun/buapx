package com.baosight.buapx.ua.auth.transfer;

import java.util.Map;

import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;

public interface IAuthTransferChannel {
	
	/**
	 * 新增用户
	 * @param userinfo
	 * @throws BuapxTransferException 
	 */
	public void addUser(AuthUserInfo userinfo) throws BuapxTransferException;
	/**
	 * 删除用户
	 * @param userids
	 */
	public void removeUser(String userid) throws BuapxTransferException;	
	/**
	 * 修改用户
	 * @param userinfo
	 * @param updateFields 需要修改的字段
	 */
	public void updateUser(AuthUserInfo userinfo,AuthUserInfo.Fields... updateFields) throws BuapxTransferException;
	/**
	 * 增加登录别名
	 * @param userid
	 * @param loginAlias
	 */
	public void addLoginAlias(String userid,String... loginAlias) throws BuapxTransferException;
	
	/**
	 * 删除登录别名
	 * @param userid
	 * @param loginAlias
	 */
	public void removeLoginAlias(String userid,String... loginAlias) throws BuapxTransferException;
	
	/**
	 * 新增子账号	
	 * @param mainAccount
	 * @param subAccount
	 * @param sysCode
	 */
	public void addSubAccount(String sysCode,String mainAccount,String subAccount) throws BuapxTransferException;
	/**
	 * 批量移除子账号
	 * @param mainAccounts
	 * @param subAccount
	 * @param sysCode
	 */
	public void removeSubAccount(String sysCode,String... mainAccounts) throws BuapxTransferException;
	
	/**
	 * 批量新增子账号
	 * @param sysCode
	 * @param userid_sourceLoginName
	 */
	public void addSubAccounts(String sysCode,
			Map<String, String> userid_sourceLoginName) throws BuapxTransferException;
	


	

}
