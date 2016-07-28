package com.baosight.buapx.threadpool.impl;
/**
 * 支持线程超时的线程池。通过守护线程的方式实现。
 * lizidi@baosight.com
 */
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



import com.baosight.buapx.log.common.PropertiesUtils;
import com.baosight.buapx.mongo.util.PropertiesUtil;
import com.baosight.buapx.threadpool.IThreadPool;
import com.baosight.buapx.threadpool.task.impl.TimeoutGuardThread;
import com.baosight.buapx.threadpool.ITimeoutThreadPool;
import com.baosight.buapx.threadpool.task.ITimeoutTask;

public class CachedTimeoutThreadPool implements ITimeoutThreadPool {
	 private Map<String,ExecutorService> threadPoolMap=new ConcurrentHashMap<String,ExecutorService>();;
	  private static Object lock=new Object();
	  private String fileName="threadpool";
	  private static String GUARD_THREAD_SUFFIX="GUARD_TREHAD_BUAPX";
 
	  private static ITimeoutThreadPool pool;
	  
	    

	  private CachedTimeoutThreadPool(){
		  
	  }
	  
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	@Override
	public <T> void submit(ITimeoutTask<T> task, String poolName,long timeout) {
		ExecutorService service= this.threadPoolMap.get(poolName);
		ExecutorService guardService= this.threadPoolMap.get(poolName+GUARD_THREAD_SUFFIX);
		Map<String,Object> properties=PropertiesUtil.getInstance().read(fileName);

		if(service==null){
			synchronized(lock){
				service= this.threadPoolMap.get(poolName);
				if(service==null){
					service= Executors.newFixedThreadPool(Integer.parseInt((String)properties.get(poolName)));
					guardService= Executors.newFixedThreadPool(Integer.parseInt((String)properties.get(poolName)));
					this.threadPoolMap.put(poolName, service);
					this.threadPoolMap.put(poolName+GUARD_THREAD_SUFFIX,guardService);
				}
			}
		}
		
		Future<T> result=service.submit(task);			
	    guardService.submit(new TimeoutGuardThread<T>(timeout,result,task));
	}
	
	public static ITimeoutThreadPool getInstance() {
		if(pool==null){
			synchronized(lock){
				if(pool==null){
					pool=new CachedTimeoutThreadPool();
				}
			}
		}
		return pool;

	}

	
	public static ITimeoutThreadPool getInstance(String fileName) {
		if(pool==null){
			synchronized(lock){
				if(pool==null){
					CachedTimeoutThreadPool p=new CachedTimeoutThreadPool();
					p.setFileName(fileName);
					pool=p;
				}
			}
		}
		return pool;

	}
}
