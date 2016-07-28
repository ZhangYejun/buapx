package com.baosight.buapx.threadpool;
/**
 * 将异步任务提交到线程池进行处理，可同时存在多个线程池
 */

import com.baosight.buapx.threadpool.task.ITimeoutTask;

public interface ITimeoutThreadPool {
	/**
	 * 提交任务到线程池，可设置此任务的超时时间
	 * @param task
	 * @param poolName
	 */
     public  <T>  void submit ( ITimeoutTask<T> task,String poolName,long timeout);
}
