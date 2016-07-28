 var login_alert="不适当的用户名或口令!";
 function loginAccount(){
	if (document.frmLogin.userid.value=="" || document.frmLogin.pwd.value==""){
		alert(login_alert);
		return false;
	}
  pathname = location.pathname;
  myDomain = pathname.substring(0,pathname.lastIndexOf('/')) +'/';
  var largeExpDate = new Date ();
  largeExpDate.setTime(largeExpDate.getTime() + (365 * 24 * 3600 * 1000));

	try{
		if(document.frmLogin.rememberMeUid.checked==true){
		    SetCookie('username',document.frmLogin.userid.value,largeExpDate,myDomain);
		}else{
		    SetCookie('username',null,null);
		}
		if(document.frmLogin.rememberMePwd.checked==true){
		    SetCookie('userpassword',document.frmLogin.pwd.value,largeExpDate,myDomain);
		}else{
		    SetCookie('userpassword',null,null);
		}				
		
	}catch(e){alert(e);}	
	//SetCookie('portal_lang',document.frmLogin.PORTAL_LANG.value,largeExpDate,myDomain);
	document.frmLogin.action="../workflow/login.wf";
	document.frmLogin.submit();
}

 function onFocus(){ 	
 	var userName=GetCookie('username');
 	var userpassword=GetCookie('userpassword');
 	//var portal_lang=GetCookie('portal_lang');
	//var objSelect=document.getElementById("PORTAL_LANG");
	//语言自动选择
	/**
	if(portal_lang!=null)
	{
		lang_chang(portal_lang);
		for (var i = 0; i < objSelect.options.length; i++) {        
    	    if (objSelect.options[i].value == portal_lang) {        
    	    	   objSelect.options[i].selected = true;
    	    }
    	} 
	}
	*/
 	if(userName != 'null' && userName!=null){
 		document.frmLogin.userid.value=userName;
 		document.frmLogin.rememberMeUid.checked=true;
 		if(userpassword == 'null'){
 			document.frmLogin.pwd.focus();
 		}
 	}else{
 		document.frmLogin.userid.value="";
 		document.frmLogin.rememberMeUid.checked=false; 	
 		document.frmLogin.userid.focus();
 	}
 	if(document.frmLogin.userid.value=='null'){
 		document.frmLogin.userid.value="";
 		document.frmLogin.pwd.value="";
 		document.frmLogin.rememberMeUid.checked=false;
 		document.frmLogin.rememberMePwd.checked=false;
 		document.frmLogin.userid.focus();
 	}else{
	 	if(userpassword != 'null' && userpassword!=null){
	 		document.frmLogin.pwd.value=userpassword;
	 		document.frmLogin.rememberMePwd.checked=true;
	 	}else{
	 		document.frmLogin.pwd.value="";
	 		document.frmLogin.rememberMePwd.checked=false; 	
	 		document.frmLogin.userid.focus();
	 	} 
	} 	
 }

function GetCookie (cookieName) {
	if(document.cookie!=''){
		var cookieNames = document.cookie.split(';');
		for(i=0;i<cookieNames.length;i++){
			if(cookieNames[i].indexOf(cookieName)>-1){
				return unescape(cookieNames[i].split('=')[1]);
			}
		}
	}
}
function sChange(obj)
{
   lang_chang(obj.value);
}
function lang_chang(val)
{
	if(val=="en"){//英语
    	document.getElementById("userid").placeholder="Please enter the account number";
		document.getElementById("password").placeholder="Please enter your password";
		document.getElementById("jzzh").innerHTML="Remember UserName";
		document.getElementById("jzmm").innerHTML="Remember Password";
		document.getElementById("dl").innerHTML="Login";
		document.getElementById("deej").innerHTML="buapx";
		login_alert="Inappropriate user name or password!";
    }else if(val=="big5"){//繁体
		document.getElementById("userid").placeholder="請輸入賬號";
		document.getElementById("password").placeholder="請輸入密碼";
		document.getElementById("jzzh").innerHTML="記住賬號";
		document.getElementById("jzmm").innerHTML="記住密碼";
		document.getElementById("dl").innerHTML="登錄";
		document.getElementById("deej").innerHTML="统一认证";
		login_alert="不適當的用戶名或口令!";		
	}else//中文
	{
		document.getElementById("userid").placeholder="请输入帐号";
		document.getElementById("password").placeholder="请输入密码";
		document.getElementById("jzzh").innerHTML="记住账号";
		document.getElementById("jzmm").innerHTML="记住密码";
		document.getElementById("dl").innerHTML="登录";
		document.getElementById("deej").innerHTML="统一认证";
		login_alert="不适当的用户名或口令!";
	}
}
/**
  设置Cookie
*/
function SetCookie (cookieName, cookieValue, expires, path, domain, secure) {
var Days = 30;  
var exp = new Date();   
exp.setTime(exp.getTime() + Days*24*60*60*1000);  
document.cookie = cookieName + "="+ escape (cookieValue) + ";expires=" + exp.toGMTString();  
}
