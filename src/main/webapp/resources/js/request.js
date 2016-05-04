getPortletContext = function(containerKey, portletKey, layerType, id, title, left, top, width, height, zindex, url) {

	showResponse = function(originalRequest) {
		var portletIns = document.getElementById(id);
		portletIns = (portletIns) ? portletIns : document.getElementById(portletKey).cloneNode(true);

		portletIns.id 					= id;
		portletIns.style.left 	= left;
		portletIns.style.top 		= top;
		portletIns.style.width 	= (width < 0)	? 0 : width;
		portletIns.style.height = (height < 0)? 0 : height;
		portletIns.style.position = (layerType == 0) ? "static" : 
																(layerType == 1) ? "absolute" : "relative";
		portletIns.style.zIndex = zindex;
		
		var portletTitle = portletIns.getElementsByTagName("LABEL");
		if(portletTitle.length > 0) portletTitle[0].innerHTML = title;
		
		var contentIns = portletIns.getElementsByTagName("DIV");
		if(contentIns.length > 0) {
			contentIns[0].style.overflowX = (width < 0)	? "visible" : "auto";
			contentIns[0].style.overflowY	= (height < 0)? "visible" : "auto";
			contentIns[0].innerHTML = originalRequest.responseText;
			contentIns[0].style.zIndex = zindex;
		}

		var containerIns = document.getElementById(containerKey);
		if(containerIns) containerIns.appendChild(portletIns);

		portletIns.style.display = "inline";
	}

	var req = new Ajax.Updater (
		{success : ''},
		url,
		{
			method: 'post', 
			parameters: null, 
			onSuccess : showResponse,
			evalScripts : true
		}
	);
	
}

loadContext = function(uri,layerKey,query) {
	successResponse = function(originalRequest) {}
	failureResponse = function(originalRequest) {}
	completeResponse = function(originalRequest) {
		try {
			openLayer(layerKey,originalRequest);
		}catch(e){}
	}
	
	
	
	var req = new Ajax.Updater (
		{success : layerKey , failure: 'abd'},
		uri,
		{
			method: 'post', 
			parameters: query,
			onSuccess : successResponse,
			onFailure : failureResponse,
			onComplete : completeResponse,
			asynchronous : true,
			evalScripts : true
		}
	);
	//alert(req); 

}

ajax_load_jquery = function(uri,layerKey,callback) {
	$(layerKey).load(uri, "", callback);
}

ajax_get_jquery = function(uri,layerKey,callback) {
	$.get(
		uri,
		"",
		callback
	).success(function() {
		//success code
	})
	 .error(function() {
		//alert('Error loading JSON data');
	})
	 .complete(function() {
		//complete code
	});
}

ajax_getjson_jquery = function(uri,param,callback) {
	$.getJSON(
		uri,
		param,
		callback
	).success(function() {
		//success code
	})
	 .error(function() {
		//alert('Error loading JSON data');
	})
	 .complete(function() {
		//complete code
	});
}

ajax_jquery = function (url,method,layerKey,query) {
	//loadingbar : content_loading
	$.ajax({
    url: url,
    type: method,
    async: true,
    timeout: 30000,
    cache: false,
    data: query,
    error: function(){
        //alert('Error loading XML document');
    },
    success: function(data){
        
		if(layerKey != '')
			$(layerKey).html(data);
		
		//$(layerKey).fadeOut(600, function ()
		//{
		//	$(layerKey).html(data);
		//});
		//$(layerKey).fadeIn(10);
   
    }
	});
}

ajax_jquery_loading = function (url,method,layerKey,loadingLayerKey, query) {
	//loadingbar : content_loading
	$.ajax({
    url: url,
    type: method,
    async: true,
    timeout: 30000,
    cache: false,
    data: query,
    error: function(){
        //alert('Error loading XML document');
    },
    success: function(data){
        $(layerKey).html(data);
    },
    beforeSend: function() {
		//var padingTop = (Number(($(layerKey).css('height')).replace("px","")) / 2) - 20;
		
        //var left = ($(window).width() - $('#content_loading').outerWidth()) / 2;
		
		//통신을 시작할때 처리
		//$('#content_loading').css('position', 'absolute');
		//$('#content_loading').css('left', left);
		//$('#content_loading').show().fadeIn('fast');
		
		$(loadingLayerKey).css('display', '');
	}, 
	complete: function() {
		//통신이 완료된 후 처리
		//$('#content_loading').fadeOut();
		//$(layerKey).html("loading...........");
		$(loadingLayerKey).css('display', 'none');
	}
	});
}

function createXMLHttpRequest() {
    xmlHttp =	(window.ActiveXObject)	? new ActiveXObject("Microsoft.XMLHTTP") :
				(window.XMLHttpRequest)	? new XMLHttpRequest() : null;
}

function getRequestText( _method, _url, _asy ){
	createXMLHttpRequest();     
	xmlHttp.open( _method, _url, _asy);
	xmlHttp.send('');

	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {     
			var xmlText = xmlHttp.responseText;                      
			return xmlText;             
		}
	}            
}

function getRequestXML( _method, _url, _asy ){
	createXMLHttpRequest();     
	xmlHttp.open( _method, _url, _asy);
	xmlHttp.send('');
	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {
			var xmlDoc = xmlHttp.responseXML;             
			return xmlDoc;             
		}
	}
}

createDivWindow = function() {
	var divWindow = document.createElement("DIV");
  	document.body.appendChild(divWindow);
}

