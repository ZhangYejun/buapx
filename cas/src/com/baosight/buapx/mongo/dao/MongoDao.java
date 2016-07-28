package com.baosight.buapx.mongo.dao;


import java.util.List;
import java.util.Map;

import com.baosight.buapx.mongo.util.MongoDBQueryMap;
import com.mongodb.DBRef;

public interface MongoDao {


	public  DBRef getDBRef(String collectionName, String id);

	/**
	 * 基本查询方法
	 * @param collectionName 查询表集合名
	 * @param returnFields 返回字段集合
	 * @param queryMap 查询条件
	 * @return List<Map<String,Object>> 结果集
	 */
	public  List<Map<String,Object>> queryForList(String collectionName,String[] returnFields,
			MongoDBQueryMap queryMap);

	/**
	 * 基本查询方法
	 * @param collectionName 查询表集合名
	 * @param queryMap 查询条件
	 * @return List<Map<String,Object>> 结果集
	 */	
	public List<Map<String, Object>> queryForList(String collectionName,
			MongoDBQueryMap queryMap);

	/**
	 * 通过ID查询
	 * @param collectionName 查询表集合名
	 * @param id 查询条件ID
	 * @return Map 结果集
	 */
	public  Map<String, Object> findById(String collectionName,
			String id);

	
	/**
	 * 获取记录条数
	 * @param collectionName 查询表集合名
	 * @param queryMap 查询条件
	 * @return Long
	 */
	public Long queryForInt(String collectionName,
			MongoDBQueryMap queryMap);
	
	
	/**
	 * 基本增加方法
	 * @param collectionName 表集合名
	 * @param dataMap 字段值
	 * @return id字符串
	 */
	public boolean insert(String collectionName, Map<String, Object> dataMap);


	/**
	 * 基本删除方法
	 * @param collectionName 表集合名
	 * @param condMap 删除条件
	 * @return
	 */
	public  boolean delete(String collectionName,
			MongoDBQueryMap queryMap);

	/**
	 * 基本删除方法
	 * @param collectionName 表集合名
	 * @param id 删除条件ID
	 * @return
	 */
	public  boolean deleteById(String collectionName,
			String id);
	/**
	 * 基本更新方法
	 * @param collectionName 表集合名
	 * @param queryMap 更新条件的 数据集
	 * @param dataMap 更新的数据集
	 * @return boolean
	 */
	public int update(String collectionName,MongoDBQueryMap queryMap,Map<String, Object> dataMap);

	/**
	 * 通过ID基本更新方法
	 * @param collectionName 表集合名
	 * @param id _id值
	 * @param dataMap 更新的数据集
	 * @param queryMap 更新条件的 数据集
	 * @param isUpdateModifyTime 是否修改Constants.Field_all_MODIFYTIME字段，true为修改，false为不修改
	 * @return  0--修改成功   1--数据库中无此记录，修改失败
	 */
	public int updateById(String collectionName,
			String id,Map<String, Object> dataMap);
	
	
	public void mapReduce(String collectionName,String map,String reduce,String outputCollection,
			MongoDBQueryMap queryMap);
	
	public long count (String collectionName,
			MongoDBQueryMap queryMap);

}