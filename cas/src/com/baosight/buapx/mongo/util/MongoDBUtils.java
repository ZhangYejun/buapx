package com.baosight.buapx.mongo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.baosight.buapx.mongo.common.MongoDBFactory;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;

public class MongoDBUtils {
	private static final Logger logger = Logger.getLogger(MongoDBUtils.class);
	private Map<String, Object> mongoPro = null;
	private Mongo mongo = null;
	private static MongoDBUtils mongoDbUtils = null;

	public static final String MONGODB_PRO = "mongoDB"; // mongoDB.properties的文件名称
	public static final String DB_NAME = "DBName"; // mongoDB的DB名称(对应关系型数据库名称)
	public static final String REPLICASETSEEDS = "replicaSetSeeds";// mongoDB集群服务
	public static final String CONNECTION_PER_HOST="connectionsPerHost";
	public static final String MAX_WAIT_TIME="maxWaitTime";
	public static final String CONNECT_TIME_OUT="connectTimeout";

	public static MongoDBUtils getInstance() {
		if (mongoDbUtils == null) {
			synchronized (MongoDBUtils.class) {
				if (mongoDbUtils == null) {
					mongoDbUtils = new MongoDBUtils();
					mongoDbUtils.getMongo();
				}
			}
		}
		return mongoDbUtils;
	}

	/**
	 * 通过mongoDB.properties配置文件获取mongo实例
	 */
	private void getMongo() {
		mongoPro = PropertiesUtil.getInstance().read(MONGODB_PRO);

		MongoDBFactory mongoDBFactory = MongoDBFactory.getInstance();

		String replicaSetSeeds = (String) mongoPro.get(REPLICASETSEEDS);

		if (replicaSetSeeds != null && replicaSetSeeds.length() > 0) {
			String[] replicas = replicaSetSeeds.split(";");
			int length = replicas.length;
			List<ServerAddress> listServerAddress = new ArrayList<ServerAddress>();
			for (int i = 0; i < length; i++) {
				String[] servers = replicas[i].split(":");
				int servers_length = servers.length;
				if (servers_length > 0) {
					try {
						if (servers_length == 2) {
							listServerAddress.add(new ServerAddress(servers[0],
									Integer.parseInt(servers[1])));
						} else {
							listServerAddress
									.add(new ServerAddress(servers[0]));
						}
					} catch (Exception e) {
						logger.error("集群服务配置错误,访问不到服务!");
						e.printStackTrace();
					}
				}
			}
			mongoDBFactory.setReplicaSetSeeds(listServerAddress);
			mongoDBFactory.setConnectionsPerHost((Integer.parseInt((String)mongoPro.get(CONNECTION_PER_HOST))));
			mongoDBFactory.setConnectTimeout((Integer.parseInt((String)mongoPro.get(CONNECT_TIME_OUT))));
			mongoDBFactory.setMaxWaitTime((Integer.parseInt((String)mongoPro.get(MAX_WAIT_TIME))));
		}
		mongo = MongoDBFactory.getInstance().getMongo();
	}

	/**
	 * 通过mongDB.propertes文件的DBName获取mongoDB的数据库实例
	 * 
	 * @return
	 */
	public DB getDB() {
		DB db = null;
		try {
			if (mongo != null) {
				Object db_name = mongoPro.get(DB_NAME);
				if (db_name != null) {
					db = mongo.getDB((String) db_name);
				}
			}
		} catch (Exception ex) {
			logger.error("获取DB失败!",ex.getCause());
		}
		return db;
	}

	/**
	 * 获取mongoDB的数据库实例
	 * 
	 * @param dbName
	 *            数据库名称
	 * @return
	 */
	public DB getDB(String dbName) {
		DB db = null;
		try {
			if (mongo != null) {
				db = mongo.getDB(dbName);
			}
		} catch (Exception ex) {
			logger.error("获取DB失败!",ex.getCause());
		}
		return db;
	}

	/**
	 * 获得DBRef
	 * @param collectionName
	 * @param id
	 * @return
	 */
	public DBRef getDBRef(String collectionName, String id) {
		try {
			return new DBRef(getDB(), collectionName, new ObjectId(id));
		} catch (Exception e) {
			logger.error("获得引用错误，由id错误引起的，error：" + id);
		}
		return null;
	}
	
	public DBObject DBRefToDBObject(DBRef ref){   
		  if(ref == null){
			  return null;   
		  }
		  return ref.fetch();   
	 }   

	public List<DBObject> DBRefDBObject(List<DBRef> refList){   
		  if(refList == null){
			  return null;   
		  }
		  List<DBObject> resultList=new ArrayList<DBObject>();   
		  for (DBRef ref : refList) {   
		   resultList.add(ref.fetch());   
		  }   
		  return resultList;   
	 }   

	/**
	 * 去掉重复的引用
	 * @param list
	 * @return
	 */
	 public List<DBRef> removeRepeat(List<DBRef> list){   
		if(list == null){
			  return null;   
		}
		List<DBRef> result = new ArrayList<DBRef>();   
		Map<String,String> tempMap = new HashMap<String,String>();   
		for (int i = 0; i < list.size(); i++) {   
			DBRef ref = list.get(i);   
			String id = ref.getId().toString();   
		    if(tempMap.get(id) == null){   
		    	tempMap.put(id, id);   
		    	result.add(ref);   
		   }   
		  }   
		  return result;   
	 } 
	 
	 public List<String> removeRepeatString(List<String> list){   
		  if(list==null){
			  return null;   
		  }
		  List<String> result = new ArrayList<String>();   
		  Map<String,String> tempMap = new HashMap<String,String>();   
		  for (int i = 0; i < list.size(); i++) {   
		   String str = list.get(i);   
		   if(tempMap.get(str) == null){   
		    tempMap.put(str, str);   
		    result.add(str);   
		   }   
		  }   
		  return result;   
		 }   


	/**
	 * 删除指定的引用
	 * @param list
	 * @param removeId
	 * @return
	 */
	 public List<DBRef> removeRef(List<DBRef> list,String removeId){   
		  if(list==null){
			  return null;   
		  }
		  List<DBRef> result = new ArrayList<DBRef>();   
		  for (DBRef ref : list) {   
		   if(removeId.equals(ref.getId().toString())){   
		    continue;   
		   }   
		   result.add(ref);   
		  }   
		  return result;   
		 }   
}
