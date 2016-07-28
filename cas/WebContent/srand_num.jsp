<%@page language="java" pageEncoding="utf-8" import="ocx.GetRandom,net.sf.json.JSONObject;"%><%
 String  mcrypt_key=GetRandom.generateString(32);
 //String  mcrypt_key="gx8j0xxacjnjh5ehxkppxnyzlxvaxxma";
 //String  mcrypt_key=new String(mcrypt_key1.getBytes("ISO-8859-1"),"UTF-8");
 session.setAttribute("mcrypt_key",mcrypt_key);
 JSONObject jsonObject = new JSONObject();  
 jsonObject.put("data",mcrypt_key) ;
 String callback =  request.getParameter("jsoncallback");
 out.print(callback+"("+jsonObject+");");
%>