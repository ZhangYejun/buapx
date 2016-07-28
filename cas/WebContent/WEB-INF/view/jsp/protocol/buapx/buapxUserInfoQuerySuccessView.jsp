<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<buapx:serviceResponse xmlns:buapx='http://www.baosight.com/buapx_cas'>
	<buapx:authenticationSuccess>
		<buapx:user>${user}</buapx:user>
	</buapx:authenticationSuccess>
</buapx:serviceResponse>