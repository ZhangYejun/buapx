<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  <OBJECT classid=clsid:5EEEA87D-160E-4A2D-8427-B6C333FEDA4D id=RTXAX></OBJECT>
  <body onload="javascript:init();">
  </body>
</html>

<SCRIPT LANGUAGE="JavaScript">
<!--
function init(){ 
    try{
       var key="${sessionKey}";
       if(key!=""){
           var ip="10.25.36.80";
           var objProp = RTXAX.GetObject("Property");
           objProp.value("RTXUsername") = "${username}";
           alert("${username}");
           objProp.value("LoginSessionKey") =key;
            objProp.value("ServerAddress") = ip; //RTX Server IP地址
            objProp.value("ServerPort") = 8000;
            alert(objProp.value("LoginSessionKey"));
           RTXAX.Call(2,objProp);  //2表示通过SessionKey登录
        }
   }catch(e){
	   alert("启动rtx出错");
   }
}
//-->
</SCRIPT>
