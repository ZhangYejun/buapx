 
package com.baosight.buapx.sso;

import com.baosight.buapx.common.TDESUtil;
 

 
public class TDESLoginTicketAnalyser implements ILoginTicketAnalyser {
	private String splitStr;  //分隔符
	private String safeCode;  //安全代码
	private String key;       //私钥
	private long expireInMills; //过期时间
	public TDESLoginTicketAnalyser() {
	}



	private TicketDefinition decode(String ticket) {
		TicketDefinition ticketDef = new TicketDefinition();
		String plain = TDESUtil.decrypt(key, ticket);
		String plainArray[] = plain.split(splitStr);
		if (plainArray.length != 3) {
			throw new RuntimeException("ticket invalid exception");
		} else {
			ticketDef.setRawTicket(ticket);
			ticketDef.setTimestamp(Long.parseLong(plainArray[2]));
			ticketDef.setSafeCode(plainArray[1]);
			ticketDef.setUsername(plainArray[0]);
			return ticketDef;
		}
	}

	public String encode(String username) {
		String plain = (new StringBuilder(String.valueOf(username))).append(
				splitStr).append(safeCode).append(splitStr).append(
				System.currentTimeMillis()).toString();
		return TDESUtil.encrypt(key, plain);
	}

	private boolean validate(TicketDefinition ticket, String username) {
		boolean result = false;
		try {
			result = ticket.getUsername().equals(username)
					&& safeCode.equals(ticket.getSafeCode());
			long timedifference = System.currentTimeMillis() - ticket.getTimestamp();
			result = result
					&& (Math.abs(timedifference)< expireInMills);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean validate(String ticket, String username) {
		try {
			TicketDefinition ticketDef = decode(ticket);
			return validate(ticketDef, username);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String args[]) {
		TDESLoginTicketAnalyser analyser = new TDESLoginTicketAnalyser();
		analyser.setKey("37d5aed075525d4fa0fe635231cba230");
		analyser.setSafeCode("buapx");
		analyser.setSplitStr("-_-");
		analyser.setExpireInMills(10000L);
		String ticket = analyser.encode("admin");
		System.out.println(ticket);
		TicketDefinition ticketDefinition = analyser.decode(ticket);
		System.out.println("TicketDefinition:"+ticketDefinition.toString());
		String checkticket ="3d7e046826e4bab81f82579e901ea445ccc2a2d936ae183459242223ab12b25464dad4f33d81a3f9";
		//checkticket= ticket;
		System.out.println(analyser.validate(checkticket, "admin123"));
	 	
		 
	}
	
	
	public String getSplitStr() {
		return splitStr;
	}

	public void setSplitStr(String splitStr) {
		this.splitStr = splitStr;
	}

	public String getSafeCode() {
		return safeCode;
	}

	public void setSafeCode(String safeCode) {
		this.safeCode = safeCode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getExpireInMills() {
		return expireInMills;
	}

	public void setExpireInMills(long expireInMills) {
		this.expireInMills = expireInMills;
	}

	
}