package com.baosight.buapx.log.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baosight.buapx.log.common.BeanUtil;
import com.baosight.buapx.log.domain.IDomainLog;
import com.baosight.buapx.log.example.UserInfo;
import com.baosight.buapx.mongo.dao.MongoDao;
import com.baosight.buapx.mongo.dao.impl.MongoDaoImpl;
import com.baosight.buapx.mongo.util.MongoDBQueryMap;

public class MongoDomainLogDao {
	 private static MongoDomainLogDao dao=new MongoDomainLogDao();

	 private MongoDomainLogDao() {
	}
	 
	 public static MongoDomainLogDao getInstance(){
		 return dao;
	 }
	 
	public  <T extends IDomainLog> List<T> find(Class<T> clazz,MongoDBQueryMap queryMap){
	   MongoDao mongoDao = MongoDaoImpl.getMongoDao();
		List<Map<String,Object>> list  = mongoDao.queryForList(clazz.getSimpleName(), (com.baosight.buapx.mongo.util.MongoDBQueryMap) queryMap);
		List<T> result=new ArrayList<T>(list.size());
		for(int i=0;i<list.size();i++){
			try {
				T bean = clazz.newInstance();
				BeanUtil.mapToBean(list.get(i), bean);
				result.add(bean);
			} catch (Exception e){
				throw new RuntimeException(e);
			}

		}
		return result;
		
	 
   }
   
	
	public  <T> long count(Class<T> clazz,MongoDBQueryMap queryMap){
		   MongoDao mongoDao = MongoDaoImpl.getMongoDao();
			return mongoDao.count(clazz.getSimpleName(), (com.baosight.buapx.mongo.util.MongoDBQueryMap) queryMap);
	   }




}
