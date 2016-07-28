package com.baosight.buapx.cassandra.client.hector;

import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;

public class SimpleConsistencyLevelPolicy implements ConsistencyLevelPolicy {
	  private HConsistencyLevel readConsistencyLevel ;
	  private HConsistencyLevel writeConsistencyLevel ;
	  private HConsistencyLevel defaultReadConsistencyLevel = HConsistencyLevel.QUORUM;
	  private HConsistencyLevel defaultWriteConsistencyLevel = HConsistencyLevel.QUORUM;
	  
	  
	  public SimpleConsistencyLevelPolicy(){
		  
	  }
	  
	  
	  public SimpleConsistencyLevelPolicy(HConsistencyLevel readConsistencyLevel,HConsistencyLevel writeConsistencyLevel){
		  this.readConsistencyLevel=readConsistencyLevel;
		  this.writeConsistencyLevel=writeConsistencyLevel;
	  }
	  	 

	@Override
	public HConsistencyLevel get(OperationType op) {
		if(op.equals(OperationType.READ)||op.equals(OperationType.READ)){
			return readConsistencyLevel==null ? defaultReadConsistencyLevel: readConsistencyLevel;
		}else{
			return readConsistencyLevel==null ? defaultWriteConsistencyLevel: writeConsistencyLevel;
		}
		
	}

	@Override
	public HConsistencyLevel get(OperationType op, String cfName) {
		if(op.equals(OperationType.READ)||op.equals(OperationType.READ)){
			return readConsistencyLevel==null ? defaultReadConsistencyLevel: readConsistencyLevel;
		}else{
			return readConsistencyLevel==null ? defaultWriteConsistencyLevel: writeConsistencyLevel;
		}
	}
	
	 
	/**
	 * @return the readConsistencyLevel
	 */
	public HConsistencyLevel getReadConsistencyLevel() {
		return readConsistencyLevel;
	}


	/**
	 * @param readConsistencyLevel the readConsistencyLevel to set
	 */
	public void setReadConsistencyLevel(HConsistencyLevel readConsistencyLevel) {
		this.readConsistencyLevel = readConsistencyLevel;
	}


	/**
	 * @return the writeConsistencyLevel
	 */
	public HConsistencyLevel getWriteConsistencyLevel() {
		return writeConsistencyLevel;
	}


	/**
	 * @param writeConsistencyLevel the writeConsistencyLevel to set
	 */
	public void setWriteConsistencyLevel(HConsistencyLevel writeConsistencyLevel) {
		this.writeConsistencyLevel = writeConsistencyLevel;
	}


	/**
	 * @return the defaultReadConsistencyLevel
	 */
	public HConsistencyLevel getDefaultReadConsistencyLevel() {
		return defaultReadConsistencyLevel;
	}


	/**
	 * @param defaultReadConsistencyLevel the defaultReadConsistencyLevel to set
	 */
	public void setDefaultReadConsistencyLevel(
			HConsistencyLevel defaultReadConsistencyLevel) {
		this.defaultReadConsistencyLevel = defaultReadConsistencyLevel;
	}


	/**
	 * @return the defaultWriteConsistencyLevel
	 */
	public HConsistencyLevel getDefaultWriteConsistencyLevel() {
		return defaultWriteConsistencyLevel;
	}


	/**
	 * @param defaultWriteConsistencyLevel the defaultWriteConsistencyLevel to set
	 */
	public void setDefaultWriteConsistencyLevel(
			HConsistencyLevel defaultWriteConsistencyLevel) {
		this.defaultWriteConsistencyLevel = defaultWriteConsistencyLevel;
	}

}
