package com.baosight.buapx.ua.auth.transfer.impl;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.baosight.buapx.cassandra.client.interfaces.CassandraSimpleOperator;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo.Fields;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;
import com.baosight.buapx.ua.auth.transfer.IAuthTransferChannel;

public class CassandraTransferChannel implements IAuthTransferChannel {
	private CassandraSimpleOperator operator = null;
	private String userInfoColumnFamily;
	private String userMappingColumnFamily;
	
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	

	public CassandraSimpleOperator getOperator() {
		return operator;
	}

	public void setOperator(CassandraSimpleOperator operator) {
		this.operator = operator;
	}

	public String getUserInfoColumnFamily() {
		return userInfoColumnFamily;
	}

	public void setUserInfoColumnFamily(String userInfoColumnFamily) {
		this.userInfoColumnFamily = userInfoColumnFamily;
	}

	public String getUserMappingColumnFamily() {
		return userMappingColumnFamily;
	}

	public void setUserMappingColumnFamily(String userMappingColumnFamily) {
		this.userMappingColumnFamily = userMappingColumnFamily;
	}


	@Override
	public void addUser(AuthUserInfo userInfo) throws BuapxTransferException {
		String userid=userInfo.getUserid();
		String password=userInfo.getPassword();
		if (userid == null || userid.trim().length() == 0 || password == null) {
			return;
		}
		try {
			operator.insertColumns(userInfo.getUserid(), userInfo.toMap(),userInfoColumnFamily);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}

	@Override
	public void removeUser(String  userid) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0) {
			return;
		}

		try {
			operator.delete(userInfoColumnFamily, userid, AuthUserInfo.getFieldsNames());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}

	@Override
	public void updateUser(AuthUserInfo userinfo, Fields... updateFields) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addLoginAlias(String userid, String... loginAlias) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLoginAlias(String userid, String... loginAlias) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSubAccount(String sysCode, String mainAccount,
			String subAccount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubAccount(String sysCode, String... mainAccounts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSubAccounts(String sysCode,
			Map<String, String> userid_sourceLoginName) {
		// TODO Auto-generated method stub
		
	}

}
