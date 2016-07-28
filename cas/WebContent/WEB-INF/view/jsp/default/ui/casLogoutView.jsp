<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>
<jsp:directive.include file="includes/top.jsp" />
<div id="center">
	<div id="msg" class="success">
		<h2>
			<spring:message code="screen.logout.header" />
		</h2>
		<p>您已经成功退出统一认证系统</p>
		<p>
			<spring:message code="screen.logout.security" />
		</p>
	</div>
</div>





 <jsp:directive.include file="includes/bottom.jsp" />

    <c:if test="${loginType eq 'iframeLogin'}">
	<c:redirect url="/login?loginType=iframeLogin"></c:redirect>
</c:if>
<c:if test="${loginType eq 'mixLogin'}">
	<script>
	if(window.top!=undefined){
		window.top.location.href='${redirectUrl}';
	}else{
		if(window.parent!=undefined){
			window.parent.location.href='${redirectUrl}';
			}else{
				window.location.href='${redirectUrl}';
		}
		
	}


</script>
</c:if>
</html>

