<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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

<script>
 window.onload=function(){
	window.location.href='${redirectUrl}';
} 

</script>
</html>