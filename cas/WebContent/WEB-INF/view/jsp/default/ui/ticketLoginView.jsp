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
.errors {
	
	font-size: 12px;
	color: red;

}
</style>


<%
  String  sTicket = request.getParameter("sTicket");
%>



<body  class="login_bg">
<table border="0" cellspacing="0" cellpadding="0" class="login_box">  
 <form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
 <tr>
  <td colspan="2">您正在自动跳转至  ${redirectUrl}</td>
 </tr>
 <tr>
  <td colspan="2" >&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   <form:errors path="*" cssClass="errors" id="status" element="span" />
  </td>
  </tr>
    <tr>
    <td width="65" >
         <label for="username" class="usernamefldtxt">
            <spring:message code="screen.welcome.label.netid" />
		</label>
	</td>
    <td>      
	  <c:if test="${empty sessionScope.openIdLocalId}">
													<spring:message code="screen.welcome.label.netid.accesskey"
														var="userNameAccessKey" />
													<form:input cssClass="usernamefldinput" cssErrorClass="error"
														id="userName"  tabindex="1"
														accesskey="${userNameAccessKey}" path="userName"
														autocomplete="false" htmlEscape="true" />
	</c:if>
   </td>
  </tr>
  <tr>
    <td>
     <label for="password" class="usernamefldtxt"><spring:message code="screen.welcome.label.password" /></label>
   </td>
    <td>
    <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
	<form:input    cssClass="usernamefldinput" cssErrorClass="error" id="ticket" tabindex="2" path="ticket" accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
    </td>
  </tr>
  
  <tr>
    <td colspan="2">
    <div class="bottonfld">
    <input  type="submit" class="loginbtn1"  value="登录" /> 
    <input   class="loginbtn2"  type="reset" value="重置"/>
    </div>
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
window.onload=function(){   
	var loginType='${loginType}';
     if(loginType=='ticketLogin'&&document.getElementById('status')==null){
      
     			  document.getElementById("ticket").value='<%=sTicket%>';
                  document.getElementById("userName").value='${sUsername}';
                  
                 document.forms[0].submit();
    }
}
</script>
