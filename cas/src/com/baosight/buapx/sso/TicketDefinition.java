package com.baosight.buapx.sso;

public class TicketDefinition {
	private String rawTicket;
	private String username;
	private long timestamp;
	private String safeCode;

	public TicketDefinition() {
	}

	public String getRawTicket() {
		return rawTicket;
	}

	public void setRawTicket(String rawTicket) {
		this.rawTicket = rawTicket;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSafeCode() {
		return safeCode;
	}

	public void setSafeCode(String safeCode) {
		this.safeCode = safeCode;
	}
	
	
	public String toString(){
		return  "rawTicket="+rawTicket+"  username=" +username+" safeCode="+safeCode+" timestamp="+timestamp;
		 
	}

}