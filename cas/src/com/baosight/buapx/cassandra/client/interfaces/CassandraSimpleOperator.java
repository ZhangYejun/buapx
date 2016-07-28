package com.baosight.buapx.cassandra.client.interfaces;
/**
 * <p>cassandra操作类接口
 * <code>NoSqlOperator.java</code>。
 * </p>
 * @author lizidi
 * @version  0.2 2011-2-8 下午16:32:05
 */
import java.util.List;
import java.util.Map;

import com.baosight.buapx.cassandra.client.hector.StringIndexExpression;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.SuperRows;
import me.prettyprint.hector.api.beans.SuperSlice;

public interface CassandraSimpleOperator {
	/**
	 * query single column of one row
	 * @param key
	 * @param columnFamily
	 * @param column value
	 * @return the value of column
	 */
	String getColumn(String key,final String columnFamily,String column);
	/**
	 * query multi columns of one row
	 * @param key
	 * @param columnFamily
	 * @param columns
	 * @return the row object,like a Map<column,value>
	 */
	ColumnSlice<String, String> getColumns(String key,final String columnFamily,String... columns);
	/**
	 * 
	 * @param key
	 * @param columnFamily
	 * @param columnRangeStart
	 * @param columnRangeEnd
	 * @param count
	 * @return
	 */
	ColumnSlice<String, String> getColumns(String key,final String columnFamily,String columnRangeStart,String columnRangeEnd,boolean reverse,int count);

	/**
	 *  query multi rows
	 * @param columnFamliy
	 * @param columns
	 * @param keys
	 * @return  the rows map
	 */
	Map<String, ColumnSlice<String, String>> getSlices(final String columnFamliy,final String[] columns,String... keys);
	/**
	 * query multi rows by indexed column value
	 * @param columnFamily
	 * @param indexExpresssions
	 * @param count
	 * @param columns
	 * @return  ordered multi rows object
	 */
	OrderedRows<String, String, String> getIndexedSlices(String columnFamily,List<StringIndexExpression> indexExpresssions,int count,String... columns);
	/**
	 * query multi rows by range of keys
	 * @param columnFamily
	 * @param keyStart
	 * @param keyEnd
	 * @param count
	 * @return  ordered multi rows object
	 */
	OrderedRows<String, String, String> getRangedSlices(String columnFamily,String keyStart,String keyEnd,int count,String...columns);
	/**
	 * query singele SuperColumn of one row
	 * @param columnFamily
	 * @param superColumnName
	 * @param key
	 * @return  The  SuperColumn
	 */
	HSuperColumn<String, String, String> getSuperColumn(String columnFamily,String superColumnName,String key);
	 /**
	  * query multi SuperColumns of one row
	  * @param columnFamily
	  * @param key
	  * @param superColumnNames
	  * @return  the SuperColumn row object
	  */
	SuperSlice<String, String, String> getSuperColumns(String columnFamily,String key,String... superColumnNames);
	/**
	 * 
	 * @param columnFamily
	 * @param key
	 * @param columnRangeStart
	 * @param columnRangeEnd
	 * @param count
	 * @return
	 */
	SuperSlice<String, String, String> getSuperColumns(String columnFamily,String key,String columnRangeStart,String columnRangeEnd,boolean reverse,int count);
	/**
	 * query multi rows of SuperColumns
	 * @param columnFamily
	 * @param superColumnNames
	 * @param keys
	 * @return the multi SuperColumn rows object
	 */
	SuperRows<String, String, String, String> getSlicesForSuperColumn(String columnFamily,String[] superColumnNames,String... keys );
	/**
	 * 
	 * @param columnFamily
	 * @param key
	 * @param superColumn
	 * @param subColumns
	 * @return
	 */
	ColumnSlice<String,String> getSubColumns(String columnFamily,String key,String superColumn,String... subColumns);
	/**
	 * insert single column of one row
	 * @param key
	 * @param column
	 * @param value
	 * @param columnFamily
	 */
	 void insertColumn(String key,String column,String value,final String columnFamily);
	 /**
	  * 
	  * @param key
	  * @param column
	  * @param ttl
	  * @param value
	  * @param columnFamily
	  */
	 void insertColumn(String key,String column,int ttl,String value,final String columnFamily);
	 /**
	  * insert multi columns of one row
	  * @param key
	  * @param columnValues
	  * @param columnFamily
	  */
	 void insertColumns(String key,Map<String,String>columnValues,final String columnFamily);
	/**
	 * 
	 * @param key
	 * @param columnValues
	 * @param ttl
	 * @param columnFamily
	 */
	 void insertColumns(String key,Map<String,String>columnValues,int ttl,final String columnFamily);
	 /**
	  * insert multi rows
	  * @param keyValues
	  * @param column
	  * @param columnFamily
	  */
	 void insertSlices(Map<String, String> keyValues,String column,final String columnFamily);
	/**
	 * 
	 * @param keyValues
	 * @param column
	 * @param ttl
	 * @param columnFamily
	 */
	 void insertSlices(Map<String, String> keyValues,String column,int ttl,final String columnFamily);
	 /**
	  * insert singe SuperColumn of one row
	  * @param columnValues
	  * @param key
	  * @param superColumn
	  * @param columnFamily
	  */
	 void insertSuperColumn(Map<String,String> columnValues,String key,String superColumn,String columnFamily);
	 /**
	  * 
	  * @param columnValues
	  * @param key
	  * @param superColumn
	  * @param ttl
	  * @param columnFamily
	  */
	 void insertSuperColumn(Map<String,String> columnValues,String key,String superColumn,int ttl,String columnFamily);
	 /**
	  * 
	  * @param key
	  * @param columnFamily
	  * @param superColumnName
	  * @param columns
	  */
	 void insertSuperColumn(String key,String columnFamily,String superColumnName,List<HColumn<String,String>> columns);/**
	 * delete multi rows
	 * @param columnFamily
	 * @param column
	 * @param keys
	 */
	 void deleteSlices(final String columnFamily,String column,String... keys);
	/**
	 * delte single row of prefered columns
	 * @param columnFamily
	 * @param key
	 * @param columns
	 */
	 void delete(final String columnFamily,String key,String... columns);
	/**
	 * 
	 * @param columnFamily
	 * @param key
	 * @param superColumns
	 */
	 void deleteSuperColumns(final String columnFamily,String key,String... superColumns);
	 
	 /**
	 * Truncate all data of columnFamily
	 * @param columnFamliy
	 */
	
	void truncate(final String columnFamliy);
}
