package com.baosight.buapx.mongo.util;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

/**
 * 查询Map
 * @author huangxin
 *
 */
public class MongoDBQueryMap extends BasicDBObject{

	private static final long serialVersionUID = 6262101859616105039L;
	private static final Logger logger = Logger.getLogger(MongoDBQueryMap.class);
	private int offset;
	private int limit;
	private Map<String,OpType> oderbyMap;
	
	public int getOffset() {
		return offset;
	}

	/**
	 * 设置分页的偏移量
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}
	
	/**
	 * 设置每页纪录条数
	 * @param limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Map<String,OpType> getOderbyMap() {
		return oderbyMap;
	}

	public void setOderbyMap(Map<String,OpType> oderbyMap) {
		this.oderbyMap = oderbyMap;
	}

	public String toString(){
		return JSON.serialize(this);
	}
	

	
	/**
	 * 存放查询条件
	 * @param key 字段名称
	 * @param value 值
	 * @param opType 查询类型   '<','>','<='.'like','in'等等... 请使用OPType类型 例如OPType.like,
	 */
	private void setValue(String key,Object value,OpType opType){
		switch (opType) {
		case EQ:
			this.append(key,value);
			break;
		case LIKE:
			Pattern pattern = null;
			if(value != null){
				if(value instanceof String){
					pattern = Pattern.compile("^.*"  + value +  ".*$" ,  Pattern.CASE_INSENSITIVE);
				}else {
				    pattern = Pattern.compile("^.*"  + value.toString() +  ".*$" ,  Pattern.CASE_INSENSITIVE);
				}
				this.append(key, pattern);
			}else{
				this.append(key, pattern+"");
			}
			break;
		case IN:
			if(value != null){
				if(value instanceof Object[]){
					this.append(key,new BasicDBObject("$in",value));
				}else {
					this.append(key,new BasicDBObject("$in",new Object[]{value}));
				}
			}
			break;
		case NIN:
			if(value != null){
				if(value instanceof Object[]){
					this.append(key,new BasicDBObject("$nin",value));
				}else {
					this.append(key,new BasicDBObject("$nin",new Object[]{value}));
				}
			}
			break;
		case GT:
			this.append(key, new BasicDBObject("$gt",value));
			break;
		case GTE:
			this.append(key, new BasicDBObject("$gte",value));
			break;
		case LT:
			this.append(key, new BasicDBObject("$lt",value));
			break;
		case LTE:
			this.append(key, new BasicDBObject("$lte",value));
			break;
		case NE:
			this.append(key, new BasicDBObject("$ne",value));
			break;
		case OR:
			if(value != null && (value instanceof Object[])){
				BasicDBList dbList = new BasicDBList();
				for(Object o : (Object[])value){
					dbList.add(new BasicDBObject(key,o));
				}
				this.append("$or",dbList);
			}else{
				logger.warn("当前：key="+key+";解析OR：Value类型不正确，Value请使用Object[]类型");
			}
			break;
		case BETWEEN:
			if(value != null && (value instanceof Object[])){
				Object[] objects = (Object[])value;
				BasicDBObject bdb = new BasicDBObject();
				bdb.append("$gte",objects[0]);
				bdb.append("$lte",objects[1]);
				this.append(key,bdb);
			}else{
				logger.warn("当前：key="+key+";解析OR：Value类型不正确，Value请使用Object[]类型");
			}
			break;
		default:
			//this.append(key, value);
			break;
		}
	}
	
	/**
	 * 存放查询条件
	 * @param key 字段名称
	 * @param value 值
	 * @param opType 查询类型   '<','>','<='.'like','in'等等... 请使用OPType类型 例如OPType.like,
	 */
	public void put(String key,Object value,OpType opType){
		if(value != null){
			if(value instanceof String && StringUtils.isNotEmpty((String)value)){
				setValue(key, value, opType);
			}else{
				setValue(key, value, opType);
			}
			
		}
	}
	
	/**
	 * 存放查询条件
	 * @param key 字段名称
	 * @param value 值
	 * @param opType 查询类型   '<','>','<='.'like','in'等等... 请使用OPType类型 例如OPType.like,
	 * @param isEmpty 是否需要为空
	 */
	public void put(String key,Object value,OpType opType,Boolean isEmpty){
		if(isEmpty){
			put(key,value,opType);
		}
	}
	
	/**
	 * 查询条件
	 * @param key 字段名称
	 * @param value 值
	 * @param isEmpty 是否需要为空
	 */
	public void put(String key,Object value,Boolean isEmpty){
		if(isEmpty){
			this.append(key, value);
		}
	}
}
