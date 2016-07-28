<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<buapx:response xmlns:buapx='http://www.baosight.com/buapx_cas'>
	<buapx:user>${auth}</buapx:user>
	<buapx:failCause>${failCause}</buapx:failCause>
	<buapx:msg>${msg}</buapx:msg>
	<buapx:loginTicket>${loginTicket}</buapx:loginTicket>
</buapx:response>