<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="../../default/ui/includes/top.jsp" />
<script type="text/javascript" src="./js/buapxProtocol.js"></script>
<%--  
<a href="./buapxServiceValidate?ST=${ticket}" >Validate Service Ticket </a>
--%>
<div id="status" class="errors">
	<h2>
		票据生成失败！
	</h2>
	<p>
		${description}
	</p>
</div>

<jsp:directive.include file="../../default/ui/includes/bottom.jsp" />