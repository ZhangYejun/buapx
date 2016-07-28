package com.baosight.buapx.mongo.util;

/**
 * 操作符
 * @author huangxin
 *
 */
public enum OpType {
	/**
	 * SQL中"="
	 */
	EQ,
	/**
	 * SQL中"like"
	 */
	LIKE,
	/**
	 * SQL中的">"
	 */
	GT,
	/**
	 * SQL中的">="
	 */
	GTE,
	/**
	 * SQL中的"<"
	 */
	LT,
	/**
	 * SQL中的"<="
	 */
	LTE,
	/**
	 * SQL中的"in"
	 */
	IN,
	/**
	 * SQL中的"not in"
	 */
	NIN,
	
	/**
	 * SQL中的"<>" 
	 */
	NE,
	
	/**
	 * SQL中的"or" 
	 */
	OR,
	/**
	 * SQL中的"asc" 
	 */
	ASC,
	/**
	 * SQL中的"desc" 
	 */
	DESC,
	/**
	 * SQL中的"value1 <= filed <= value2" 
	 */
	BETWEEN;
}
