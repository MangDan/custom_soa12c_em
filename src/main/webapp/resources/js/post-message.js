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
	
	displayConnectionsResource(WC.getLinkAttrByKeyValue(data.links,"resourceType", connectionsURN,"template"));
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

displayConnectionsResource = function(connectionsUrlTemplate) {
	var connectionsData = "";
	connectionsUrlTemplate = new UrlTemplate(connectionsUrlTemplate);
	
	var connectionsUrl = connectionsUrlTemplate.toUrl({
		'startIndex' : '0',
		'itemsPerPage' : '50',
		'projection' : 'details'
	});
	
	WC.getData(connectionsUrl, function(data) {
		//alert(JSON.stringify(data));
		//appendObjectChildData(data, 0);
		//$("#connectionsData").html(resourceData);
		
		connectionsData += "<table border='1'>";
		connectionsData += "<thead>";
		connectionsData += "<th>ID</th>";
		connectionsData += "<th>DISPLAY NAME</th>";
		connectionsData += "<th>GUID</th>";
		connectionsData += "<th>EMAIL</th>";
		connectionsData += "<th>MOBILE</th>";
		connectionsData += "<th>DEPARTMENT</th>";
		connectionsData += "<th>EMPLOYEENUMBER</th>";
		connectionsData += "</thead>";
		connectionsData += "<tbody>";
		for(var i=0; i<data.items.length; i++) {
			connectionsData += "<tr>";
			connectionsData += "<td>"+data.items[i].id+"</td>";
			connectionsData += "<td>"+data.items[i].displayName+"</td>";
			connectionsData += "<td>"+data.items[i].guid+"</td>";
			connectionsData += "<td>"+data.items[i].emails.value+"</td>";
			for(var n=0; n<data.items[i].phoneNumbers.length; n++) {
				if(data.items[i].phoneNumbers[n].type == 'MOBILE')
					connectionsData += "<td>"+data.items[i].phoneNumbers[n].value+"</td>";
			}
			connectionsData += "<td>"+data.items[i].organizations.department+"</td>";
			connectionsData += "<td>"+data.items[i].organizations.employeeNumber+"</td>";
			connectionsData += "</tr>";
			
		}
		connectionsData += "</tbody>";
		connectionsData += "</table><p>";
		
		$("#connectionsData").html(connectionsData);
	});
}

sendMessage = function(message) {
	var body = "{\"body\" : \""+message+"\"}";
	
	WC.actionData(messageBoardUrl, "POST", body, function() {
		alert("success!!!");
	});
}