<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page session="true"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${not empty cssName}">
<link type="text/css" rel="stylesheet" href="css/${cssName}.css" />
</c:if>

<c:if test="${empty cssName}">
<link type="text/css" rel="stylesheet" href="css/iplat4j.css" />
</c:if>
<script  type="text/javascript" id="jqueryJs">	
</script>

<script type="text/javascript"  id="caJs">	
</script>

<style type="text/css">
#forgetPwd{
	font-size:13px;
	}

.errors{
	font-size: 12px;
	color: red;
	}

</style>

<script type="text/javascript" >
function blurq(){
	var username=document.getElementById("username");
	var p_username = username.value;
	if(p_username==""){
		username.value = "工号/手机号码/邮箱地址";
		username.style.color = "#666";
		username.style.fontSize = "12px";
		username.style.fontWeight = "100";
		//#CFC8C8
	}
}

function focusq(){
	var username=document.getElementById("username");
	var p_username = username.value;
	if(p_username=="工号/手机号码/邮箱地址"){
		username.value = "";
		username.style.color = "black";
		username.style.fontSize = "16px";
		username.style.fontWeight = "bold";
	}
}
</script>




<body  class="login_bg" style="background-color:transparent;">
<table border="0" cellspacing="0" cellpadding="0" class="login_box">  
 <form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
 	<tr>
  		<td colspan="4">
  			<br id="error_br">&nbsp<form:errors path="*" cssClass="errors" id="status" element="div" />
  		</td>
  	</tr>
  	<tr>
    	<td  >
         <label for="username" class="usernamefldtxt">
            <spring:message code="screen.welcome.label.netid" />
		</label>
		</td>
    	<td>
      <c:if test="${not empty sessionScope.openIdLocalId}">
		 <strong>${sessionScope.openIdLocalId}</strong>
		 <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
	  </c:if>
	  <c:if test="${empty sessionScope.openIdLocalId}">
		<spring:message code="screen.welcome.label.netid.accesskey"
			var="userNameAccessKey" />
		<form:input cssClass="usernamefldinput" cssErrorClass="error"
			id="username"  tabindex="1"
			accesskey="${userNameAccessKey}" path="username"
			autocomplete="false" htmlEscape="true" value='${credentials.username}' />
	  </c:if>
   </td>
   <td>
     <label for="password" class="usernamefldtxt"><spring:message code="screen.welcome.label.password" /></label>
   </td>
   <td>
    <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
	<form:password    cssClass="usernamefldinput" cssErrorClass="error" id="password" tabindex="2" path="password" accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
   </td>
  </tr>

</table>
	<input id="useCert" name="useCert" type="hidden" value="false"/>
			<input type="hidden" name="lt" value="${loginTicket}" />
			<input type="hidden" name="execution" value="${flowExecutionKey}" />
			<input type="hidden" name="_eventId" value="submit" />
</form:form>
</body>

<script>
window.onload=function(){document.getElementById("username").focus();}
</script>

<%
 String cssName=request.getParameter("cssName");
if(cssName!=null&&!cssName.equals("")&&cssName.indexOf("-alert")!=-1){
%>
<script>
var status=document.getElementById("status");
if(status!=null&&status.innerHTML!=''){
		  alert(status.innerHTML);   	       
	  }
</script>
<%
}
%>
