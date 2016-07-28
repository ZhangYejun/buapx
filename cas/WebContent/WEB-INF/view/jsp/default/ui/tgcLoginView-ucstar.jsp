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
     <object id="UcSTARApi" classid="clsid:2532DED7-AB04-4D70-9D0C-1FB71F13323F"  width="0"  height="0" title=""></object>
<OBJECT id="UcStar" classid=clsid:2532DED7-AB04-4D70-9D0C-1FB71F13323F>
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
var stats=UcSTARApi.GetUcPresence();
if(stats=='1'){
		var token=UcSTARApi.getToken;
		alert(token);
		if(token!='NULL'){
			document.getElementById('tgc').value=token;
			document.forms[0].submit();	
		}else{
			alert('获取UC登录状态失败,请通过用户名密码登录');
			var queryString=window.location.search;
			var newQueryString='';
			if(queryString.indexOf('\?loginType=tgcLogin')!=-1){
				if(queryString.indexOf('\?loginType=tgcLogin&')!=-1){
					newQueryString=queryString.replace('\?loginType=tgcLogin&','\?');
				}else{
					newQueryString=queryString.replace('\?loginType=tgcLogin','');
					}	
			}else{
				 newQueryString=queryString.replace('&loginType=tgcLogin','');
				}

			var listUrl='<%=listUrl%>';
			window.location.href=listUrl+newQueryString;
			}
	}else{
		UcSTARApi.CloseUc(1);
		alert('获取UC登录状态失败,请通过用户名密码登录');
		var queryString=window.location.search;
		var newQueryString='';
		if(queryString.indexOf('\?loginType=tgcLogin')!=-1){
			if(queryString.indexOf('\?loginType=tgcLogin&')!=-1){
				newQueryString=queryString.replace('\?loginType=tgcLogin&','\?');
			}else{
				newQueryString=queryString.replace('\?loginType=tgcLogin','');
				}	
		}else{
			 newQueryString=queryString.replace('&loginType=tgcLogin','');
			}

		var listUrl='<%=listUrl%>';
		window.location.href=listUrl+newQueryString;
	}

//alert('tgc');
//document.getElementById('tgc').value='<%=tgc%>';
//alert('value='+document.getElementById('tgc').value);
//document.forms[0].submit();	

</script>
