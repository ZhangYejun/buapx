package com.baosight.buapx.cassandra.client.hector;

import java.util.HashMap;
import java.util.Map;

import com.baosight.buapx.cassandra.client.domain.ClientConfig;

import me.prettyprint.cassandra.connection.DynamicLoadBalancingPolicy;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

public class HectorClientConfigurator {
   
	  public static Cluster getOrCreateCluster(ClientConfig clientConfig) {		
		  Cluster cluster= HFactory.getCluster(clientConfig.getClusterName());
			if(cluster==null){
					CassandraHostConfigurator chc = new CassandraHostConfigurator();
					String hosts="";
					for(String host:clientConfig.getAddress()){			
							hosts=hosts+host+",";
					}
					hosts.substring(0, hosts.length()-1);
					chc.setHosts(hosts);
					chc.setUseThriftFramedTransport(true);   // new added
					chc.setRetryDownedHosts(true);  //new added  
					chc.setAutoDiscoverHosts(false);
					chc.setAutoDiscoveryDelayInSeconds(30);
					chc.setCassandraThriftSocketTimeout(5000); //临时
					chc.setLoadBalancingPolicy(new DynamicLoadBalancingPolicy()); //new added
					chc.setMaxActive(clientConfig.getMaxActive());
					chc.setMaxWaitTimeWhenExhausted(clientConfig.getMaxWaitTimeWhenExhausted());
					chc.setExhaustedPolicy(clientConfig.getExhaustedPolicy());
					Map map=new HashMap();
					map.put("username",clientConfig.getUsername());
					map.put("password", clientConfig.getPassword());
					//return HFactory.getOrCreateCluster(clientConfig.getClusterName(), chc,map);
					return HFactory.createCluster(clientConfig.getClusterName(), chc,map);

			}else{
				return cluster;
			}
		}	

	public static void shutDownConnectPool(String clusterName){
        
		Cluster cluster= HFactory.getCluster(clusterName);
		if(cluster!=null){
			cluster.getConnectionManager().shutdown();
		}
		 
	}
}
