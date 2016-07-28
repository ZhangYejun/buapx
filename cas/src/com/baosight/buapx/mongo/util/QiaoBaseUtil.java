package com.baosight.buapx.mongo.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class QiaoBaseUtil {
	protected final static Log logger = LogFactory.getLog(QiaoBaseUtil.class);
	
	
	private static QiaoBaseUtil qiaoBaseUtil = null;
	private QiaoBaseUtil(){
		
	}
	
	public static QiaoBaseUtil getInstance(){
		if(qiaoBaseUtil == null){
			synchronized (QiaoBaseUtil.class) {
				if(qiaoBaseUtil == null){
					qiaoBaseUtil =  new QiaoBaseUtil();
				}
			}
		}
		return qiaoBaseUtil;
	}
	

	
}
