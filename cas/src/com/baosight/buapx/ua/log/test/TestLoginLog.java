package com.baosight.buapx.ua.log.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baosight.buapx.log.dao.MongoDomainLogDao;
import com.baosight.buapx.mongo.util.MongoDBQueryMap;
import com.baosight.buapx.mongo.util.OpType;
import com.baosight.buapx.ua.log.domain.LoginLog;

public class TestLoginLog {
	private static SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		MongoDBQueryMap  mongoQueryParam=new MongoDBQueryMap();
		//开始时间
		String startTimeStr="2015-10-10 13:30:00";
		//结束时间
		String endTimeStr="2015-10-10 14:35:00";

		try {
			Date startTime=startTimeStr==null?null:fmt.parse(startTimeStr);
			Date endTime=endTimeStr==null?null:fmt.parse(endTimeStr);
			Date[] ins= new Date[]{startTime,endTime};
			//查询某段时间内的数据
			mongoQueryParam.put("loginTime",ins,OpType.BETWEEN);
			String[] types = new String[]{"login_success","redirect"};
			//查询登录类型为"login_success"和"redirect"的数据
			mongoQueryParam.put("loginType",types,OpType.IN);
			//查询登录类型为"login_success"的数据
			//mongoQueryParam.put("loginType","login_success");
			//查询登录类型为"redirect"的数据
			//mongoQueryParam.put("loginType","redirect");
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("日期转换出错");
		}
			
		List<LoginLog> logs=MongoDomainLogDao.getInstance().find(LoginLog.class, mongoQueryParam);
	    for (LoginLog log : logs){
	    	System.out.println("log="+log.domainToMap());
	    }
	    System.out.println("count="+logs.size());
	
	}
	
}
