package com.baosight.buapx.cas.authentication.principal;



import org.jasig.cas.authentication.principal.Credentials;

public class TgcCredentials implements Credentials {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9072743979259311085L;
	
	
    private String tgc;
    private String userName;
    
    
    
    public String getTgc() {
		return tgc;
	}



	public void setTgc(String tgc) {
		this.tgc = tgc;
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

        TgcCredentials that = (TgcCredentials) o;

        if (tgc != null ? !tgc.equals(that.tgc) : that.tgc != null) return false;
        return true;
    }



}
