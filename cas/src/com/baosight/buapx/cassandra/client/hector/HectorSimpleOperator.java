package com.baosight.buapx.cassandra.client.hector;

/**
 * <p>Hector实现的Cassandra简单操作类
 * <code>HectorSimpleOperator.java</code>
 * </p>
 * @author lizidi
 * @version HectorSimpleOperator.java 0.2 2011-7-7 下午14:32:05
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.model.AllOneConsistencyLevelPolicy;
import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.beans.SuperRows;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.MultigetSuperSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.hector.api.query.SubSliceQuery;
import me.prettyprint.hector.api.query.SuperColumnQuery;
import me.prettyprint.hector.api.query.SuperSliceQuery;

import com.baosight.buapx.cassandra.client.domain.ClientConfig;
import com.baosight.buapx.cassandra.client.interfaces.CassandraSimpleOperator;
 
public class HectorSimpleOperator implements CassandraSimpleOperator {

	private ClientConfig clientConfig = null;
	private Cluster cluster = null;
	private final StringSerializer serializer = StringSerializer.get();
    private ThreadLocal<Keyspace> keyspace;
	/**
	 * 
	 * @param clientConfig
	 *            客户端配置
	 */

	public HectorSimpleOperator(ClientConfig clientConfig) {
		this.clientConfig = clientConfig;
		this.cluster = HectorClientConfigurator
				.getOrCreateCluster(clientConfig);		
		this.keyspace=new ThreadLocal<Keyspace>();
		
	}


	@Override
	public String getColumn(String key,final String columnFamily,String column) {
		ColumnQuery<String, String, String> q = HFactory
				.createStringColumnQuery(getKeyspace());
		QueryResult<HColumn<String, String>> r = q
				.setColumnFamily(columnFamily)
				.setKey(key).setName(column).execute();
		HColumn<String, String> c = r.get();
		return c == null ? null : c.getValue();

	}

	@Override
	public ColumnSlice<String, String> getColumns(String key,final String columnFamily,String... columns){
		 SliceQuery<String, String, String>  sq=HFactory.createSliceQuery(getKeyspace(), serializer, serializer, serializer);
		 QueryResult<ColumnSlice<String, String>>  result=sq.setColumnFamily(columnFamily).setColumnNames(columns).setKey(key).execute();
		 ColumnSlice<String, String> cs=result.get();
		 return cs;
		 
	}
	@Override
	public Map<String, ColumnSlice<String, String>> getSlices(final String columnFamliy,final String[] columns,String... keys) {
		MultigetSliceQuery<String, String, String> q = HFactory
				.createMultigetSliceQuery(getKeyspace(), serializer,
						serializer, serializer);
		q.setColumnFamily(columnFamliy);
		q.setKeys(keys);	
        q.setColumnNames(columns);
		QueryResult<Rows<String, String, String>> r = q.execute();
		Rows<String, String, String> rows = r.get();
		Map<String, ColumnSlice<String, String>> ret = new HashMap<String, ColumnSlice<String, String>>(keys.length);
		for (String k : keys) {
			ColumnSlice<String, String> cs = rows.getByKey(k).getColumnSlice();
			if (cs!= null) {
				ret.put(k,cs);
			}
		}
		return ret;
	}
	@Override
	public HSuperColumn<String, String, String> getSuperColumn(String columnFamily,String superColumnName,String key){
		 SuperColumnQuery<String, String, String, String> superColumnQuery = 
             HFactory.createSuperColumnQuery(getKeyspace(), serializer, serializer, 
                     serializer, serializer);
              superColumnQuery.setColumnFamily(columnFamily).setKey(key).setSuperName(superColumnName);
              QueryResult<HSuperColumn<String, String, String>> result = superColumnQuery.execute();
              HSuperColumn<String, String, String> spColumn=result.get();
              return spColumn;
	}
	
	@Override
	public SuperSlice<String, String, String> getSuperColumns(String columnFamily,String key,String... superColumnNames){
		SuperSliceQuery<String, String, String, String> superSliceQuery=HFactory.createSuperSliceQuery(getKeyspace(), serializer, serializer, serializer, serializer);
		superSliceQuery.setColumnFamily(columnFamily);
		superSliceQuery.setColumnNames(superColumnNames);
		superSliceQuery.setKey(key);
		QueryResult<SuperSlice<String, String, String>>  result=superSliceQuery.execute();
		return result.get();
	}
	@Override
	public  SuperRows<String, String, String, String> getSlicesForSuperColumn(String columnFamily,String[] superColumnNames,String... keys ){
		MultigetSuperSliceQuery<String,String,String,String>  multiSuperSliceQuery=HFactory.createMultigetSuperSliceQuery(getKeyspace(), serializer, serializer, serializer, serializer);
		 multiSuperSliceQuery.setColumnFamily(columnFamily);
		multiSuperSliceQuery.setColumnNames(superColumnNames); 		 
		 multiSuperSliceQuery.setKeys(keys);
		 QueryResult<SuperRows<String, String, String, String>>  result=multiSuperSliceQuery.execute();
		 SuperRows<String, String, String, String> superRows=result.get();
		 return superRows;
	}
	
	@Override
	public OrderedRows<String, String, String> getRangedSlices(String columnFamily,String keyStart,String keyEnd,int count,String...columns){
		RangeSlicesQuery<String, String, String> rangeSlicesQuery = 
            HFactory.createRangeSlicesQuery(getKeyspace(), serializer, serializer, serializer);
        rangeSlicesQuery.setColumnFamily(columnFamily);            
        rangeSlicesQuery.setKeys(keyStart, keyEnd);
        if(count>0){
        	rangeSlicesQuery.setRowCount(count);
        }       
        rangeSlicesQuery.setColumnNames(columns);
        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        OrderedRows<String, String, String> orderedRows = result.get();     
        return orderedRows;        
	}
	@Override
	public OrderedRows<String, String, String> getIndexedSlices(String columnFamily,List<StringIndexExpression> indexExpresssions,int count,String... columns){
		IndexedSlicesQuery<String, String, String> indexedSlicesQuery = 
        HFactory.createIndexedSlicesQuery(getKeyspace(), serializer, serializer, serializer);      
        indexedSlicesQuery.setColumnNames(columns);
        if(count>0){
        	indexedSlicesQuery.setRowCount(count);
        }
        indexedSlicesQuery.setColumnFamily(columnFamily);
        for(StringIndexExpression exp:indexExpresssions){
        	switch(exp.getIndexOperator()){
        	     case EQ:
        	    	     indexedSlicesQuery.addEqualsExpression(exp.getColumn(), exp.getValue());
        	             break;
        	     case GT:
        	    	     indexedSlicesQuery.addGtExpression(exp.getColumn(), exp.getValue());
	                     break;
        	     case LT:
        	    	     indexedSlicesQuery.addLtExpression(exp.getColumn(), exp.getValue());
	                     break;  
        	     case GTE:
        	    	     indexedSlicesQuery.addGteExpression(exp.getColumn(), exp.getValue());
    	                 break;
        	     case LTE:
        	    	     indexedSlicesQuery.addLteExpression(exp.getColumn(), exp.getValue());
    	                 break;
        	}
        }
        QueryResult<OrderedRows<String, String, String>> result = indexedSlicesQuery.execute();
        return result.get();
	}

	@Override
	public void insertColumn(String key,String column,String value,final String columnFamily) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		m.insert(key, columnFamily, HFactory
				.createColumn(column, value, serializer,
						serializer)); 
	}

	@Override
	public void insertColumns(String key,Map<String,String>columnValues,final String columnFamily) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		for(Map.Entry<String, String> columnValue : columnValues.entrySet()){
			m.addInsertion(key, columnFamily, HFactory
					.createStringColumn(columnValue.getKey(),columnValue.getValue())); 
		}

		m.execute();
	}
	
	@Override
	public void insertSlices(Map<String, String> keyValues,String column,final String columnFamily) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		for (Map.Entry<String, String> keyValue : keyValues.entrySet()) {
			m.addInsertion(keyValue.getKey(), 
					 columnFamily, HFactory.createStringColumn(
					column, keyValue.getValue()));		
		}
		m.execute();
	} 

	@Override
	public void insertSuperColumn(Map<String,String> columnValues,String key,String superColumn,String columnFamily){
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(), serializer);
		List<HColumn<String,String>> columns=new ArrayList<HColumn<String,String>>();
		for(Map.Entry<String, String> e:columnValues.entrySet()){
			columns.add(HFactory.createStringColumn(e.getKey(), e.getValue()));
		}
        mutator.insert(key,columnFamily, HFactory.createSuperColumn(superColumn, 
                columns,serializer, serializer, serializer));
		
	}
	@Override
	public void deleteSlices(final String columnFamily,String column,String... keys) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		for (String key : keys) {
			m.addDeletion(key, columnFamily,column, serializer);
		}
		m.execute();
	}
	@Override
	public void delete(final String columnFamily,String key,String... columns){
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		for (String column : columns) {
			m.addDeletion(key, columnFamily,column, serializer);
		}
		m.execute();
	}

	/**
	 * Gets the keyspace.
	 * 
	 * @return the keyspace
	 */
	private Keyspace getKeyspace() {		   
		     if(this.keyspace.get()==null){
		    	 SimpleConsistencyLevelPolicy policy=new SimpleConsistencyLevelPolicy(clientConfig.getReadConsistencyLevelPolicy(), clientConfig.getWriteConsistencyLevelPolicy());
		 		 Keyspace keyspace=HFactory.createKeyspace(
		 		clientConfig.getKeyspaceName(), cluster, policy,FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE);
		 		 this.keyspace.set(keyspace);		 		 
		     }else{
		     }
		     return this.keyspace.get();
	}

	@Override
	public void truncate(final String columnFamliy) {
		cluster.truncate(clientConfig.getKeyspaceName(), columnFamliy);		
	}


	@Override
	public void insertColumn(String key, String column, int ttl, String value,
			String columnFamily) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		HColumn<String, String> c=HFactory.createColumn(column, value, serializer,serializer);
		c.setTtl(ttl);
		m.insert(key, columnFamily,c); 
		
	}


	@Override
	public void insertColumns(String key, Map<String, String> columnValues,
			int ttl, String columnFamily) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		HColumn<String, String> c=null;
		for(Map.Entry<String, String> columnValue : columnValues.entrySet()){
			c=HFactory
			.createStringColumn(columnValue.getKey(),columnValue.getValue());
			c.setTtl(ttl);
			m.addInsertion(key, columnFamily,c ); 
		}
		m.execute();
		
	}


	@Override
	public void insertSlices(Map<String, String> keyValues, String column,
			int ttl, String columnFamily) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		HColumn<String, String> c=null;
		for (Map.Entry<String, String> keyValue : keyValues.entrySet()) {
			c=HFactory.createStringColumn(column, keyValue.getValue());
			c.setTtl(ttl);
			m.addInsertion(keyValue.getKey(), 
					 columnFamily,c);		
		}
		m.execute();
		
	}


	@Override
	public void insertSuperColumn(Map<String, String> columnValues, String key,
			String superColumn, int ttl, String columnFamily) {
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(), serializer);
		List<HColumn<String,String>> columns=new ArrayList<HColumn<String,String>>();
		HColumn<String, String> c=null;
		for(Map.Entry<String, String> e:columnValues.entrySet()){
			c=HFactory.createStringColumn(e.getKey(), e.getValue());
			c.setTtl(ttl);
			columns.add(c);
		}
        mutator.insert(key,columnFamily, HFactory.createSuperColumn(superColumn, 
                columns,serializer, serializer, serializer));
		
	}


	@Override
	public void insertSuperColumn(String key, String columnFamily,
			String superColumnName, List<HColumn<String, String>> columns) {
		Mutator<String> mutator = HFactory.createMutator(getKeyspace(), serializer);
        mutator.insert(key,columnFamily, HFactory.createSuperColumn(superColumnName, 
                columns,serializer, serializer, serializer));
		
	}
	
	public ColumnSlice<String,String> getSubColumns(String columnFamily,String key,String superColumn,String... subColumns){
		SubSliceQuery<String, String, String, String> query=HFactory.createSubSliceQuery(getKeyspace(), serializer, serializer, serializer, serializer);
		QueryResult<ColumnSlice<String, String>> result= query.setColumnFamily(columnFamily)
             .setColumnNames(subColumns)
             .setKey(key)
             .setSuperColumn(superColumn)
             .execute();
		return result.get();
	}


	@Override
	public void deleteSuperColumns( String columnFamily, String key,
			String... superColumns) {
		Mutator<String> m = HFactory.createMutator(getKeyspace(), serializer);
		
		if(superColumns.length>0){
			for (String column : superColumns) {
				m.addDeletion(key, columnFamily,column, serializer);
			}	
		}else{
			m.addDeletion(key, columnFamily,null, serializer);
		}		
		m.execute();
		
	}


	@Override
	public ColumnSlice<String, String> getColumns(String key,
			String columnFamily, String columnRangeStart,
			String columnRangeEnd,boolean reverse, int count) {
		SliceQuery<String, String, String>  sq=HFactory.createSliceQuery(getKeyspace(), serializer, serializer, serializer);
		sq.setColumnFamily(columnFamily)
		   .setKey(key);
		if(reverse==true){
			sq.setRange(columnRangeStart, columnRangeEnd, reverse, count);
		}else{
			sq.setRange(columnRangeEnd,columnRangeStart,  reverse, count);
		}
		 QueryResult<ColumnSlice<String, String>>  result=sq.execute();
		 ColumnSlice<String, String> cs=result.get();
		 return cs;
	}


	@Override
	public SuperSlice<String, String, String> getSuperColumns(
			String columnFamily, String key, String columnRangeStart,
			String columnRangeEnd,boolean reverse, int count) {
		SuperSliceQuery<String, String, String, String> superSliceQuery=HFactory.createSuperSliceQuery(getKeyspace(), serializer, serializer, serializer, serializer);
		superSliceQuery.setColumnFamily(columnFamily);
		if(reverse==true){
			superSliceQuery.setRange(columnRangeEnd,columnRangeStart, reverse, count);
		}else{
			superSliceQuery.setRange(columnRangeStart,columnRangeEnd, reverse, count);
		}
		superSliceQuery.setKey(key);
		QueryResult<SuperSlice<String, String, String>>  result=superSliceQuery.execute();
		return result.get();
	}
	
	

}
