var peopleURN = "urn:oracle:webcenter:people";
var personURN = "urn:oracle:webcenter:people:person";
var messageBoardURN = "urn:oracle:webcenter:messageBoard";
var connectionsURN = "urn:oracle:webcenter:people:person:list";
var peopleReouserURLTemplate;
var messageBoardUrl;
var currentId;
var userId = "";

personInitialize = function(data) {
	getPersonData(data);
};

getPersonData = function(data) {
	var peopleResourceURL = WC.getLinkAttrByKeyValue(data.links,"resourceType", peopleURN,"href");
	peopleReouserURLTemplate = WC.getLinkAttrByKeyValue(data.links,"resourceType", personURN,"template");
	
	WC.getData(peopleResourceURL, displayPeopleResource);
};

getUserProfile = function(userId) {
	this.userId = userId;
	var query = "";
	
	if(userId == '')
		query = "loginid:equals:"+currentId;
	else
		query = "loginid:equals:"+userId;
	//var personResourceURL = "http://localhost:8888/rest/api/people?q=loginid:equals:weblogic&data=basic&projection=details&utoken=FM2EN1hL52-yndCSDPOPhdoon4Ir_w**";
	
	var personUrlTemplate = new UrlTemplate(peopleReouserURLTemplate);
	
	var personUrl = personUrlTemplate.toUrl({
		'q' : query,
		'data' : 'basic'
	});
	
	WC.getResourceData(personUrl, personURN, false, displayPersonResource);	// resource URL을 이용해 비동기 호출하여 Data 획득한다.
}

displayPeopleResource = function(data) {

	var displayPeopleData = "";
	displayPeopleData += "guid : " + data.guid + "<br>";
	displayPeopleData += "id : " + data.id + "<br>";
	displayPeopleData += "displayName : " + data.displayName + "<br>";
	displayPeopleData += "connected : " + data.connected + "<p>";
	
	currentId = data.id;
	
	$("#currentUser").html(displayPeopleData);
}

// people data...
displayPersonResource = function(data) {
	//alert(JSON.stringify(data));
	
	resourceData = "<br>============================================================================================================<br>";
	resourceData += "<b>- personURN :</b> "+personURN+"<br>";
	
	appendObjectChildData(data, 0);
	
	messageBoardUrl = WC.getLinkAttrByKeyValue(data.links,"resourceType", messageBoardURN,"href");
	$("#personResource").html(resourceData);
	
};

sendMessage = function(message) {
	var body = "{\"body\" : \""+message+"\"}";
	
	WC.actionData(messageBoardUrl, "POST", body, function() {
		alert("success!!!");
	});
}