package com.baosight.buapx.threadpool.task.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.baosight.buapx.threadpool.task.ITimeoutTask;

public  class TimeoutGuardThread<T> implements Callable<T> {
	private long timeout;
	private Future<T> future;
	private ITimeoutTask<T> task;

	public TimeoutGuardThread(long timeout, Future<T> future,ITimeoutTask<T> task) {
		this.timeout = timeout;
		this.future = future;
		this.task=task;
	}

	
	
	@Override
	public T call() throws Exception {

		try {
			return this.future.get(this.timeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			try{
				task.releaseResource();
			}catch(Exception ee){
				ee.printStackTrace();
			}
			try{
				future.cancel(true);
			}catch(Exception ee){
				ee.printStackTrace();
			}
			return null;
		}

	}
	
	

}
