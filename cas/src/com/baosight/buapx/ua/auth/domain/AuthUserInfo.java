package com.baosight.buapx.ua.auth.domain;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthUserInfo {
	private String userid = null;
	private String password = null;
	private String displayName = " ";
	private String email = " ";
	private String workNumber = " ";
	private boolean useCert = false;
	private boolean hasActived = false;
	private String userType = " ";
	private String encVersion = " ";

	private Date expiryDate = new Date(System.currentTimeMillis() + 1000 * 3600
			* 24 * 90);
	private static String[] fieldsNames;

	static {
		Field[] fields = AuthUserInfo.class.getDeclaredFields();
		fieldsNames = new String[fields.length - 1];
		int i = 0;
		for (Field f : fields) {
			if (!f.getName().equals("fieldsNames")) {
				fieldsNames[i] = f.getName();
				i++;
			}

		}

	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}

	public boolean isUseCert() {
		return useCert;
	}

	public void setUseCert(boolean useCert) {
		this.useCert = useCert;
	}

	public boolean isHasActived() {
		return hasActived;
	}

	public void setHasActived(boolean hasActived) {
		this.hasActived = hasActived;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}



	public String getEncVersion() {
		return encVersion;
	}

	public void setEncVersion(String encVersion) {
		this.encVersion = encVersion;
	}

	public Map<String, String> toMap() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("userid", userid);
		m.put("password", password);
		m.put("displayName", displayName);
		m.put("email", email);
		m.put("workNumber", workNumber);

		m.put("useCert", useCert ? "true" : "false");
		m.put("hasActived", hasActived ? "true" : "false");
		m.put("userType", userType);
		m.put("encVersion", encVersion);

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		m.put("expiryDate", fmt.format(expiryDate));
		return m;
	}

	public AuthUserInfo fromMap(Map<String, String> m) throws ParseException {
		setUserid(m.get("userid"));
		setPassword(m.get("password"));
		setDisplayName(m.get("displayName"));
		setEmail(m.get("email"));
		setWorkNumber(m.get("workNumber"));
		setUserType(m.get("userType"));
		setEncVersion(m.get("encVersion"));

		if (m.get("useCert") != null) {
			setUseCert(m.get("useCert").equals("true") ? true : false);
		}
		if (m.get("hasActived") != null) {
			setHasActived(m.get("hasActived").equals("true") ? true : false);
		}
		if (m.get("expiryDate") != null) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			setExpiryDate(fmt.parse(m.get("expiryDate")));
		}
		return this;
	}

	public static enum Fields {
		USERID, PASSWORD, DISPLAY_NAME, EMAIL, WORK_NUMBER, USER_CERT, HAS_ACTIVED, USER_TYPE, EXPIRY_DATE,ENC_VERSION;
	}

	public static class Constants {

		public static final String USE_CERT_TRUE = "true";
		public static final String USE_CERT_FALSE = "false";
		public static final String HAS_ACTIVED_TRUE = "true";
		public static final String HAS_ACTIVED_FALSE = "false";

		public static final String PASSWORD_FIELD_NAME = "password";
		public static final String USERID_FIELD_NAME = "userid";
		public static final String EXPIRYDATE_FIELD_NAME = "expiryDate";
		public static final String HASACTIVED_FIELD_NAME = "hasActived";
		public static final String USERTYPE_FIELD_NAME = "userType";
		public static final String ENCVERSION_FIELD_NAME ="encVersion";

	}

	public static String[] getFieldsNames() {
		return fieldsNames;
	}

}
