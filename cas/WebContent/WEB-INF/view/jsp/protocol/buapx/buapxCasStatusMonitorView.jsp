<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<buapx:serviceResponse xmlns:buapx='http://www.baosight.com/buapx_cas'>
	<buapx:success>
		<buapx:ticketCount>${ticket_count}</buapx:ticketCount>
		<buapx:loginTimeCost>${time_cost_in_percentage}</buapx:loginTimeCost>
	</buapx:success>          
</buapx:serviceResponse>