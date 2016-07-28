<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<script src="js/jquery.min.js" type="text/javascript"></script>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<%
Exception exp = (Exception)request.getAttribute("AuthenticationException");
String user = (String)request.getAttribute("AuthenticationUser");
String username = "";
if ( exp != null ){
	username = user;
}
String clo = "color:black;height:18px;line-height:18px;width:180px;";
if(username == ""){
	username = "工号/手机号码/邮箱地址";
	clo = "color:#666;font-size:12px;font-weight:100;height:18px;line-height:18px;width:180px;";
}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>宝钢统一认证平台登录</title>


<c:if test="${not empty cssName}">
<link type="text/css" rel="stylesheet" href="css/${cssName}.css" />
</c:if>

<c:if test="${empty cssName}">
<link type="text/css" rel="stylesheet" href="css/login2.css" />
</c:if>


<script type="text/javascript" id="jqueryJs">

</script>

<script type="text/javascript" id="caJs">
function blurq(){
	var username=document.getElementById("username");
	var p_username = username.value;
	if(p_username==""){
		username.value = "工号/手机号码/邮箱地址";
		username.style.color = "#666";
		username.style.fontSize = "12px";
		username.style.fontWeight = "100";
		//#CFC8C8
	}
}

function focusq(){
	var username=document.getElementById("username");
	var p_username = username.value;
	if(p_username=="工号/手机号码/邮箱地址"){
		username.value = "";
		username.style.color = "black";
		username.style.fontSize = "16px";
		username.style.fontWeight = "bold";
	}
}


function checkNum(value){
	if(value.length==4){
		$.ajax({
		    url: "checkCaptcha",
		    data: {num:value},
		    type:"get",
		    cache: false,
		    success: function (data) {
		    	if(data=="true"){
		    		$('#nike').css('display','');
		    	}
		    	else{
		    		$('#nike').css('display','none');
		    	}
		    },
		    error: function (jqXHR, textStatus, errorThrown) {
		        alert(errorThrown);
		    }
		});
		}

}
</script>

</head>

<body>
	<div id="container">
		<!--头部-->
		<div class="g-header">
			<!--logo -->
			<div class="g-header-left">
				<img src="images/logo02.png" width="350" height="45" />
			</div>
		</div>
	</div>
	<div class="menu_item_blue">
		<div class="menu_inner"></div>
	</div>

	<div id="mainContent02">
		<div class="loginbg">
			<div class="loginbg_left">
				<form:form method="post" commandName="${commandName}"
						htmlEscape="true">
					<ul>
						<li class="wrong" id="errorMsg">
						&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
						&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
						<form:errors path="*" element="font" />
						<li>用户名：&nbsp;&nbsp;&nbsp;<form:input cssClass="required" class="wid"
									id="username" name="username" tabindex="1"
									accesskey="${userNameAccessKey}" path="username"
									autocomplete="false" htmlEscape="true" onblur="blurq()" onfocus="focusq();" value="<%=username %>"
									 />
						</li>
						<div id="ppcard" style="position: absolute; padding-left: 0px">
						</div>
						<li>密 码：&nbsp;&nbsp;&nbsp;
							 <form:password cssClass="required"
									cssErrorClass="error" id="password" class="wid" tabindex="1"
									style="width: 180px"
									path="password" accesskey="${passwordAccessKey}"
									htmlEscape="true" autocomplete="off" />
						   <a id="forgetPwd" href="https://bca.baogang.info/buap/buap/getPassword.html"  target="_blank">忘记密码?</a>
						</li>
						<li>验证码：&nbsp; <input type="text" class="wid" name="_captcha_parameter" onkeyup="checkNum(this.value)"
							 tabindex="1" style="width: 100px;" maxlength="4"
							 onfocus="document.getElementById('captcha').style.display='inline';document.getElementById('notClear').style.display='inline';" />
							 <img id="captcha"
							 width="60" height="21"
							onclick="this.src = 'getCaptchaImage?d='+new Date().getTime();"
							title="看不清,换一张" src="getCaptchaImage?d='+new Date().getTime()"
							style="cursor: pointer; position:relative;display: none;"/>
							<a href="javascript:;" id="notClear" style="display: none;" onclick="document.getElementById('captcha').src='getCaptchaImage?d='+new Date().getTime();"  class="pw">&nbsp;&nbsp;&nbsp;看不清图片</a></span>
						    <img
							id="nike" width="60" height="21"
							src="images/services/nike.jpg"
							style="cursor: pointer; position: absolute; display: none;"/>
						</li>

						<li>

							<div class="btn2" align="center">
								<input type="submit" class="btn_submit" value="登&nbsp;&nbsp;录"
									style="margin-top: px;" />
							</div></li>

					</ul>
					        <input type="hidden" name="userType" value="COMPANY"/>
							<input type="hidden" name="lt" value="${loginTicket}" />
							<input type="hidden" name="execution" value="${flowExecutionKey}" />
							<input type="hidden" name="_eventId" value="submit" />
				</form:form>
			</div>

			<div class="loginbg_right">
				<div class="marquees">
<!--					<ul>-->
<!--						<li><a href="#">1.中煤统一认证平台试运行公告 </a><img-->
<!--							src="images/gif-0313.gif" width="22" height="9" />-->
<!--						</li>-->
<!--						<li><a href="#"> 2.如何找回遗忘的统一认证密码？</a>-->
<!--						</li>-->
<!---->
<!--					</ul>-->
<!--		-->

				<iframe src="https://bca.baogang.info/buap/buaps/msg.jsp"  width=”380″ frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes"/>

				</div>
			</div>
		</div>
	</div>
	<div class="footer">版权所有 宝钢集团</div>
	</body>
</html>
