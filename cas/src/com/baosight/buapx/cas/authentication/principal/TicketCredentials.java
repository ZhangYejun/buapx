package com.baosight.buapx.cas.authentication.principal;



import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.principal.Credentials;

public class TicketCredentials implements Credentials {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9072743979259311085L;
	
	
    private String ticket;
    private String userName;
    
    
    



	public String getTicket() {
		return ticket;
	}






	public void setTicket(String ticket) {
		if(!StringUtils.isEmpty(ticket)){
			if(ticket.charAt(ticket.length()-1)==','){
				ticket=ticket.substring(0, ticket.length()-1);
			}
		}
		this.ticket = ticket;
	}






	public String getUserName() {
		return userName;
	}






	public void setUserName(String userName) {		
		this.userName = userName;
	}






	@Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketCredentials that = (TicketCredentials) o;

        if (ticket != null ? !ticket.equals(that.ticket) : that.ticket != null) return false;
        return true;
    }



}
