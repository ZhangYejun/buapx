package com.baosight.buapx.ua.auth.api.impl;

/**
 * <p>Hector实现的认证管理
 * <code>HectorBuapxAuthManager.java</code>。
 * </p>
 * @author lizidi
 * @version HectorBuapxAuthManager.java 0.2 2011-7-9 下午12:32:05
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;

import com.baosight.buapx.cassandra.client.interfaces.CassandraSimpleOperator;
import com.baosight.buapx.ua.auth.api.IAuthManager;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;

public class CassandraBuapxAuthManager implements IAuthManager {
	private CassandraSimpleOperator operator = null;
	private String userMappingColumnFamily;
	private String userInfoColumnFamily;
	private static String CAS_SYSCODE="buapxcas2012";
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ThreadLocal<AuthUserInfo> userInfoPool=new ThreadLocal<AuthUserInfo>();
	
	private boolean loginNameCaseInsensitive;//是否区分大小写,默认false，区分大小写

 



	public boolean isLoginNameCaseInsensitive() {
		return loginNameCaseInsensitive;
	}




	public void setLoginNameCaseInsensitive(boolean loginNameCaseInsensitive) {
		this.loginNameCaseInsensitive = loginNameCaseInsensitive;
	}




	public CassandraBuapxAuthManager(CassandraSimpleOperator operator) {
		this.operator = operator;
	}




	public String getUserMappingColumnFamily() {
		return userMappingColumnFamily;
	}




	public void setUserMappingColumnFamily(String userMappingColumnFamily) {
		this.userMappingColumnFamily = userMappingColumnFamily;
	}




	public String getUserInfoColumnFamily() {
		return userInfoColumnFamily;
	}




	public void setUserInfoColumnFamily(String userInfoColumnFamily) {
		this.userInfoColumnFamily = userInfoColumnFamily;
	}






	@Override
	public boolean hasPermissionToSystem(String userid, String systemCode) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0 || systemCode == null
				|| systemCode.trim().length() == 0) {
			return false;
		}
		String result=null;

		try {
			result = operator.getColumn(systemCode, userMappingColumnFamily,
					userid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}
		return (result != null) && result.trim().length() != 0;
	}

	@Override
	public boolean hasPermissionToCas(String userid, String password)
			throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0 || password == null) {
			return false;
		}
		AuthUserInfo userInfo=userInfoPool.get();
		String result = null;
		if(userInfo!=null&&userInfo.getUserid().equals(userid)){
			result=userInfo.getPassword();
		}else{
			try {
				result = operator.getColumn(userid, userInfoColumnFamily,
						AuthUserInfo.Constants.PASSWORD_FIELD_NAME);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BuapxTransferException(e.getMessage(),e);
			}
		}

		return (result != null) && result.equals(password);
	}

	@Override
	public void addPermissionToSystem(String userid, String systemCode,
			String sourceLoginName) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0 || systemCode == null
				|| systemCode.trim().length() == 0 || sourceLoginName == null
				|| sourceLoginName.trim().length() == 0) {
			return;
		}
		try {
			operator.insertColumn( systemCode,userid, sourceLoginName,
					userMappingColumnFamily);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}
	}

	@Override
	public void addUser(AuthUserInfo userInfo)
			throws BuapxTransferException {
		String userid=userInfo.getUserid();
		String password=userInfo.getPassword();
		if (userid == null || userid.trim().length() == 0 || password == null) {
			return;
		}
		try {
			operator.insertColumns(userInfo.getUserid(), userInfo.toMap(), userInfoColumnFamily);
		    addLoginAlias(userid, userid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}
	}

	@Override
	public void removePermissionToSystem(String systemCode, String... userids)
			throws BuapxTransferException {
		if (userids == null || userids.length == 0 || systemCode == null
				|| systemCode.trim().length() == 0) {
			return;
		}
		try {
			operator.delete(userMappingColumnFamily, systemCode,
					userids);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}

	@Override
	public void removeUser(String userid) throws BuapxTransferException {
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
	public String querySubAccount(String userid, String systemCode)
			throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0 || systemCode == null
				|| systemCode.trim().length() == 0) {
			return null;
		}
		try {
			return operator.getColumn(systemCode, userMappingColumnFamily,
					userid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}


	@Override
	public void addMultiPermissionsToSystem(String sysCode,
			Map<String, String> userid_sourceLoginName)
			throws BuapxTransferException {
		try {
			operator.insertColumns(sysCode, userid_sourceLoginName, userMappingColumnFamily);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}
	}


/*	@Override
	public void truncateUserAuthColumnFamily() throws BuapxCassandraException {
		try {
			operator.truncate(getUserAuthColumnFamily());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxCassandraException(e.getMessage());
		}

	}*/



	@Override
	public boolean hasUser(String userid) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0) {
			return false;
		}
		String result = null;
		try {
			result = operator.getColumn(userid, userInfoColumnFamily,
					AuthUserInfo.Constants.USERID_FIELD_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}
		return (result != null) && result.trim().length() != 0;
	}



	@Override
	public AuthUserInfo queryUserInfo(String userid) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0) {
			return null;
		}
		AuthUserInfo user=null;
		AuthUserInfo userInfo=userInfoPool.get();
		if(userInfo!=null&&userInfo.getUserid().equals(userid)){
			user=userInfo;
		}else{
			ColumnSlice<String, String> result = null;
			try {
				result = operator.getColumns(userid, userInfoColumnFamily, AuthUserInfo.getFieldsNames());
			    user=new AuthUserInfo();
			    List<HColumn<String, String>> columns= result.getColumns();
			    if(columns!=null){
			    	 Map<String,String> m=new HashMap<String, String>();
					    for(HColumn<String, String> column:columns){
					    	m.put(column.getName(),column.getValue());
					    }
					   user.fromMap(m);

			    }
			} catch (Exception e) {
				e.printStackTrace();
				throw new BuapxTransferException(e.getMessage(),e);
			}
		}
		if(user.getUserid()!=null){
			 return user;
		  }else{
			  return null;
		  }
	}

	@Override
	public void removeLoginAlias(String userid, String... loginNames) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0) {
			return;
		}
		try {
			operator.delete(userMappingColumnFamily, CAS_SYSCODE,
					loginNames);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}

	@Override
	public void addLoginAlias(String userid, String... loginNames) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0) {
			return;
		}
        try {
        	Map map=new HashMap();
        	for(String ln:loginNames){
        		map.put(ln, userid);
        	}
			operator.insertColumns(CAS_SYSCODE, map, userMappingColumnFamily);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}

	@Override
	public String queryUserByLoginAlias(String loginName) {
		if (StringUtils.isEmpty(loginName))
			return null;
		String  casuserid =operator.getColumn(CAS_SYSCODE, userMappingColumnFamily, loginName);
		if (!StringUtils.isEmpty(casuserid)){
			return casuserid;			
		}
		if (isLoginNameCaseInsensitive()){		//不区分大小写，则转为大写查，然后再转为小写查	 
			casuserid =operator.getColumn(CAS_SYSCODE, userMappingColumnFamily, loginName.toUpperCase());	
			if (!StringUtils.isEmpty(casuserid)){
				return casuserid;			
			}
			casuserid =operator.getColumn(CAS_SYSCODE, userMappingColumnFamily, loginName.toLowerCase());	
			if (!StringUtils.isEmpty(casuserid)){
				return casuserid;			
			}
		}
  
		return casuserid;
	}




	@Override
	public void saveUserToCache(String userid) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0) {
			return ;
		}
		ColumnSlice<String, String> result = null;
		try {
			result = operator.getColumns(userid, userInfoColumnFamily, AuthUserInfo.getFieldsNames());
		    AuthUserInfo user=new AuthUserInfo();
		    List<HColumn<String, String>> columns= result.getColumns();
		    if(columns!=null){
		    	 Map<String,String> m=new HashMap<String, String>();
				    for(HColumn<String, String> column:columns){
				    	m.put(column.getName(),column.getValue());
				    }
				   user.fromMap(m);
				  if(user.getUserid()!=null){
					  userInfoPool.set(user);
				  }
		    }
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}




	@Override
	public void clearUserCache() {
		userInfoPool.remove();
	}




	@Override
	public boolean isUserActived(String userid) throws BuapxTransferException {
		AuthUserInfo user=queryUserInfo(userid);
		return user.isHasActived();
	}




	@Override
	public void activeUser(String userid) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0 ) {
			return;
		}
		try {
			if(queryUserInfo(userid)!=null){
				operator.insertColumn(userid, AuthUserInfo.Constants.HASACTIVED_FIELD_NAME,AuthUserInfo.Constants.HAS_ACTIVED_TRUE,userInfoColumnFamily);
			}else{
				throw new BuapxTransferException("用户不存在");
			}		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}




	@Override
	public void handupUser(String userid) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0 ) {
			return;
		}
		try {
			if(queryUserInfo(userid)!=null){
				operator.insertColumn(userid, AuthUserInfo.Constants.HASACTIVED_FIELD_NAME,AuthUserInfo.Constants.HAS_ACTIVED_FALSE,userInfoColumnFamily);
			}else{
				throw new BuapxTransferException("用户不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}








	@Override
	public boolean isUserPwdExpire(String userid) throws BuapxTransferException {
		if (userid == null || userid.trim().length() == 0) {
			return false;
		}
		AuthUserInfo userInfo=userInfoPool.get();
		Date expiryDate = null;
		if(userInfo!=null&&userInfo.getUserid().equals(userid)){
			expiryDate=userInfo.getExpiryDate();
		}else{
			try {
				String expiryDateStr = operator.getColumn(userid, userInfoColumnFamily,
						AuthUserInfo.Constants.EXPIRYDATE_FIELD_NAME);
				expiryDate=format.parse(expiryDateStr);

			} catch (Exception e) {
				e.printStackTrace();
				throw new BuapxTransferException(e.getMessage(),e);
			}
		}
		if(expiryDate==null){
			return false;
		}else{

			return expiryDate.compareTo(new Date())<0?true:false;
		}
	}




	@Override
	public void updatePwd(String userid, String pwd) throws BuapxTransferException {
		if (StringUtils.isEmpty(userid)||StringUtils.isEmpty(pwd)) {
			return;
		}
		try {
			if(queryUserInfo(userid)!=null){
				operator.insertColumn(userid, AuthUserInfo.Constants.PASSWORD_FIELD_NAME,pwd,userInfoColumnFamily);
			}else{
				throw new BuapxTransferException("用户不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}




	@Override
	public void updatePwdExpiryDate(String userid, Date newDate) throws BuapxTransferException {
		if (StringUtils.isEmpty(userid)||newDate==null) {
			return;
		}
		try {
			if(queryUserInfo(userid)!=null){
				operator.insertColumn(userid, AuthUserInfo.Constants.EXPIRYDATE_FIELD_NAME,format.format(newDate),userInfoColumnFamily);
			}else{
				throw new BuapxTransferException("用户不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}


	}


	@Override
	public void updatePwdEncryptVersion(String userid, String encVersion) throws BuapxTransferException {
		if (StringUtils.isEmpty(userid)||StringUtils.isEmpty(encVersion)) {
			return;
		}
		try {
			if(queryUserInfo(userid)!=null){
				operator.insertColumn(userid, AuthUserInfo.Constants.ENCVERSION_FIELD_NAME,encVersion,userInfoColumnFamily);
			}else{
				throw new BuapxTransferException("用户不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuapxTransferException(e.getMessage(),e);
		}

	}


	private static class StringUtils{
		public static boolean isEmpty(String str){
			return str==null||str.trim().equals("");
		}
	}


 




}
