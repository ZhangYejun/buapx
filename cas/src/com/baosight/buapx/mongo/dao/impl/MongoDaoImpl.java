package com.baosight.buapx.mongo.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.baosight.buapx.mongo.dao.MongoDao;
import com.baosight.buapx.mongo.util.MongoDBQueryMap;
import com.baosight.buapx.mongo.util.MongoDBUtils;
import com.baosight.buapx.mongo.util.OpType;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.MapReduceCommand;
import com.mongodb.WriteResult;

public class MongoDaoImpl implements MongoDao {
	protected final static Log logger = LogFactory.getLog(MongoDaoImpl.class);
	
	private MongoDBUtils mongoDBUtils  = MongoDBUtils.getInstance();

	private static MongoDaoImpl dao = null;
	
	private MongoDaoImpl(){
		
	}
	
	public static MongoDaoImpl getMongoDao(){
		if(dao==null){
			dao = new MongoDaoImpl();
		}
		return dao;
	}
	
	public DBRef getDBRef(String collectionName, String id) {
		return new DBRef(mongoDBUtils.getDB(), collectionName, id);
	}

	public void mapReduce(String collectionName,String map,String reduce,String outputCollection,
			MongoDBQueryMap queryMap) {
		if(StringUtils.isEmpty(collectionName)){
			logger.error("collectionName不能为空!");
			return ;
		}
		DBCollection md = mongoDBUtils.getDB().getCollection(collectionName);
		MapReduceCommand cmd = new MapReduceCommand(md, map, reduce,outputCollection,MapReduceCommand.OutputType.REPLACE,queryMap);
		md.mapReduce(cmd);
	}

	public List<Map<String, Object>> queryForList(String collectionName,String[] returnFields,
			MongoDBQueryMap queryMap) {
		if(StringUtils.isEmpty(collectionName)){
			logger.error("collectionName不能为空!");
			return null;
		}
		BasicDBObject fieldsObject = new BasicDBObject();
		if(returnFields != null){
			for(String fields : returnFields){
				fieldsObject.append(fields, "1");
			}
		}
		
		DBCursor dbCursor = mongoDBUtils.getDB().getCollection(collectionName).find(queryMap,fieldsObject)
				.sort(sortMap(queryMap.getOderbyMap()))
				.skip(queryMap.getOffset()).limit(queryMap.getLimit());
		return dbCursor2List(dbCursor);
	}
	
	public long count (String collectionName,
			MongoDBQueryMap queryMap) {
		if(StringUtils.isEmpty(collectionName)){
			logger.error("collectionName不能为空!");
			return 0;
		}
	
		return mongoDBUtils.getDB().getCollection(collectionName).count(queryMap);
	}
	

	public List<Map<String, Object>> queryForList(String collectionName,
			MongoDBQueryMap queryMap) {
		if(StringUtils.isEmpty(collectionName)){
			logger.error("collectionName不能为空!");
			return null;
		}
		
		DBCursor dbCursor = mongoDBUtils.getDB().getCollection(collectionName).find(queryMap)
				.sort(sortMap(queryMap.getOderbyMap()))
				.skip(queryMap.getOffset()).limit(queryMap.getLimit());
		return dbCursor2List(dbCursor);
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> dbCursor2List(DBCursor dbCursor){
		if(dbCursor == null){
			return null;
		}
		 List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Iterator<DBObject> iter = dbCursor.iterator();
		while(iter.hasNext()){
			DBObject dbObject =  iter.next();
			list.add(dbObject.toMap());
		}
		return list;
	}
	
	private BasicDBObject sortMap(Map<String,OpType> orderyMap){
		if(orderyMap == null){
			return null;
		}
		Iterator<String> iter = orderyMap.keySet().iterator();
		BasicDBObject sortDB = new BasicDBObject();
		while(iter.hasNext()){
			String key = iter.next();
			switch (orderyMap.get(key)) {
			case ASC:
				sortDB.append(key, 1);
				break;
			case DESC:
				sortDB.append(key, -1);
				break;
			default:
				logger.error("queryMap.getOderbyMap()中的key:" + key + "排序类型不正确!");
				break;
			}
		}
		return sortDB;
	}

	@SuppressWarnings("unchecked")

	public Map<String, Object> findById(String collectionName,
			String id) {
		if(collectionName == null || id == null){
			return null;
		}
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.append("_id", new ObjectId(id));
		return mongoDBUtils.getDB().getCollection(collectionName).findOne(dbObject).toMap();
	}


	public Long queryForInt(String collectionName, MongoDBQueryMap queryMap) {
		if(collectionName == null){
			logger.error("collectionName不能为空!");
			return null;
		}
		return mongoDBUtils.getDB().getCollection(collectionName).getCount(queryMap);
	}


	public boolean insert(String collectionName, Map<String, Object> dataMap) {
		if(collectionName == null){
			logger.error("collectionName不能为空!");
			return false;
		}
		BasicDBObject bdObejct = new BasicDBObject(dataMap);
		WriteResult wr = mongoDBUtils.getDB().getCollection(collectionName).save(bdObejct);
		if (wr.getError() != null) {
			logger.error("新增失败！"+wr.getError());
			return false;
		}else{
			return true;
		}
	}


	public boolean delete(String collectionName, MongoDBQueryMap queryMap) {
		if(collectionName == null){
			logger.error("collectionName不能为空!");
			return false;
		}
		WriteResult wr = mongoDBUtils.getDB().getCollection(collectionName).remove(queryMap);
		if (wr.getError() != null) {
			logger.error("删除失败！"+wr.getError());
			return false;
		}else{
			return true;
		}
	}


	public boolean deleteById(String collectionName, String id) {
		if(collectionName == null || id == null){
			return false;
		}
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("_id", new ObjectId(id));
		WriteResult wr = mongoDBUtils.getDB().getCollection(collectionName).remove(dbObject);
		if (wr.getError() != null) {
			logger.error("删除失败！"+wr.getError());
			return false;
		}else{
			return true;
		}
	}
	

	public int update(String collectionName,
			MongoDBQueryMap queryMap,Map<String, Object> dataMap) {
		if(collectionName == null){
			logger.error("collectionName不能为空!");
			return 0;
		}
		int counts = 0;
		DBCollection dbColl = mongoDBUtils.getDB().getCollection(collectionName);
		DBCursor dbCursor = dbColl.find(queryMap);
		Iterator<DBObject> iter = dbCursor.iterator();
		while(iter.hasNext()){
			DBObject dbObject = iter.next();
			dbObject.putAll(dataMap);
			WriteResult wr = dbColl.save(dbObject);;
			if (wr.getError() != null) {
				logger.error("修改失败！"+wr.getError());
			}else{
				counts++;
			}
		}
		return counts;
	}


	public int updateById(String collectionName,
			String id,Map<String, Object> dataMap) {
		if(collectionName == null){
			logger.error("collectionName不能为空!");
			return 0;
		}
		int counts = 0;
		DBCollection dbColl = mongoDBUtils.getDB().getCollection(collectionName);
		DBObject dbObject = dbColl.findOne(new BasicDBObject("_id",new ObjectId(id)));		
		dbObject.putAll(dataMap);
		WriteResult wr = dbColl.save(dbObject);;
		if (wr.getError() != null) {
				logger.error("修改失败！"+wr.getError());
		}else{
			counts++;
		}
		return counts;
	}
	
}
