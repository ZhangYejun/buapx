package com.baosight.buapx.cassandra.client.domain;

import me.prettyprint.cassandra.connection.LoadBalancingPolicy;
import me.prettyprint.cassandra.service.ExhaustedPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;

public class ClientConfig {
	private String[] address={"localhost:9160"};
	private String username="lzd";
	private String password="1988";
	private String clusterName="Test Cluster";	
	private String keyspaceName="buapx";	
	private ExhaustedPolicy exhaustedPolicy=ExhaustedPolicy.WHEN_EXHAUSTED_BLOCK;
	private int maxActive=50;
	private long maxWaitTimeWhenExhausted=1000;
	private HConsistencyLevel readConsistencyLevelPolicy=HConsistencyLevel.ONE;
	private HConsistencyLevel writeConsistencyLevelPolicy=HConsistencyLevel.ONE;
	
	/**
	 * @return the readConsistencyLevelPolicy
	 */
	public HConsistencyLevel getReadConsistencyLevelPolicy() {
		return readConsistencyLevelPolicy;
	}
	/**
	 * @param readConsistencyLevelPolicy the readConsistencyLevelPolicy to set
	 */
	public void setReadConsistencyLevelPolicy(
			HConsistencyLevel readConsistencyLevelPolicy) {
		this.readConsistencyLevelPolicy = readConsistencyLevelPolicy;
	}
	/**
	 * @return the writeConsistencyLevelPolicy
	 */
	public HConsistencyLevel getWriteConsistencyLevelPolicy() {
		return writeConsistencyLevelPolicy;
	}
	/**
	 * @param writeConsistencyLevelPolicy the writeConsistencyLevelPolicy to set
	 */
	public void setWriteConsistencyLevelPolicy(
			HConsistencyLevel writeConsistencyLevelPolicy) {
		this.writeConsistencyLevelPolicy = writeConsistencyLevelPolicy;
	}
	/**
	 * @return the keyspaceName
	 */
	public String getKeyspaceName() {
		return keyspaceName;
	}
	/**
	 * @param keyspaceName the keyspaceName to set
	 */
	public void setKeyspaceName(String keyspaceName) {
		this.keyspaceName = keyspaceName;
	}
	
	
	/**
	 * @return the exhaustedPolicy
	 */
	public ExhaustedPolicy getExhaustedPolicy() {
		return exhaustedPolicy;
	}
	/**
	 * @param exhaustedPolicy the exhaustedPolicy to set
	 */
	public void setExhaustedPolicy(ExhaustedPolicy exhaustedPolicy) {
		this.exhaustedPolicy = exhaustedPolicy;
	}


	/**
	 * @return the maxWaitTimeWhenExhausted
	 */
	public long getMaxWaitTimeWhenExhausted() {
		return maxWaitTimeWhenExhausted;
	}
	/**
	 * @param maxWaitTimeWhenExhausted the maxWaitTimeWhenExhausted to set
	 */
	public void setMaxWaitTimeWhenExhausted(long maxWaitTimeWhenExhausted) {
		this.maxWaitTimeWhenExhausted = maxWaitTimeWhenExhausted;
	}
	/**
	 * @return the maxActive
	 */
	public int getMaxActive() {
		return maxActive;
	}
	/**
	 * @param maxActive the maxActive to set
	 */
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	/**
	 * @return the address
	 */
	public String[] getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String[] address) {
		this.address = address;
	}
	/**
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}
	/**
	 * @param clusterName the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
