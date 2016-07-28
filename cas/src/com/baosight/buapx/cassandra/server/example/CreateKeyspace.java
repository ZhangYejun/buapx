package com.baosight.buapx.cassandra.server.example;


import com.baosight.buapx.cassandra.client.domain.ClientConfig;
import com.baosight.buapx.cassandra.server.hector.HectorSchemaManagerImpl;
import com.baosight.buapx.cassandra.server.interfaces.SchemaManager;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.BasicKeyspaceDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ExhaustedPolicy;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

public class CreateKeyspace {
	private static StringSerializer stringSerializer = StringSerializer.get();

	  public static void main(String[] args){
		  ClientConfig clientConfig=initClientConfig();//生成客户端配置	
	    	 SchemaManager schemaManager=new HectorSchemaManagerImpl(clientConfig);//新建schema操作类

		  //新建keyspace定义。如果之前已建立了keyspace，则从服务器获取keyspace定义，否则new SimpleKeyspaceDefinition()即可
	    	KeyspaceDefinition  kd=schemaManager.describeKeyspace("buapx");
	    	System.out.println("keyspace:"+kd.getName()+" "+kd.toString()) ;
	    	
	    	 /*try{
	    		 schemaManager.dropKeyspace("buapx");
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    		 return;
	    	 }*/
	    	
	    	 //创建keyspace
	    	// BasicKeyspaceDefinition keyspaceDef= getKeyspaceDef("buapx");	    	 
	    	//schemaManager.addKeyspace(keyspaceDef);
	    	 
	    	 //创建columfamily，分别运行
	        schemaManager.addColumnFamily(getColumnFamilyDef(0, "UserMapping","buapx",false,100,50), null);
	    	schemaManager.addColumnFamily(getColumnFamilyDef(0, "UserInfo","buapx",false,50000,10000), null);

	    	
	    	 
	    	 
	    	 System.out.println("运行完毕");
	    	 
	  }

	  
	  /**
	      * 生成客户端配置
	      * @return
	      */
	     private static ClientConfig initClientConfig(){
	    	 ClientConfig clientConfig=new ClientConfig();
	    	 
	    	String host = "10.46.20.58:9160"; //开发环境
		    	String clusterName = "Test Cluster";
		    	
	    	//String host = "10.46.20.59:9160"; //验证环境
	    	//String clusterName = "buapx_cluster";
	    	 
	    //	String host = "10.60.2.226:9160";//正式环境
	    	//String clusterName = "Oy Cluster";
	    	 
	    	 
	    	 clientConfig.setAddress(new String[]{host});//设置cassandra服务器地址
	    	 clientConfig.setClusterName(clusterName); //设置cassandra集群名称
	    	 clientConfig.setMaxActive(1);  //设置线程池最大线程数
	    	 clientConfig.setUsername("admin");//设置用户名
	    	 clientConfig.setPassword("password");//设置密码，须具体修改
	    	 clientConfig.setExhaustedPolicy(ExhaustedPolicy.WHEN_EXHAUSTED_BLOCK); //设置线程池策略：连接耗尽时等待
	    	 clientConfig.setMaxWaitTimeWhenExhausted(500); //设置等待线程池分配连接时间
	    	 return clientConfig;
	     }
	     
	     
	     
		    /**
		     * 生成keyspace定义 
		     * @param keyspaceName
		     * @return
		     */
		     private static BasicKeyspaceDefinition getKeyspaceDef(String keyspaceName){
		    	 BasicKeyspaceDefinition keyspaceDef=new BasicKeyspaceDefinition();
		    	 keyspaceDef.setName(keyspaceName);
		    	 //验证环境策略
		    	 keyspaceDef.setStrategyClass("org.apache.cassandra.locator.SimpleStrategy");
		    	keyspaceDef.setReplicationFactor(1);//2台机器
		    	 //end 验证环境
		      //正式环境策略
		     /* keyspaceDef.setReplicationFactor(4);//4台机器
		      keyspaceDef.setStrategyClass("org.apache.cassandra.locator.NetworkTopologyStrategy");//设置服务器位置策略
		   	  keyspaceDef.setStrategyOption("DC1", "2");
		   	  keyspaceDef.setStrategyOption("DC2", "2");*/
		   	  //end 正式环境
		    	 return keyspaceDef;    	 
		     }
		     
		     /**
		      * 生成ColumnFamily定义
		      * @param id
		      * @param columnFamilyName
		      * @return
		      */
		     private static BasicColumnFamilyDefinition getColumnFamilyDef(int id,String columnFamilyName,String keyspaceName,boolean ifSuperColumn,int keyCacheSize,int rowCacheSize){
		    	  BasicColumnFamilyDefinition cfDef1=new BasicColumnFamilyDefinition();
		    	  cfDef1.setId(id);//若为新增CF的操作，则id无需指定。若为更新操作，则需要指定ID
		    	  cfDef1.setName(columnFamilyName);
		    	  cfDef1.setKeyspaceName(keyspaceName);
		    	  cfDef1.setComparatorType(ComparatorType.UTF8TYPE);
		    	 
		    	  cfDef1.setDefaultValidationClass("UTF8Type");
		    	  if(ifSuperColumn){
		    		  cfDef1.setSubComparatorType(ComparatorType.UTF8TYPE);
			    	    cfDef1.setColumnType(ColumnType.SUPER);
		    	  }
		    	  //以下参数更具需要设定
		    	//  cfDef1.setGcGraceSeconds(gcGraceSeconds);
		    	  cfDef1.setKeyCacheSize(keyCacheSize);
		    	  cfDef1.setRowCacheSize(rowCacheSize);
		    	// cfDef1.setMaxCompactionThreshold(maxCompactionThreshold);
		    	 cfDef1.setMemtableFlushAfterMins(3);
		    	  cfDef1.setMemtableOperationsInMillions(0.01);
		    	  cfDef1.setMemtableThroughputInMb(16);
		    	//  cfDef1.setMinCompactionThreshold(minCompactionThreshold);
		    	//  cfDef1.setReadRepairChance(readRepairChance);
		    	//  cfDef1.setRowCacheSavePeriodInSeconds(rowCacheSavePeriodInSeconds);
		    	
		    	return cfDef1;      	  
		     }
	     
}
