<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title logout</title>
</head>
<body>
退出功能演示
<%session.invalidate(); %>
<iframe src="http://www.cas.com:9080/cas/logout?loginType=mixLogin&redirectUrl=http://www.client.com:8080/cas_client_all/index.jsp" style="display: none">

</iframe>
 
</body>
</html>