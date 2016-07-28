package com.baosight.buapx.threadpool;
import java.util.concurrent.Callable;
/**
 * 将异步任务提交到线程池进行处理，可同时存在多个线程池
 */
import java.util.concurrent.Future;

public interface IThreadPool {
	/**
	 * 提交异步任务到线程池
	 * @param task
	 * @param poolName
	 * @return
	 */
     public  <T> Future<T> submit(Callable<T> task,String poolName);
}
