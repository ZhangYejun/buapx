package com.baosight.buapx.cassandra.client.hector;

import org.apache.cassandra.thrift.IndexOperator;

public class StringIndexExpression {
  private IndexOperator indexOperator;
  private String column;
  private String value;
  public StringIndexExpression(String column,IndexOperator indexOperator,String value){
	  this.indexOperator=indexOperator;
	  this.column=column;
	  this.value=value;
  }
  /**
 * @return the indexOperator
 */
public IndexOperator getIndexOperator() {
	return indexOperator;
}
/**
 * @return the column
 */
public String getColumn() {
	return column;
}
/**
 * @return the value
 */
public String getValue() {
	return value;
}

}
