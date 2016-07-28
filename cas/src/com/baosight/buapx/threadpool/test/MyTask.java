package com.baosight.buapx.threadpool.test;

import java.util.concurrent.Callable;

import com.baosight.buapx.threadpool.task.ITimeoutTask;

public class MyTask implements ITimeoutTask<Boolean> {  
	  
    @Override  
    public Boolean call() throws Exception {  
        // 总计耗时约10秒  
        for (int i = 0; i < 100; i++) {  
            Thread.sleep(100); // 睡眠0.1秒  
            System.out.print(i+"_");  
        }  
       return Boolean.TRUE;  
    }

	@Override
	public void releaseResource() {
		System.out.println("释放资源");
		
	}  
} 