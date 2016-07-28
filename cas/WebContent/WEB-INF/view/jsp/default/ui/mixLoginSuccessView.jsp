<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.net.URLDecoder" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>


<c:if test="${not empty cssName}">
<link type="text/css" rel="stylesheet" href="css/${cssName}.css" />
</c:if>

<c:if test="${empty cssName}">
<link type="text/css" rel="stylesheet" href="css/iplat4j.css" />
</c:if>



</head>

<body  class="login_bg">
  <img alt="" src="./images/services/loading.gif" style="margin-left:70px;margin-top: 35px"> 
</body>
<%
String redirectUrl=request.getParameter("redirectUrl");
if(redirectUrl!=null&&redirectUrl.trim().length()!=0){
	redirectUrl=URLDecoder.decode(redirectUrl);
}
%>
<c:if test="${empty forceRedirectUrl}">
<script>
	   if(window.parent!=undefined){
		  window.parent.location.href='<%=redirectUrl%>';
		}else{
			window.location.href='<%=redirectUrl%>';
	   }

</script>
</c:if>

<c:if test="${not empty forceRedirectUrl}">
<script>
 window.onload=function(){
	if(window.parent!=undefined){		
		window.parent.location.href='${forceRedirectUrl}';
	}else{
		window.location.href='${forceRedirectUrl}';
	}
} 
</script>
</c:if>

</html>