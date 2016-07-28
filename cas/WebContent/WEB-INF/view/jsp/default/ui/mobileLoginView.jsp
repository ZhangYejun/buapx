<%@ page session="true"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<!DOCTYPE html PUBLIC "" "">
<HTML>
<HEAD>
<META content="IE=11.0000" http-equiv="X-UA-Compatible">

<META charset="utf-8">
<TITLE>统一认证</TITLE>
<META name="viewport" content="width=device-width, initial-scale=1">
<LINK href="js/jquery.mobile-1.3.0.min.css" rel="stylesheet">
<LINK href=js/mobile.css " rel="stylesheet">
<SCRIPT src="js/mobile_login.js" type="text/javascript"></SCRIPT>
<script type="text/javascript" id="caJs">
	function blurq() {
		var username = document.getElementById("username");
		var p_username = username.value;
		if (p_username == "") {
			username.value = "工号/手机号码/邮箱地址";
			username.style.color = "#666";
			username.style.fontSize = "12px";
			username.style.fontWeight = "100";
			//#CFC8C8
		}
	}

	function focusq() {
		var username = document.getElementById("username");
		var p_username = username.value;
		if (p_username == "工号/手机号码/邮箱地址") {
			username.value = "";
			username.style.color = "black";
			username.style.fontSize = "16px";
			username.style.fontWeight = "bold";
		}
	}


</script>
<STYLE type="text/css">
.login-font {
	color: #000000;
	font-size: 13px;
}

.login-footer {
	color: #000000;
	font-size: 12px;
	padding-left: 15px;
}

.btnsize {
	height: 45px;
	width: 45px;
}

body {
	overflow: auto;
	word-break: break-all;
	font-size: 14px;
	font-family: "Arial", "Arial", "Helvetica", "sans-serif";
	background-image: url('images/background14.jpg');
	background-attachment: fixed;
}
</STYLE>

<SCRIPT type="text/javascript"></SCRIPT>

<META name="GENERATOR" content="MSHTML 11.00.9600.17280">
</HEAD>
<BODY>
	<DIV id="logout-page" data-role="page">
<form:form method="post" commandName="${commandName}" htmlEscape="true" name="frmLogin">		
			<TABLE width="100%" height="340" border="0" cellspacing="0"
				cellpadding="0">
				<TBODY>
					<TR align="center" valign="middle">
						<TD>
							<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
								<TBODY>
								<TR>
								&nbsp&nbsp&nbsp统一认证
								<form:errors path="*" element="font" />
								</TR>
									<TR>
										<TD width="30%" height="35" align="right"><SPAN
											class="zi">帐号</SPAN>&nbsp;&nbsp;</TD>
										<TD width="39%" align="left">
										<form:input cssClass="required" class="margin-top5"
										id="username" name="username" tabindex="1"
										accesskey="${userNameAccessKey}" path="username"
										autocomplete="false" htmlEscape="true" size="14"   onblur="blurq()"
										onfocus="focusq();"  /></TD>
										<TD width="31%" align="left" rowspan="2"><INPUT
											class="btnsize" 
											type="submit" border="0" value="登录"> <!--div class="ui-block-b" style="margin-top:7px;width:50%">
   </div--></TD>
									</TR>
									<TR>
										<TD align="right"><SPAN class="zi">密码</SPAN>&nbsp;&nbsp;</TD>
										<TD align="left"><form:password cssClass="required"
									cssErrorClass="error" id="password" class="margin-top5" tabindex="1" 
									size="14"
									path="password" accesskey="${passwordAccessKey}" 
									htmlEscape="true" autocomplete="off" /></TD>
									</TR>
									<TR>
										<TD>&nbsp;</TD>
										<TD align="left" colspan="2">&nbsp;</TD>
									</TR>
									<TR>
										<TD>&nbsp;</TD>
										<TD align="left" colspan="2">
											<!-- 
											<LABEL for="rememberMeUid"><a id="forgetPwd" href="http://192.168.3.168:8088/buapx/BUAPX/UA/SY/userLabel.jsp"  target="_blank">忘记密码?</a></LABEL>
											<INPUT name="rememberMe" id="rememberMe"
											type="checkbox">自动登录
										 	 -->
										</TD>
									</TR>
									<TR>
										<TD>&nbsp;</TD>
										<TD align="left" colspan="2">&nbsp;</TD>
									</TR>
								</TBODY>
							</TABLE>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			<INPUT name="cmd" type="hidden"
				value="com.actionsoft.apps.portal.mobile_Mobile_Portal_Login">
			<!-- <input type=hidden name=PORTAL_LANG value="cn"> -->
			<INPUT name="extInfo" type="hidden">

			<DIV style="display: none;">
				<DIV class="ui-block-b">
					<LABEL for="rememberMeUid"><FONT id="jzzh">记住账号</FONT></LABEL>
				</DIV>
				<DIV class="ui-block-b">
					<LABEL for="rememberMePwd"><FONT id="jzmm">记住密码</FONT></LABEL>
				</DIV>
				<DIV class="ui-block-b" style="width: 50%; margin-top: 7px;">
				</DIV>
			</DIV>
		<input type="hidden" name="lt" value="${loginTicket}" />
		<input type="hidden" name="execution" value="${flowExecutionKey}" />
		<input type="hidden" name="_eventId" value="submit" />
		</form:form>
	</DIV>
	<div class="foot">
	<div style="width:400px;height:1px;margin:0px auto;padding:0px;background-color:black;overflow:hidden;"></div>
	<br>
	统一认证平台 版权所有Copyright  2013-2016
	</div>
	<SCRIPT>
		onFocus();
	</SCRIPT>
</BODY>
</HTML>
