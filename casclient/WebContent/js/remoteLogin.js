/**
 *
 */

var TIMEOUT = 3000

function CasRemoteClient(casAuthUrl, loginCallback,ssoCallback,service) {
	this.casAuthUrl = casAuthUrl;
	this.loginCallbackWrapper = new LoginCallbackWrapper(loginCallback);
	this.ssoCallbackWrapper = new SsoCallbackWrapper(ssoCallback);
	this.service=service;
	this.login = function(username, password) {
		var data = {};
		data.username = username;
		data.password = password;
		data.service = this.service;
		$.ajax({
			type:"post",
			url : this.casAuthUrl,
			dataType : 'jsonp',
			data : data,
			jsonp : 'callback',
			jsonpCallback:"jsonpCallback",
			crossDomain:true,
			success : this.loginCallbackWrapper.success,
			error : this.loginCallbackWrapper.error,
			timeout : TIMEOUT
		});

	}
	this.sso = function() {
		var data = {};
		data.service=this.service;
		$.ajax({
			type:"post",
			url : this.casAuthUrl,
			dataType : 'jsonp',
			data : data,
			jsonpCallback:"jsonpCallback",
			jsonp : 'callback',
			crossDomain:true,
			success : this.ssoCallbackWrapper.success,
			error : this.ssoCallbackWrapper.error,
			timeout : TIMEOUT
		});
	}
}

function LoginCallbackWrapper(callback) {
	var callbackBase = callback;
	this.success = function(data) {
		if (data.success == true) {
			var result = CasAuthUtil.validateTicketAndInit(data.ticket);
			if (result.success = true) {
				callbackBase(true, result.msg, result.user);
			}
		} else {
			callbackBase(false, result.msg, null);
		}
	}

	this.error = function() {
		callbackBase(false, "认证发生异常", null);
	}


}

function SsoCallbackWrapper(callback) {
	var callbackBase = callback;
	this.success = function(data) {
		alert(data.msg);
		if (data.success == true) {
			var result = CasAuthUtil.validateTicketAndInit(data.ticket);
			if (result.success = true) {
				callbackBase(true, result.msg, result.user);
			}
		} else {
			callbackBase(false, data.msg, data.user);
		}
	}

	this.error = function(data,status) {
		alert("异常");
		callbackBase(false, "认证发生异常", null);
	}

}

	var CasAuthUtil = {
		validateTicketAndInit : function(ticket) {
			var data = {};
			data.msg = "票据验证成功";
			data.user = "190222";
			data.success = true;
			return data;
		}

	}


