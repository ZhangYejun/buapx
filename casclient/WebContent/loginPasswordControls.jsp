<%@page import="java.net.URLEncoder"%>
<%@page import="com.baosight.buapx.security.common.UrlConstructor"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script language="javascript" src="js/PassGuardCtrl.js"></script>
<script type="text/javascript">
	function FormSubmit_sign(){  
	var d = new Date();
	$.ajax({
		async: false, 
		url: "http://localhost:9080/cas/srand_num.jsp?"+d.getTime(),
		type: "get",
		data:{},
		dataType: 'jsonp',
   		jsonp: 'jsoncallback',
		success: function(data){
				var  srand_num = data.data;
		   		pgeditor.pwdSetSk(srand_num);
		    	var PwdResult=pgeditor.pwdResult();
				var machineNetwork=pgeditor.machineNetwork();
				var machineDisk=pgeditor.machineDisk();
				var machineCPU=pgeditor.machineCPU();
				var aaa = $("#password").val(PwdResult);//获得密码密文,赋值给表单
			    document.forms[0].submit();
			    document.forms[0].method.value = '';
		},
		error:function(){
			alert("error");
		}
	 });

}
	var pgeditor = new $.pge({
		pgePath: "download/ocx/",//控件文件目录
		pgeId: "_ocx_password",//控件ID
		pgeEdittype: 0,//控件类型,0星号,1明文
		pgeEreg1: "[\\s\\S]*",//输入过程中字符类型限制
		pgeEreg2: "[\\s\\S]{6,30}",	//输入完毕后字符类型判断条件
		pgeMaxlength: 30,//允许最大输入长度
		pgeTabindex: 0,//tab键顺序
		pgeClass: "ocx_style",//控件css样式
		pgeInstallClass: "ocx_style",//针对安装或升级
		pgeOnkeydown:"FormSubmit_sign()",//回车键响应函数
	    tabCallback:"gdkey"
	});	
	window.onload = function(){	 
	   	$("#logindiv").focus();
	    pgeditor.pgInitialize();//初始化控件
	    //window.setInterval("GetLevel()",800); //实时显示密码强度
	}
</script>
<style>
.ocx_style{border:1px solid #555; width:163px; height:22px;display:inline-block;}
.ocx_style:hover{border:1px solid #79B0E9;}
</style>
</head>
<%
//感知是否已经登录，如果已经登录，直接跳转到首页
String login=request.getParameter("login");
if(login==null||login.equals("")){
	response.sendRedirect("index.jsp");
}
//取得从cas返回的service，如果直接输入login.jsp，则拼接service为首页
String service=request.getParameter("service");
if(service!=null){
	service=URLEncoder.encode(service, "UTF-8");
}else{
	service=UrlConstructor.constructServiceExternal("/index.jsp");
}
  %>
<body>
<div id="logindiv" style="float: left; display: inline; margin-right: 11px;">
 	<form action="http://localhost:9080/cas/remoteLogin?service=<%=service%>" method="post">
		<table>
		<tr>
		<td></td><td><%=request.getParameter("error") %></td>
		</tr>
			<tr>
				<td>用户名</td>
				<td><input type="text" name="username" /></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><script type="text/javascript">pgeditor.generate()</script>
          		<input type="hidden" name="selcert1" id="selcert1" width="20"  value="">
				<input type="hidden" name="password" id="password" value=""/></td>
			</tr>
			<tr>
			<td>验证码</td>
			<td><input type="text" class="wid" name="_captcha_parameter"
							 tabindex="1" style="width: 100px;" maxlength="4" />
							 <img id="captcha"
							 width="60" height="21"
							onclick="this.src = 'http://localhost:9080/cas/getCaptchaImage?d='+new Date().getTime();"
							title="看不清,换一张" src='http://localhost:9080/cas/getCaptchaImage?d='+new Date().getTime();
							style="cursor: pointer; position:relative;"/>
			</td>
			</tr>
			<tr>
				<td><input type="button" value="登录" onclick="FormSubmit_sign();"/></td>
			</tr>
		</table>
		 <input type="hidden" name="useControls"  id="useControls" value="1"/>
		 <input type="hidden" name="usertype" value="PERSONAL"/>
		 <input type="hidden" name="action" value="submit"/>
		<input type="hidden" value="http%3A%2F%2Flocalhost%3A8080%2Fcas_client_all%2Flogin.jsp" name="remoteLoginPage" />

	</form>
</div>	
</body>
</html>