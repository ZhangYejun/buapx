package com.baosight.buapx.mongo.util;

import java.util.List;
import java.util.Map;
import com.baosight.buapx.mongo.Event;
import com.baosight.buapx.mongo.dao.impl.MongoDaoImpl;
//import com.baosight.common.utils.JavaBeanUtil;

public class Test {
	public static void main(String[] args) {
		MongoDaoImpl mongoDao = MongoDaoImpl.getMongoDao();
		System.out.println(mongoDao.hashCode());
		MongoDBQueryMap queryMap = new MongoDBQueryMap();
		queryMap.setLimit(10);
		queryMap.setOffset(20);
		List<Map<String,Object>> list  = mongoDao.queryForList("event", queryMap);
		for(Map<String,Object> map : list){
			Event event = new Event();
		//	JavaBeanUtil.map2bean(event, map, true);
			System.out.println(map.toString());
			System.out.println();
			System.out.println(event);
		}
	}
}
