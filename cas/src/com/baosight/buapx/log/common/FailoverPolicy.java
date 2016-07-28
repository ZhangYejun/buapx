package com.baosight.buapx.log.common;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class FailoverPolicy {
  private Object lock=new Object();
  private  int maxFailCount = 5;

	
  private  AtomicInteger failCount=new AtomicInteger(0);
  private long timeInterval = 30000;
  private AtomicLong lockedTime=new AtomicLong(System.currentTimeMillis()-1000*3600*24*365);

  
  
  
  public  synchronized void fail(){
		int count=failCount.incrementAndGet();
		if(count>=maxFailCount){
			lockedTime.set(System.currentTimeMillis());
		}
		
		
  }
  
  public  boolean isEnabled(){
	  return failCount.get()<=maxFailCount||(System.currentTimeMillis()-lockedTime.get())>this.timeInterval ;
  }
    
  
  public  void success(){
	  int count=failCount.get();
	  if(count>0){
		  failCount.compareAndSet(count, 0); 
	  }
  }
  

  
  
  public FailoverPolicy(long timeInterval,int maxFailCount){
	  this.timeInterval=timeInterval;
	  this.maxFailCount=maxFailCount;
  }
}
