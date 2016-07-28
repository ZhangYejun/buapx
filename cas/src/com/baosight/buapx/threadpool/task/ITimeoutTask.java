package com.baosight.buapx.threadpool.task;

import java.util.concurrent.Callable;

public interface ITimeoutTask<T> extends Callable<T>{
	/**
	 * 释放资源。当线程超时后，守护线程会调用该任务的releasResource()方法以释放资源。 
	 * 若不进行此处理，则当进行外部IO操作时，导致中断操作无效.
	 */
	public  void releaseResource();
}
