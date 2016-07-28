<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:directive.include file="../../default/ui/includes/top.jsp" />
<script type="text/javascript" src="./js/buapxProtocol.js"></script>
<%-- --%>  
<a href="./buapxServiceValidate?ST=${ticket}&sysCode=${sysCode}" >Validate Service Ticket </a>

<div id="msg" class="success">
	<h2>
		临时票据发送成功
	</h2>
	<p>
		请等待客户端验证票据后登录
	</p>
</div>

<c:if test="${not empty cs}">
<script type="text/javascript">
	sendBuapxProtocol("${sysCode}", "${ticket}");
</script>
</c:if>


<c:if test="${empty cs}">
<script type="text/javascript">
	window.location.href="${sysUrl}";
</script>
</c:if>
<jsp:directive.include file="../../default/ui/includes/bottom.jsp" />