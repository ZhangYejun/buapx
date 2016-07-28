<%@ page session="false" contentType="text/plain" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<buapx:serviceResponse xmlns:buapx='http://www.baosight.com/buapx_cas'>
	<buapx:authenticationFailure code='${code}'>
		${fn:escapeXml(description)}
	</buapx:authenticationFailure>
</buapx:serviceResponse>