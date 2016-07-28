<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>单点登录</title>
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/remoteLogin.js"></script>

<script>
	var service = "<%=URLEncoder.encode("http://www.sina.com", "UTF-8")%>";

	function loginCallback(success, msg, user) {
		alert(success + "   " + msg + "    " + user);
		//alert(true);
	}

	function ssoCallback(success, msg, user) {
		alert(success + "   " + msg + "    " + user);
		//alert(false);
	}
	CasRemoteClient
	casClient = new CasRemoteClient("http://www.cas.com:9080/cas/jsLogin",
			loginCallback, ssoCallback, service);

	casClient.sso();
</script>
</head>
<body>
单点登录页
</body>
</html>