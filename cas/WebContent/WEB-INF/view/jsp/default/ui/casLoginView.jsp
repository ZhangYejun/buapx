<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script src="js/jquery.min.js" type="text/javascript"></script>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="renderer" content="ie-stand"> 
<title>统一认证平台</title>
<link type="text/css" rel="stylesheet" href="css/ouyeel.css" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.jslides.js"></script>

<%//页面加载时，读取用户名密码
	String userName="";
	String passWord="";
	try{
		Cookie[] cookies=request.getCookies();
		for(int i=0;i<cookies.length;i++){
			String name=cookies[i].getName();
			if(name.equals("lastUser")){
				userName=cookies[i].getValue();
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
%>
<script language="JavaScript" type="text/javascript">
window.onload =loadPage;
<%-- 页面加载器时给记住密码打钩 --%>
function loadPage(){
	var userName=document.getElementById("input_user").value;
	if(userName!=null && userName!=""){
		document.getElementById("isRemember").setAttribute("checked",true); 
	}
}

<%-- 记住密码 --%>
function rememberPass(){
	if($("#isRemember").attr("checked")=="checked"){
		var userName=document.getElementById("input_user").value;
		if(userName!=null && userName!=""){
			delCookie("lastUser");
			SetCookie("lastUser",userName);
		}
	}else{
		var userName=document.getElementById("input_user").value;
		if(userName!=null && userName!=""){
			delCookie("lastUser");
		}
	}
}

<%-- 清除用户名密码并取消打钩 --%>
function clearPass(){
	document.getElementById("input_user").value="";
	document.getElementById("input_pw").value="";
	document.getElementById("isRemember").checked=false;
	delCookie("lastUser");
}

function SetCookie(name, value) {
	var Days = 30;
	var exp = new Date(); 
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie=name+"="+value+";expires="+exp.toGMTString();
}

function delCookie(name) { 
    var exp = new Date(); 
    exp.setTime(exp.getTime() - 1); 
    var cval=getCookie(name); 
    if(cval!=null) 
        document.cookie=name+"="+cval+";expires="+exp.toGMTString(); 
} 

function getCookie(name) { 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]); 
    else 
        return null; 
} 
</script>

</head>
<body onload="document.getElementById('input_pw').focus()">
	<div id="log_info">
  		<div class="ouyeelf"></div>
  		<div class="hotline"></div>
	</div>
	<div class="logo">
		<!-- <img src="images/login_logo.png" width="160" height="42" alt=""> -->
		<img src="images/login_logo2.png" width="148" height="38" alt="">
	</div>
	<div id="login">
		<div class="link" >
			<div id="slidebox">
				<img src="images/login_img.jpg" />
			</div>
		</div>
		<form:form method="post" commandName="${commandName}"
			htmlEscape="true" class="login_con" name="form1" id="ind">
			<ul>
				<li class="ent_tit"><a href="#" class="on" style=" height: 40px; float: left; line-height: 40px;">用户登录</a></li>
			</ul>
			<table width="80%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td style="padding: 0px" align="center"><span class="input_wrong"><form:errors
								path="*" element="font" />&nbsp</span></td>
				</tr>
				<tr>
					<td><form:input cssClass="input_login ie7float" 
							id="input_user" tabindex="1" accesskey="${userNameAccessKey}"
							path="username" autocomplete="false" htmlEscape="true"
							value="<%=userName%>" placeholder="用户名"/></td>
				</tr>
				<tr>
					<td><form:password cssClass="input_login ie7float"
							id="input_pw" tabindex="2" accesskey="${userNameAccessKey}"
							path="password" autocomplete="false" htmlEscape="true"
							value=""  placeholder="密码"/></td>
				</tr>
				
				<tr>
					<td>
						<div style="float:left; width:20px; display:block;  height:20px; padding-top:6px;   ">
						<input type="checkbox" id="isRemember" name="isRemember" checked="checked" style="height: 13px;color: black;" tabindex="3"/>
						</div>
    				  <div style="float:left;width:120px; display:block;height:20px; line-height:20px;">
						&nbsp;记住用户名
						</div>  
					</td>
				</tr>
				
				<tr>
					<td><input type="submit" name="button" id="button" value="登录" tabindex="4"
						class="btn_b blue_bg" style="border: none"  onclick="rememberPass()"/></td>
				</tr>
			</table>
			<input type="hidden" name="userType" value="PERSONAL" />
			<input type="hidden" name="lt" value="${loginTicket}" />
			<input type="hidden" name="execution" value="${flowExecutionKey}" />
			<input type="hidden" name="_eventId" value="submit" />
			<div id="go2register">
				<!-- <a href="http://190.10.10.85:8080/buapx/BUAPX/UA/SY/userLabel.jsp">忘记密码？</a> -->
				(初始密码为身份证后8位或者工号)

			</div>
		</form:form>
	</div>
	<div class="foot">
		<br>统一认证平台 版权所有Copyright © 2013-2016
	</div>
	<img src="images/login_bg.jpg" class="login_bg"/>
</body>
</html>