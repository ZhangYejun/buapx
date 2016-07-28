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

<%
	String contextpath = request.getContextPath();
	String baseUrl = contextpath;
	String listUrl = baseUrl + "/login";
	
	String  tgc = request.getParameter("tgcvalue");
%>

<body  class="login_bg" >
     
	<PARAM NAME="_Version" VALUE="65536">
	<PARAM NAME="_ExtentX" VALUE="2646">
	<PARAM NAME="_ExtentY" VALUE="1323">
	<PARAM NAME="_StockProps" VALUE="0">
</OBJECT>

 <form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">			
            <input type="hidden" name="tgc"  id="tgc" value="" />
			<input type="hidden" name="lt" value="${loginTicket}" />
			<input type="hidden" name="execution" value="${flowExecutionKey}" />
			<input type="hidden" name="_eventId" value="submit" />
</form:form>

</body>
<script>
alert('tgc');
document.getElementById('tgc').value='<%=tgc%>';
alert('value='+document.getElementById('tgc').value);
document.forms[0].submit();	
			
			
 

</script>
