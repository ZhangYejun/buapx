package com.baosight.buapx.log.log4j.appender;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import com.baosight.buapx.log.common.BeanUtil;
import com.baosight.buapx.log.common.FailoverPolicy;
import com.baosight.buapx.log.common.PropertiesUtils;
import com.baosight.buapx.log.domain.IDomainLog;
import com.baosight.buapx.log.example.UserInfo;
import com.baosight.buapx.mongo.dao.MongoDao;
import com.baosight.buapx.mongo.dao.impl.MongoDaoImpl;


import com.baosight.buapx.threadpool.impl.CachedThreadPool;
import com.baosight.buapx.threadpool.impl.CachedTimeoutThreadPool;
import com.baosight.buapx.threadpool.task.ITimeoutTask;
import com.baosight.buapx.log.common.BeanUtil;

public class MongoLog4jDomainAppender extends AbstractLog4jDomainAppender {
	private MongoDao mongoDao = MongoDaoImpl.getMongoDao();
	private boolean useFailoverPolicy=true;
	private long failoverInterval=-1;
	private int maxFailCount=-1;
	private long threadTimeout=-1;
	private FailoverPolicy failoverPolicy;
	public static final String THREADPOOL_NAME="log4Mongo";
	
	private String threadPoolFilePath="threadpool";
	
	
	
	public String getThreadPoolFilePath() {
		return threadPoolFilePath;
	}


	public void setThreadPoolFilePath(String threadPoolFilePath) {
		this.threadPoolFilePath = threadPoolFilePath;
	}


	public boolean isUseFailoverPolicy() {
		return useFailoverPolicy;
	}


	public void setUseFailoverPolicy(boolean useFailoverPolicy) {
		this.useFailoverPolicy = useFailoverPolicy;
	}


	public long getFailoverInterval() {
		return failoverInterval;
	}


	public void setFailoverInterval(long failoverInterval) {
		this.failoverInterval = failoverInterval;
	}


	public int getMaxFailCount() {
		return maxFailCount;
	}


	public void setMaxFailCount(int maxFailCount) {
		this.maxFailCount = maxFailCount;
	}


	public long getThreadTimeout() {
		return threadTimeout;
	}


	public void setThreadTimeout(long threadTimeout) {
		this.threadTimeout = threadTimeout;
	}


	@Override
	protected void store(final IDomainLog domain) {		
		
		if(failoverPolicy==null){
			if(failoverInterval<=0&&maxFailCount<=0){
				failoverPolicy=new FailoverPolicy(3000, 5);
			}else{
				failoverPolicy=new FailoverPolicy(failoverInterval, maxFailCount);
			}
		}
		
		
		CachedThreadPool.getInstance(threadPoolFilePath).submit(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				if(useFailoverPolicy&&!failoverPolicy.isEnabled()){
					return false;
				}
				boolean fail=false;
				try{
					mongoDao.insert(domain.getClass().getSimpleName(),BeanUtil.beanToMap(domain));				
				}catch(Exception e){
					e.printStackTrace();
					fail=true;
					if(useFailoverPolicy){
						failoverPolicy.fail();
					}
				}
				if(!fail){
					if(useFailoverPolicy){
					failoverPolicy.success();
					}
					return true;
				}else{
					return false;
				}
			}

			
		}
		,THREADPOOL_NAME);
	}
	

	@Override
	public void close() {	
		
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
	

}
