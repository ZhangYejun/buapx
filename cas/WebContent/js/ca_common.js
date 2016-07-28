// ����CA��JS�ļ�����Ҫʱ���������casLoginView.jsp�� 




String.prototype.trim=function()
{
     return this.replace(/(^\s*)(\s*$)/g, "");
}


if(typeof(UniContrl) != 'object')
{
  document.write("<object classid=\"CLSID:1256C72B-2043-47FE-B328-4E2801C7BEA6\" id=\"UniContrl\">");
  document.write("</object>");
}


window.onload=function(){
	document.getElementById("isUseCertDiv").style.display="none";
}
var hasLoaded=false;


function setCaRandomNumber(){
	$.get("<%=request.getContextPath()%>/getCaRandomNumber",function(result){
		  document.getElementById("caRandomNumber").value=result.trim();
		});
}


function setStateChangeCallback(object,callback){
	object.onreadystatechange=function(){  
		if(object.readyState == 'complete' || object.readyState == 'loaded'){  
		 object.onreadystatechange = null; 
		 callback.call();  
       } else{  
              caJs.onload = function(){  
                               caJs.onload = null; 
                               callback.call(); 
                            }  	
           }
	  }
}

function loadJS(id,src,callback){
	var dom=document.getElementById(id);
	setStateChangeCallback(dom, callback);
	dom.src=src;
  }	 
   
function listProvider(){
		//Show the certificates source select box, if needed.
		var cryptprovDom = document.getElementById("cryptprov"); //$("#cryptprov")[0];
		if (cryptprovDom) {
			//�о��豸
			enumProvider(cryptprovDom);
		}	
}

function onformSubmit() {
	//return true;
	var isUseCert=document.getElementById("useCertCheckbox");
	if(!isUseCert.checked){
		 document.getElementById("useCert").value="false";			  
		return;
		}
	var selectDom = document.getElementById("cryptprov");
	//�ж��Ƿ���USB KEY֤��ģʽ
	if (selectDom.value == "CCIT Smartcard CSP V1.0") {
		if (ISUKConnect() == 0) {
			alert("�����key");
			return false;
		}
	}
	//���豸����ʼ�������豸
	if (!initProv(selectDom.value)) {
		return false;
	} else {
		var certData = getCert();
		if (certData == '') {
			return false;
		}
		document.getElementById("cert").value = certData;
		//��ȡ֤�����к�
		var certSerialNum = getSerialNumber();
		document.getElementById("sn").value = certSerialNum;

		if (certSerialNum != "") {

			var randomNumber = document.getElementById("caRandomNumber").value;
			//alert(randomNumber);
			var sign = signature(randomNumber);
			if (sign != null && sign.length > 0) {
				document.getElementById("sign").value = sign;
			}
		}

		return true;
	}
}

function changeCaOptions(checkbox){
	  var cryptprovDiv = document.getElementById("isUseCertDiv");
		
	if(checkbox.checked){
		document.getElementById("useCert").value="true";
     if(!hasLoaded){ 
         loadJS("caJs","./js/ccitenrl.js",listProvider); 
         loadJS("jqueryJs","./js/jquery.min.js",setCaRandomNumber);  
        // listProvider(); 
         hasLoaded=true; 	 
         }
       cryptprovDiv.style.display="inline";
	  }else{
		 document.getElementById("useCert").value="false";			  
		  cryptprovDiv.style.display="none";
		}
}

