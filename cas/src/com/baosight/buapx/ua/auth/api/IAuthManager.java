package com.baosight.buapx.ua.auth.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
import com.baosight.buapx.ua.auth.exception.BuapxTransferException;



/**
 * <p>Buapx认证管理接口
 * <codeBuapxAuthManager.java</code>。
 * </p>
 * @author lizidi
 * @version BuapxAuthManager.java 0.1 2011-2-7 下午12:32:05
 */
public interface IAuthManager {
   /**
    * 查看用户是否有权限访问某系统
    * @param userid
    * @param systemCode
    * @return
 * @throws BuapxTransferException
 * @
    */
	 boolean hasPermissionToSystem(String userid,String systemCode) throws BuapxTransferException  ;
	/**
	 * 查询用户对于某系统的映射账号
	 * @param userid
	 * @param systemCode
	 * @return 若无映射则返回空，否则返回映射账号
	 * @throws BuapxTransferException
	 * @
	 */
	String querySubAccount(String userid,String systemCode) throws BuapxTransferException  ;
/**
 * 查看用户的CAS用户名和密码是否正确
 * @param userid
 * @param password
 * @return
 * @throws BuapxTransferException
 *
 * @
 */
	boolean hasPermissionToCas(String userid, String password) throws BuapxTransferException  ;


/**
 * 将用户访问某系统的权限信息加入NoSql服务器
 * @param userid
 * @param systemCode
 * @param sourceLoginName
 * @throws BuapxTransferException
 * @
 */
	void addPermissionToSystem(String userid,String systemCode,String sourceLoginName) throws BuapxTransferException  ;

	/**
	 * 批量加入用户账号映射信息
	 * @param sysCode
	 * @param userid_sourceLoginName
	 * @throws BuapxTransferException
	 * @
	 */
	void addMultiPermissionsToSystem(String sysCode,Map<String,String> userid_sourceLoginName) throws BuapxTransferException  ;

	/**
	 * 修改密码
	 * @param userid
	 * @param pwd
	 * @throws BuapxTransferException
	 */
	void updatePwd(String userid,String pwd) throws BuapxTransferException;

/**
 * 更新密码加密算法版本
 * @param userid
 * @param encVersion
 */
	void updatePwdEncryptVersion(String userid,String encVersion)  throws BuapxTransferException ;
	/**
 * 将用户的CAS用户名和密码信息放入NoSql服务器
 * @param userInfo
 * @throws BuapxTransferException
 * @
 */
	void addUser(AuthUserInfo userInfo) throws BuapxTransferException  ;
	/**
	 * 是否有该用户
	 * @param userid
	 * @throws BuapxTransferException
	 * @return
	 * @
	 */
	boolean hasUser(String userid) throws BuapxTransferException  ;

	/**
	 * 从nosql中删除映射信息
	 * @param userid
	 * @param systemCode
	 * @throws BuapxTransferException
	 * @
	 */
	void removePermissionToSystem(String systemCode,String... userids) throws BuapxTransferException  ;
	/**
	 * 从nosql中移除用户
	 * @param userid
	 * @throws BuapxTransferException
	 * @
	 */
	void removeUser(String userid) throws BuapxTransferException  ;

	/**
	 * 宝钢项目临时添加方法开始
	 */
	/**
	 * 删除登录别名
	 */
	void removeLoginAlias(String userid,String... loginNames) throws BuapxTransferException;

	/**
	 * 添加登录别名
	 * @param userid
	 * @param loginNames
	 * @throws BuapxTransferException
	 */
	void addLoginAlias(String userid,String... loginNames) throws BuapxTransferException;
	/**
	 * 根据登录别名，查出对应的CAS用户名
	 * @param loginName
	 * @return
	 */
	String queryUserByLoginAlias(String loginName);

	/**
	 * 用户是否激活
	 * @param userid
	 * @return
	 * @throws BuapxTransferException
	 */
	boolean isUserActived(String userid) throws BuapxTransferException;

	/**
	 *
	 * @param userid
	 * @throws BuapxTransferException
	 */
	void activeUser(String userid) throws BuapxTransferException;

	/**
	 * 挂起用户
	 * @param userid
	 * @throws BuapxTransferException
	 */
	void handupUser(String userid) throws BuapxTransferException;


	/**
	 * 用户密码是否过期
	 * @param userid
	 * @return
	 * @throws BuapxTransferException
	 */
	boolean isUserPwdExpire(String userid) throws BuapxTransferException;


	/**
	 * 宝钢项目临时添加方法结束
	 */

	/**
	 * 查询用户信息
	 * @throws BuapxTransferException
	 */
	AuthUserInfo queryUserInfo(String userid) throws BuapxTransferException;

	/**
	 * 更新用户密码过期时间
	 * @param userid
	 * @param newDate
	 * @throws BuapxTransferException
	 */
	void updatePwdExpiryDate(String userid,Date newDate) throws BuapxTransferException;

	/**
	 * 载入用户信息至当前线程变量中，若当前线程变量中有值，则关于用户信息的查询不再去访问数据源
	 * @throws BuapxTransferException
	 */

	void saveUserToCache(String userid) throws BuapxTransferException;

	/**
	 * 清除线程变量中的用户信息
	 */
	void clearUserCache();

/*	*//**
	 * 移除用户认证表所有信息
	 * @throws BuapxCassandraException
	 *//*
    void truncateUserAuthColumnFamily() throws BuapxCassandraException;*/


}
