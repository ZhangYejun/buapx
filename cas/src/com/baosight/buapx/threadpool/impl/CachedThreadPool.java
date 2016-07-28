package com.baosight.buapx.threadpool.impl;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;





import com.baosight.buapx.log.common.PropertiesUtils;
import com.baosight.buapx.mongo.util.PropertiesUtil;
import com.baosight.buapx.threadpool.IThreadPool;

public class CachedThreadPool implements IThreadPool {
  private Map<String,ExecutorService> threadPoolMap=new ConcurrentHashMap<String,ExecutorService>();;
  private static Object lock=new Object();
  private  String fileName="threadpool.properties";
  private static IThreadPool pool;
    
  private CachedThreadPool(){
  }
  
  
  
  
public void setPool(IThreadPool pool) {
	this.pool = pool;
}




public String getFileName() {
	return fileName;
}




public void setFileName(String fileName) {
	this.fileName = fileName;
}




@Override
public <T> Future<T> submit(Callable<T> task, String poolName) {
	ExecutorService service= this.threadPoolMap.get(poolName);
	if(service==null){
		synchronized(lock){
			Map<String,Object> properties=PropertiesUtil.getInstance().read(fileName);
			service= this.threadPoolMap.get(poolName);
			if(service==null){
				service= Executors.newFixedThreadPool(Integer.parseInt((String)properties.get(poolName)));
				this.threadPoolMap.put(poolName, service);
			}
		}
	}
	return service.submit(task);	
}

public static IThreadPool getInstance() {
	if(pool==null){
		synchronized(lock){
			if(pool==null){
				pool=new CachedThreadPool();
			}
		}
	}
	return pool;

}

public static IThreadPool getInstance(String fileName) {
	if(pool==null){
		synchronized(lock){
			if(pool==null){
				CachedThreadPool p=new CachedThreadPool();
				p.setFileName(fileName);
				pool=p;
			}
		}
	}
	return pool;

 }
  
}
