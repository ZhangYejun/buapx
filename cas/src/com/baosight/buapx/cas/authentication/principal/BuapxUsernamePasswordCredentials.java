package com.baosight.buapx.cas.authentication.principal;

import org.jasig.cas.authentication.principal.RememberMeCredentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

public class BuapxUsernamePasswordCredentials extends UsernamePasswordCredentials implements RememberMeCredentials {

	/** Unique ID for serialization. */
	private static final long serialVersionUID = -8343864967200862794L;

	/** The validatecode. */
	// @NotNull
	// @Size(min=1, message = "required.validatecode")
	private String validatecode;

	private boolean isPlain=true;

	private boolean rememberMe=false;

	private String userType;

	/**
	 * Gets the validatecode.
	 *
	 * @return the validatecode
	 */
	public final String getValidatecode() {
		return validatecode;
	}

	/**
	 * Sets the validatecode.
	 *
	 * @param validatecode
	 *            the new validatecode
	 */
	public final void setValidatecode(final String validatecode) {
		this.validatecode = validatecode;
	}



	public boolean isPlain() {
		return isPlain;
	}

	public void setPlain(boolean isPlain) {
		this.isPlain = isPlain;
	}



	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BuapxUsernamePasswordCredentials that = (BuapxUsernamePasswordCredentials) o;
		String username = super.getUsername();
		String password = super.getPassword();

		if (password != null ? !password.equals(that.getPassword()) : that.getPassword() != null)
			return false;
		if (username != null ? !username.equals(that.getUsername()) : that.getUsername() != null)
			return false;
		if (userType != null ? !userType.equals(that.getUserType()) : that.getUserType() != null)
			return false;
		// if (validatecode != null ? !validatecode.equals(that.validatecode) :
		// that.validatecode != null) return false;
		return true;
	}


	@Override
	public int hashCode() {
		String username = super.getUsername();
		String password = super.getPassword();
		int result = username != null ? username.hashCode() : 0;
		// result = 31 * result +validatecode != null ? validatecode.hashCode():0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}

	@Override
	public boolean isRememberMe() {
		return this.rememberMe;
	}

	@Override
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe=rememberMe;
	}
}
