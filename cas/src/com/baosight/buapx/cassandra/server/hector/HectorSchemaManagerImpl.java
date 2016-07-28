package com.baosight.buapx.cassandra.server.hector;


import java.util.List;

import com.baosight.buapx.cassandra.client.domain.ClientConfig;
import com.baosight.buapx.cassandra.client.hector.HectorClientConfigurator;
import com.baosight.buapx.cassandra.server.interfaces.SchemaManager;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.ThriftColumnDef;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;

public class HectorSchemaManagerImpl implements SchemaManager{
	private ClientConfig clientConfig;
	private Cluster cluster;
  
    public HectorSchemaManagerImpl(ClientConfig clientConfig){
    	this.clientConfig=clientConfig;
    	cluster =HectorClientConfigurator.getOrCreateCluster(this.clientConfig);
    }
       
    public void addKeyspace(KeyspaceDefinition keyspaceDef){
    	keyspaceDef=new ThriftKsDef(keyspaceDef);
    	cluster.addKeyspace(keyspaceDef);
    }
    
    public void updateKeyspace(KeyspaceDefinition keyspaceDef){
    	keyspaceDef=new ThriftKsDef(keyspaceDef);
    	cluster.updateKeyspace(keyspaceDef);
    }
    
    public void dropKeyspace(String keyspaceName){
    	cluster.dropKeyspace(keyspaceName);
    }
    
    public void addColumnFamily(ColumnFamilyDefinition cfDef,List<ColumnDefinition> colDefs){
    	if(colDefs!=null){
    		for(ColumnDefinition colDef:colDefs){
        		cfDef.addColumnDefinition(new ThriftColumnDef(colDef));
        	}
    	}
    	
    	cfDef=new ThriftCfDef(cfDef);
    	cluster.addColumnFamily(cfDef);
    }
    
    public void updateColumnFamliy(ColumnFamilyDefinition cfDef,List<ColumnDefinition> colDefs){
    	if(colDefs!=null){
    		for(ColumnDefinition colDef:colDefs){
        		cfDef.addColumnDefinition(new ThriftColumnDef(colDef));
        	}
    	}	
    	cfDef=new ThriftCfDef(cfDef);
    	cluster.updateColumnFamily(cfDef);
    }
    
   public void dropColumnFamily(String keyspaceName,String columnFamily){
	   cluster.dropColumnFamily(keyspaceName, columnFamily);
   }
   
    public KeyspaceDefinition describeKeyspace(String keyspaceName){
    	return cluster.describeKeyspace(keyspaceName);
    }
	
	
   
    	
}
