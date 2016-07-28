package com.baosight.buapx.mongo.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

/**
 * 获取mongoDB中的mongo实例
 * @author huangxin
 *
 */
public class MongoDBFactory {
	private static final Logger logger = Logger.getLogger(MongoDBFactory.class);
	
	private static MongoDBFactory mongoDBFactory = null;
	private String host = "";
    private Integer port = 27017;
    private Integer connectionsPerHost= 10;
    private Integer connectTimeout= 2000;
    private Integer maxWaitTime = 2000;
    
	private List<ServerAddress> replicaSetSeeds = new ArrayList<ServerAddress>();
	
	
	public static MongoDBFactory getInstance(){
		if(mongoDBFactory == null){
			synchronized(MongoDBFactory.class){
				if(mongoDBFactory == null){
					mongoDBFactory = new MongoDBFactory();
				}
			}
		}
		return mongoDBFactory;
	}
	
	/**
	 * 获取mongo实例
	 * @return
	 */
	public Mongo getMongo(){
		try{
			Mongo mongo = null;
			MongoOptions mongooption  = new MongoOptions();
			mongooption.setAutoConnectRetry(true);
			mongooption.setMaxWaitTime(this.maxWaitTime);
			mongooption.setConnectionsPerHost(this.connectionsPerHost);
			mongooption.setConnectTimeout(this.connectTimeout);
			
			if(replicaSetSeeds.isEmpty()){
				ServerAddress defaultOptions = new ServerAddress(getHost(),getPort());
				replicaSetSeeds.add(defaultOptions);
			}
				mongo = new  Mongo(replicaSetSeeds,mongooption);
				mongo.setReadPreference(ReadPreference.SECONDARY);
			return mongo;
		}catch(Exception ex){
			logger.error("获取mongoDB连接失败！",ex.getCause());
			ex.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 通过设置MongoOptions获取mongo实例
	 * @param mongooptions
	 * @return
	 */
	public Mongo getMongo(MongoOptions mongooptions){
		if(mongooptions ==  null){
			return getMongo();
		}
		try{
			Mongo mongo = null;
			if(replicaSetSeeds.isEmpty()){
				ServerAddress defaultOptions = new ServerAddress(getHost(),getPort());
				replicaSetSeeds.add(defaultOptions);
			}
			    mongooptions.setMaxWaitTime(this.maxWaitTime);
			    mongooptions.setConnectionsPerHost(this.connectionsPerHost);
			    mongooptions.setConnectTimeout(this.connectTimeout);
				mongo = new  Mongo(replicaSetSeeds,mongooptions);
				mongo.setReadPreference(ReadPreference.SECONDARY);
				return mongo;
		}catch(Exception ex){
			logger.error("获取mongoDB连接失败！",ex.getCause());
			ex.printStackTrace();
			return null;
		}
	}
	
	public String getHost() {
		return host;
	}

    /**
     * 设置mongoDB服务ip地址
     */
	public void setHost(String host) {
		this.host = host;
	}


	public Integer getPort() {
		return port;
	}

	/**
	 * 设置mongoDB服务端口
	 */
	public void setPort(Integer port) {
		this.port = port;
	}


	public List<ServerAddress> getReplicaSetSeeds() {
		return replicaSetSeeds;
	}

    /**
     * 设置mongoDB集群服务
     */
	public void setReplicaSetSeeds(List<ServerAddress> replicaSetSeeds) {
		this.replicaSetSeeds = replicaSetSeeds;
	}

	public Integer getConnectionsPerHost() {
		return connectionsPerHost;
	}

	public void setConnectionsPerHost(Integer connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(Integer maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
	
	
	
	
} 
