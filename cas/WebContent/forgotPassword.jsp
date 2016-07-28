<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page session="false"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%--
<spring:theme code="mobile.custom.css.file" var="mobileCss" text="" />
 --%>
<title>宝信统一认证平台</title>

<link href="css/css.css" " rel="stylesheet" type="text/css" />
<
<style>
<!--
#center {
	MARGIN-RIGHT: auto;
	MARGIN-LEFT: auto;
}
-->
</style>
<script>
	function changeImage() {
		document.getElementById('kaptchaImage').src = 'jcaptcha.jpg?captchaId='
				+ Math.random();
	}
	function submitAction(type) {
	//	document.getElementById("type").value = type;
		document.form1.submit();
	}
</script>
</head>

<body>
	<form id="form1" name="form1" method="post"
		action="/cas/forgotPassword">


		<%--  
<div id="wrapper">
  <div id="top">
  </div>
  <div id="main">
    <div id="retrieve_password">

      <h1><img src="./images/services/page2_icon1.jpg" width="80" height="48" /><img
							src="./images/services/password2_03.gif" width="95" height="68" />
					</h1>
      <div class="clear"></div>
      <div id="block_bg">
        <div><img src="./images/services/frm_bar1.gif" width="400" height="7" /></div>
        <div id="password1_frm" >${fn:escapeXml(description)}
          <br/><label for="textfield2">&nbsp;&nbsp;&nbsp;<label for="username" class="fl-label"><spring:message code="screen.welcome.label.netid" /></label></label>
          <input type="text" name="loginId" id="loginId" />
          <br/>&nbsp;&nbsp;&nbsp;<label for="validatecode" class="fl-label"><spring:message code="screen.welcome.label.validatecode" /></label>
		  <input type="hidden" name="type" id="type" />
          <input type="text" name="validatecode" id="validatecode" />
          <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="jcaptcha.jpg" id="kaptchaImage" onclick="changeImage();"/>
        <input  type="button" onclick="submitAction('hint')" class="retrieve_password_btn mid" id="button" value="密码提示" />
		<input  type="button"onclick="submitAction('send')" class="retrieve_password_btn mid" id="button" value="发送到邮箱" />
      </div>
	  <br>
      <div><img src="./images/services/frm_bar2.gif" width="400" height="7" /></div>
      </div>
    </div>
  </div>
  <div class="password2_btm" id="password2_btm">版权所有©上海宝信软件股份有限公司</div>
</div>
--%>

		<div id="top"></div>
		<div id="center">
			<table border="0" align="center" cellpadding="5" cellspacing="0">
				<tr>
					<td colspan="2" style="color: blue; font-size: 13; line-height: 2">
						<br></br> 系统会向您注册的邮箱发送重新设置密码的确认信， <br />点击"确认"按钮，发送邮件。 <br />发送邮件可能有一定的时间延迟。
						<br />如果您没有注册的邮箱或者忘记邮箱密码，请联系您所在部门的信息化联络员申请邮箱或初始化邮箱密码。 <br></br></td>
				</tr>
				<tr>
					<td>工号</td>
					<td><input type='text' id='userLabel' name='userLabel' /> <span
						style="color: red">*</span>
					</td>
				</tr>
				<tr>
					<td>验证码</td>
					<td><img src="jcaptcha.jpg" id="kaptchaImage"
						onclick="changeImage();" /></td>
				</tr>

				<tr>
					<td colspan="2"><input type="button"
						onclick="submitAction('send')" class="retrieve_password_btn mid"
						id="button" value="确认" /> <input type="button"
						onclick="window.close()" class="retrieve_password_btn mid"
						id="button" value="关闭" /></td>
				</tr>


			</table>
		</div>
		<div class="clear"></div>
		<div id="btm">版权所有©上海宝信软件股份有限公司</div>
	</form>
</body>
</html>
