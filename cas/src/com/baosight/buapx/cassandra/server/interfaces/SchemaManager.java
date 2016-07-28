package com.baosight.buapx.cassandra.server.interfaces;

import java.util.List;

import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;

public interface SchemaManager {
	
	   void addKeyspace(KeyspaceDefinition keyspaceDef);
	    
	   void updateKeyspace(KeyspaceDefinition keyspaceDef);
	    
	   void dropKeyspace(String keyspaceName);
	    
	   void addColumnFamily(ColumnFamilyDefinition cfDef,List<ColumnDefinition> colDefs);
	    
	   void updateColumnFamliy(ColumnFamilyDefinition cfDef,List<ColumnDefinition> colDefs);
	    
	   void dropColumnFamily(String keyspaceName,String columnFamily);
	   
	   KeyspaceDefinition describeKeyspace(String keyspaceName);
}
