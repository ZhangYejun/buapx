<%@page import="java.net.URLEncoder"%>
<%@page import="com.baosight.buapx.security.common.UrlConstructor"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<%
//感知是否已经登录，如果已经登录，直接跳转到首页
String login=request.getParameter("login");
if(login==null||login.equals("")){
	response.sendRedirect("index.jsp");
}
//取得从cas返回的service，如果直接输入login.jsp，则拼接service为首页
String service=request.getParameter("service");
if(service!=null){
	service=URLEncoder.encode(service, "UTF-8");
}else{
	service=UrlConstructor.constructServiceExternal("/index.jsp");
}
  %>
<body>
 	<form action="http://www.cas.com:9080/cas/remoteLogin?service=<%=service%>" method="post">
		<table>
		<tr>
		<td></td><td><%=request.getParameter("error") %></td>
		</tr>
			<tr>
				<td>用户名</td>
				<td><input type="text" name="username" tabindex="1"/></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><input type="password" name="password" tabindex="2"/></td>
			</tr>
			<tr>
			<td>验证码</td>
			<td><input type="text" class="wid" name="_captcha_parameter"
							 tabindex="3" style="width: 100px;" maxlength="4" />
							 <img id="captcha"
							 width="60" height="21"
							onclick="this.src = 'http://www.cas.com:9080/cas/getCaptchaImage?d='+new Date().getTime();"
							title="看不清,换一张" src='http://www.cas.com:9080/cas/getCaptchaImage?d='+new Date().getTime();
							style="cursor: pointer; position:relative;"/>
			</td>
			</tr>
			<tr>
				<td><input type="submit" value="登录" tabindex="4"/></td>
			</tr>
		</table>
		 <input type="text" name="usertype" value="PERSONAL"/>
		 <input type="hidden" name="action" value="submit"/>
		<input type="hidden" value="<%=java.net.URLEncoder.encode("http://www.client.com:8080/cas_client_all/login.jsp","UTF-8")%>" name="remoteLoginPage" />

	</form>
</body>
</html>