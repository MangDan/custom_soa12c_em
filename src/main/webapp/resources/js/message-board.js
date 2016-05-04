messageBoardURN = "urn:oracle:webcenter:messageBoard";
var startIndex = 0;
var itemsPerPage = 50;

messageBoardInitialize = function(data) {
	getMessageBoardData(data);
};

getMessageBoardData = function(data) {
	var resourceURL = WC.getLinkAttrByKeyValue(data.links,"resourceType", messageBoardURN,"href");
	
	var messageBoardUrlTemplate = new UrlTemplate(WC.getLinkAttrByKeyValue(data.links,"resourceType", messageBoardURN,"href"));
	
	var messageBoardUrlTemplateUrl = messageBoardUrlTemplate.toUrl({
		'startIndex' : startIndex,
		'itemsPerPage' : itemsPerPage
	});
	
	//alert(messageBoardUrlTemplateUrl);
	WC.getData(messageBoardUrlTemplateUrl, getMessageBoardCallback);	// resource URL을 이용해 비동기 호출하여 Data 획득한다.
};

getMessageBoardCallback = function(data) {
	var message = "";
	var msgItem;
	var items = data.items;
	var msgBody;
	var msgCreated;
	var displayName;
	var actUrl;
	var createUrl = data.links[0].href;
	var writebutton = "<input type=\"button\" value=\"Write\" onclick=\"createMsg('"+createUrl+"')\" />";
	for ( var i = 0; i < items.length; i++) {
		msgItem = items[i];
		msgBody = msgItem.body;	//{actor[0]} posted a message
		msgCreated = msgItem.created;
		displayName = msgItem.author.displayName;
		actUrl = msgItem.links[0].href;
		message += displayName + "<br>" + msgBody + "<br>" + msgCreated + "<input type=\"button\" value=\"Delete\" onclick=\"javascript:msgDel('"+actUrl+"');\"/>" + "<input type=\"button\" id=\"delbtn_"+i+"\" value=\"Update\" onclick=\"javascript:msgUpdate('"+actUrl+"');\"/>" + "<p>";
		
	}
	
	message += writebutton;
	resourceData = "<br>============================================================================================================<br>";
	//resourceData += "<b>- peopleServiceURN :</b> "+peopleServiceURN+"<br>";
	
	appendObjectChildData(data, 0);
	
	$("#messageboardList").html(message);
	$("#messageboardData").html(resourceData);
	
	//$('#messageboardData').html(JSON.stringify(data));
}

msgDel = function(actUrl) {
	WC.actionData(actUrl, "DELETE", "", function() {
		WC.getResourceIndex(messageBoardInitialize);
	});
}

msgUpdate = function(actUrl) {
	var body = "{\"body\" : \"updated.....\"}";
	WC.actionData(actUrl, "PUT", body, function() {
		WC.getResourceIndex(messageBoardInitialize);
	});
}

createMsg = function(createUrl) {
	
	var body = "{\"body\" : \"created.....\"}";
	
	WC.actionData(createUrl, "POST", body, function() {
		WC.getResourceIndex(messageBoardInitialize);
	});
}