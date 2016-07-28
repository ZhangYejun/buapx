package com.baosight.buapx.threadpool.test;

import com.baosight.buapx.threadpool.impl.CachedTimeoutThreadPool;

public class TestTimeout {

	public static void main(String[] args) {
		CachedTimeoutThreadPool.getInstance().submit(new MyTask(), "log4Mongo",4000);
	}

	
	

}
