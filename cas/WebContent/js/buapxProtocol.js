


var userAgent = navigator.userAgent.toLowerCase();
var ie = /msie/.test(userAgent) && !/opera/.test(userAgent);
var g = {
	closeWindow : function() {
		if ((ie && window.history.length > 0)
				|| (!ie && window.history.length > 1)) {
			window.history.back(1);
			return;
		}
		if (ie) {
			window.opener = null;
			window.open('', '_top');
			window.top.close();
		}
	}
};
function sendBuapxProtocol(protocolName, stTicket) {
	
	var _protocolName = !!protocolName ? protocolName:"buapx";
	var _stTicket = !!stTicket ?stTicket:"";
	var casLocation=null;
	if(protocolName.indexOf("IP4CWI_")!=-1){
		casLocation="IP4CWI:"+_stTicket+"!!"+protocolName.split("_")[1];
	}else{
		 casLocation= _protocolName+":ticket="+_stTicket;
	}
	 
	window.location = casLocation;
		window.setTimeout(function() {
			g.closeWindow();
		}, 200);
	
}