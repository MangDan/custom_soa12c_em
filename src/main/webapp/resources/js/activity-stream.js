activityStreamURN = "urn:oracle:webcenter:activities:stream";
var startIndex = 0;
var itemsPerPage = 50;

activityStreamInitialize = function(data) {
	getActivityStreamData(data);
};

getActivityStreamData = function(data) {
	var resourceURL = WC.getLinkAttrByKeyValue(data.links,"rel", activityStreamURN,"template");
	alert(resourceURL);
	var activityStreamUrlTemplate = new UrlTemplate(WC.getLinkAttrByKeyValue(data.links,"resourceType", activityStreamURN,"template"));
	
	var activityStreamTemplateUrl = activityStreamUrlTemplate.toUrl({
		'startIndex' : startIndex,
		'itemsPerPage' : itemsPerPage,
		'personal' : 'true',
		'personGuid' : '@me',
		'connections' : 'true',
		'advancedQuery' : "TO_CHAR(AE.ACTIVITY_TIME,'YYYY-MM-DD') > '2014-03-03' AND TO_CHAR(AE.ACTIVITY_TIME,'YYYY-MM-DD') <= '2014-03-10'" 
	});
	
	alert(encodeURIComponent(activityStreamTemplateUrl));
	WC.getData(activityStreamTemplateUrl, getActivityStreamCallback);	// resource URL을 이용해 비동기 호출하여 Data 획득한다.
};

getActivityStreamCallback = function(data) {
	
	resourceData = "<br>============================================================================================================<br>";
	//resourceData += "<b>- peopleServiceURN :</b> "+peopleServiceURN+"<br>";
	
	appendObjectChildData(data, 0);
	
	$("#activityStream").html(resourceData);
	
	//$("#activityStream").html(JSON.stringify(data));
	var activity;
	var strMessage;
	var html = '';
	var items = data.items;
	
	for ( var i = 0; i < items.length; i++) {
		activity = items[i];
		strMessage = activity.message;	//{actor[0]} posted a message

		// Look for activity parameters and replace them in the message.
		if (activity.templateParams && activity.templateParams.items) {
			for ( var j = 0; j < activity.templateParams.items.length; j++) {
				var param = activity.templateParams.items[j];
				
				if (param.links != null) {
					// "< a href="urn:oracle:webcenter:spaces:profile:href">weblogic</a>"
					value = '<a href="'+ WC.getLinkAttrByKeyValue(param.links, 'rel', 'alternate', 'href') + '">'+ param.displayName + '</a>';
				} else {
					value = param.displayName;	// "weblogic"
				}
				// key : {actor[0]}
				strMessage = strMessage.replace(param.key, value);
				//alert(strMessage);
			}
		}
		if (activity.detail) {
			strMessage = strMessage + '<br><em>' + activity.detail + '</em>';
			
		}
		
		//alert(strMessage);
		html += strMessage + "<br>";
	}
	
	$("#activityStreamList").html(html);
	
};