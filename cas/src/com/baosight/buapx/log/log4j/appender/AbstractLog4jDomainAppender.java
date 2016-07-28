package com.baosight.buapx.log.log4j.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.baosight.buapx.log.domain.IDomainLog;


public abstract class AbstractLog4jDomainAppender extends AppenderSkeleton {

	
	@Override
	protected void append(LoggingEvent event) {
		
		Object e=event.getMessage();
		if(e instanceof IDomainLog){
			IDomainLog domain=(IDomainLog) e;
			store(domain);
		}

	}
	
	protected abstract void store(final IDomainLog domain);

}
