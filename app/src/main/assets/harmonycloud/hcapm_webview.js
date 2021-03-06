var xmlns="http://www.w3.org/2000/svg";
var w=window;
var d=window.document;
function getTimings(){
	var a=[];a.push(createEntryFromNavigationTiming());
	return a
}
function createEntryFromNavigationTiming(){
	var a=w.performance.timing;
	var b="about:blank";
	if(d.URL==b){
		b="about:blank";
	}

	return {
		url:d.URL,
		connectStart:a.connectStart,
		connectEnd:a.connectEnd,
		domComplete:a.domComplete,
		domContentLoadedEventEnd:a.domContentLoadedEventEnd,
		domContentLoadedEventStart:a.domContentLoadedEventStart,
		domInteractive:a.domInteractive,
		domLoading:a.domLoading,
		domainLookupEnd:a.domainLookupEnd,
		domainLookupStart:a.domainLookupStart,
		fetchStart:a.fetchStart,
		loadEventEnd:a.loadEventEnd,
		loadEventStart:a.loadEventStart,
		navigationStart:a.navigationStart,
		redirectEnd:a.redirectEnd,
		redirectStart:a.redirectStart,
		requestStart:a.requestStart,
		responseEnd:a.responseEnd,
		responseStart:a.responseStart,
		secureConnectionStart:a.secureConnectionStart,
		unloadEventEnd:a.unloadEventEnd,
		unloadEventStart:a.unloadEventStart
	}
}
function createEntryFromResourceTiming(a){
	return {
		url:a.name,
		start:a.startTime
	}
}
function sortFloat(a){
	return a.sort(function(e,c){
		return e-c
	});
}
function hcapm_error_catch(g){
	var a=[];
	if(typeof(g.timeStamp)=="undefined"){
		var j="";
	}else{
		var j=g.timeStamp;
	}
	if(typeof(g.filename)=="undefined"){
		var c="byfHello";
	}else{
		var c=g.filename
	}
	if(typeof(g.error.name)=="undefined"){
		var b=""
	}else{
		var b=g.error.name
	}
	if(typeof(g.message)=="undefined"){
		var h=""
	}else{
		var h=g.message
	}
	if(typeof(g.error.stack)=="undefined"){
		var l=""
	}else{
		var l=g.error.stack
	}
	var k=navigator.userAgent;
	var f=navigator.language;
	a.push(_hcapm_webview_1_);
	a.push(c);
	a.push(j);
	a.push(b);
	a.push(h);
	a.push(l);
	a.push(k);
	a.push(f);
	window.HarmonycloudWebViewProxy.onError(JSON.stringify(a));
}
window.addEventListener("error",hcapm_error_catch,false);
function resourceTiming(c){
	var n="getEntriesByType" in window.performance;
	if(!n){
		window.HarmonycloudWebViewProxy.androidLog("error","html5 feature not support getEntriesByType function,will not collect webview data.");
		return;
	}
	var b=window.performance.getEntriesByType("resource");
	var o=0;
	var k=0;
	var f=0;
	var e=0;
	var l=Array();
	var g=Array();
	var h=Array();
	for(i=0;i<b.length;i++){
		e=Math.round(b[i].responseEnd-b[i].startTime);
		if(b[i].initiatorType=="img"){
			l.push(e);
			o+=e;
		}else{
			if(b[i].initiatorType=="link"){
				f+=e;h.push(e);
			}else{
				if(b[i].initiatorType=="script"){
					k+=e;g.push(e);
				}
			}
		}
	}
	sortFloat(l);
	sortFloat(g);
	sortFloat(h);
	if(l.length!=0){
		if(l.length==1){
			var a={
				"count":1+"",
				"exclusive":o+"",
				"max":l[0]+"",
				"min":l[0]+"",
				"sum_of_squares":0+"",
				"total":o+""
			};
			window.HarmonycloudWebViewProxy.addImageMetric(JSON.stringify(a)+"",c+"");
		}else{
			var a={
				"count":l.length+"",
				"exclusive":o+"",
				"max":l[l.length-1]+"",
				"min":l[0]+"",
				"sum_of_squares":0+"",
				"total":o+""
			};
			window.HarmonycloudWebViewProxy.addImageMetric(JSON.stringify(a)+"",c+"");
		}
	}
	if(g.length!=0){
		if(g.length==1){
			var j={
				"count":1+"",
				"exclusive":k+"",
				"max":g[0]+"",
				"min":g[0]+"",
				"sum_of_squares":0+"",
				"total":k+""
			};
			window.HarmonycloudWebViewProxy.addScriptMetric(JSON.stringify(j)+"",c+"");
		}else{
			var j={
				"count":g.length-1+"",
				"exclusive":k+"",
				"max":g[g.length-1]+"",
				"min":g[0]+"",
				"sum_of_squares":0+"",
				"total":k+""
			};
			window.HarmonycloudWebViewProxy.addScriptMetric(JSON.stringify(j)+"",c+"");
		}
	}
	if(h.length!=0){
		if(h.length==1){
			var m={
				"count":1+"",
				"exclusive":f+"",
				"max":h[0]+"",
				"min":h[0]+"",
				"sum_of_squares":0+"",
				"total":f+""
			};
			window.HarmonycloudWebViewProxy.addLinkMetric(JSON.stringify(m)+"",c+"");
		}else{
			var m={
				"count":h.length-1+"",
				"exclusive":f+"",
				"max":h[h.length-1]+"",
				"min":h[0]+"",
				"sum_of_squares":0+"",
				"total":f+""
			};
			window.HarmonycloudWebViewProxy.addLinkMetric(JSON.stringify(m)+"",c+"");
		}
	}
}
function _hcapm_invoke_java_commit_data(a){
	_hcapm_webview_1_=a;
	setTimeout(function(){
		if(!window.performance.timing||!window.performance.timing.navigationStart){
			window.HarmonycloudWebViewProxy.androidLog("error","html5 feature not support ,will not collect webview data.");
			return;
		}
		var g=window.performance;
		var e=g.timing;
		var f=e.domComplete-e.navigationStart;
		var c=e.domainLookupEnd-e.domainLookupStart;
		if(f<0){
			f=0;
		}
		if(c<0){
			c=0;
		}
		resourceTiming(a);
		window.HarmonycloudWebViewProxy.addDomainLookupTime(JSON.stringify({"addDomainLookupTime":c+""}),_hcapm_webview_1_+"");
		window.HarmonycloudWebViewProxy.addTotalWebViewSummary(JSON.stringify({"total_webview_summary":f+""}));
		window.HarmonycloudWebViewProxy.addSingleWebViewSummary(JSON.stringify({"singe_webview_summary":f+""}),_hcapm_webview_1_+"");
		window.HarmonycloudWebViewProxy.addWebViewSummaryMetric(JSON.stringify({"singe_webview_summary":f+""}),_hcapm_webview_1_+"");
		var b=getTimings();
		window.HarmonycloudWebViewProxy.fetchPageContent(JSON.stringify(b[0]),_hcapm_webview_1_+"")
	},300);
}
window._hcapm_invoke_java_commit_data=_hcapm_invoke_java_commit_data;