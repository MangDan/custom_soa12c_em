var personURN = "urn:oracle:webcenter:people:person";
var peopleURN = "urn:oracle:webcenter:people";
var connectionsURN = "urn:oracle:webcenter:people:person:list";
var connectionListNamesURN = "urn:oracle:webcenter:people:person:listNames";
var connectionsByListURN = "urn:oracle:webcenter:people:person:list:list";
var invitationURN = "urn:oracle:webcenter:people:invitations";
var userId = "weblogic";
var inviteUrl = "";

peopleConnectionsInitialize = function(data) {
	getPeopleConnectionsData(data);
};

getPeopleConnectionsData = function(data) {
	var peopleResourceURL = WC.getLinkAttrByKeyValue(data.links,"resourceType", peopleURN,"href");
	var invitationURL = WC.getLinkAttrByKeyValue(data.links,"resourceType", invitationURN,"template");
	
	WC.getData(peopleResourceURL, displayPeopleResource);
	WC.getData(peopleResourceURL, displayConnectionListNames);
	
	var invitationURLTemplate = new UrlTemplate(invitationURL);
	
	// ���� �ʴ���� �ʴ� �޽���..
	invitationURL = invitationURLTemplate.toUrl({
		'q' : 'invitee:equals:@me'
	});
	
	WC.getData(invitationURL, displayPendingInvitees);
};

displayPeopleResource = function(data) {
	var connectionsData="";
	var connectionsUrl = WC.getLinkAttrByKeyValue(data.links,"resourceType", connectionsURN,"template");
	inviteUrl = WC.getLinkAttrByKeyValue(data.links,"resourceType", connectionsURN,"href");

	var connectionsURLTemplate = new UrlTemplate(connectionsUrl);
	
	connectionsUrl = connectionsURLTemplate.toUrl({
		'projection' : 'basic',
		'startIndex' : '0',
		'itemsPerPage' : '50'
	});
	
	WC.getData(connectionsUrl, function(data) {
	
		connectionsData += "ALL";
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
			
			connectionsData += "<td>"+(data.items[i].emails == undefined ? '' : data.items[i].emails.value)+"</td>";
			if(data.items[i].phoneNumbers != undefined) {
				for(var n=0; n<data.items[i].phoneNumbers.length; n++) {
					if(data.items[i].phoneNumbers[n].type == 'MOBILE')
						connectionsData += "<td>"+data.items[i].phoneNumbers[n].value+"</td>";
				}
			}
			connectionsData += "<td>"+(data.items[i].organizations == undefined ? '' : data.items[i].organizations.department)+"</td>";
			connectionsData += "<td>"+(data.items[i].organizations == undefined ? '' : data.items[i].organizations.employeeNumber)+"</td>";
			connectionsData += "</tr>";
			
		}
		connectionsData += "</tbody>";
		connectionsData += "</table><p>";
		
		$("#connectionsData").html(connectionsData);
	});
	
};

displayConnectionListNames = function(data) {
	var connectionList="";
	var connectionListNamesUrl = WC.getLinkAttrByKeyValue(data.links,"resourceType", connectionListNamesURN,"href");
	WC.getData(connectionListNamesUrl, function(data) {
		var items = data.items;
		
		connectionList += "<select value='' id='' onchange='selectConnList(this)'>";
		connectionList += "<option value='select'>����</option>";
		for(var i=0; i<items.length; i++) {
			connectionList += "<option value='"+WC.getLinkAttrByKeyValue(items[i].links,"rel", connectionsByListURN,"href")+"'>"+items[i].name+"</option>\n";
		}
		connectionList += "</select>";
		
		$("#connectionList").html(connectionList);
		
	});
}

// accepted�� ignored �ܿ� declined�� �ȵ�. (status ���� �ٸ��ǰ�?)
invitaionAct = function(invitationAct, invitationActUrl, invitationId) {
	
	var body = "{\"id\":\""+invitationId+"\",\"status\":\""+invitationAct+"\"}";

	WC.actionData(invitationActUrl, 'PUT', body, function(data){
		JSON.stringify(data);
	});
};

displayPendingInvitees = function(data) {
	var pendingInvitations = "Pending Invitations";
	
	var items = data.items;
	var invitationActUrl = "";
	
	if(items.length > 0) {
		pendingInvitations += "<table border='1'>";
		pendingInvitations += "<thead>";
		pendingInvitations += "<th>INVITATION ID</th>";
		pendingInvitations += "<th>USER ID</th>";
		pendingInvitations += "<th>DISPLAY NAME</th>";
		pendingInvitations += "<th>GUID</th>";
		pendingInvitations += "<th>Message</th>";
		pendingInvitations += "<th>send Date</th>";
		pendingInvitations += "<th>Status</th>";
		pendingInvitations += "<th>Action</th>";
		pendingInvitations += "</thead>";
		pendingInvitations += "<tbody>";
		
		for(var i=0; i<items.length; i++) {
			pendingInvitations += "<tr>";
			pendingInvitations += "<td>"+items[i].id+"</td>";
			pendingInvitations += "<td>"+items[i].invitor.id+"</td>";
			pendingInvitations += "<td>"+items[i].invitor.displayName+"</td>";
			pendingInvitations += "<td>"+items[i].invitor.guid+"</td>";
			pendingInvitations += "<td>"+items[i].message+"</td>";
			pendingInvitations += "<td>"+items[i].sentDate+"</td>";
			pendingInvitations += "<td>"+items[i].status+"</td>";
			invitationActUrl = WC.getLinkAttrByKeyValue(items[i].links,"resourceType", "urn:oracle:webcenter:people:invitation","href");
			pendingInvitations += "<td><input type=\"button\" id=\"accepted\" name=\"accepted\" value=\"accepted\" onclick=\"invitaionAct('accepted','"+invitationActUrl+"','"+items[i].id+"')\" /><input type=\"button\" id=\"declined\" value=\"declined\" onclick=\"invitaionAct('declined','"+invitationActUrl+"','"+items[i].id+"')\" /><input type=\"button\" id=\"ignored\" value=\"ignored\" onclick=\"invitaionAct('ignored','"+invitationActUrl+"','"+items[i].id+"')\" /></td>";
			pendingInvitations += "</tr>";
		}
		
		pendingInvitations += "</tbody>";
		pendingInvitations += "</table><p>";
		
		$("#pendingInvitaions").html(pendingInvitations);
	}
};

// Connection Category �� �����´�. 
selectConnList = function(obj) {
	var connectionsByLists = "";
	var connectionsByListsUrl = obj.options[obj.selectedIndex].value;
	
	if(connectionsByListsUrl != 'select') {
		WC.getData(connectionsByListsUrl, function(data) {
			var items = data.items;
			if(items.length > 0) {
				connectionsByLists += "<table border='1'>";
				connectionsByLists += "<thead>";
				connectionsByLists += "<th>ID</th>";
				connectionsByLists += "<th>DISPLAY NAME</th>";
				connectionsByLists += "<th>GUID</th>";
				connectionsByLists += "<th>EMAIL</th>";
				connectionsByLists += "<th>MOBILE</th>";
				connectionsByLists += "<th>DEPARTMENT</th>";
				connectionsByLists += "<th>EMPLOYEENUMBER</th>";
				connectionsByLists += "</thead>";
				connectionsByLists += "<tbody>";
				
				for(var i=0; i<items.length; i++) {
					connectionsByLists += "<tr>";
					connectionsByLists += "<td>"+items[i].id+"</td>";
					connectionsByLists += "<td>"+items[i].displayName+"</td>";
					connectionsByLists += "<td>"+items[i].guid+"</td>";
					connectionsByLists += "<td>"+items[i].emails.value+"</td>";
					for(var n=0; n<items[i].phoneNumbers.length; n++) {
						if(items[i].phoneNumbers[n].type == 'MOBILE')
							connectionsByLists += "<td>"+items[i].phoneNumbers[n].value+"</td>";
					}
					connectionsByLists += "<td>"+items[i].organizations.department+"</td>";
					connectionsByLists += "<td>"+items[i].organizations.employeeNumber+"</td>";
					connectionsByLists += "</tr>";
				}

			} else {
				connectionsByLists += "<tr>";
				connectionsByLists += "<td colspan='7'>no data found...</td>";
				connectionsByLists += "</tr>";
			}
			
			connectionsByLists += "</tbody>";
			connectionsByLists += "</table><p>";
			
			$("#connectionsByList").html(connectionsByLists);
		});
	}
}

// Invitaion Message�� �����Ѵ�. 
// WebCenter Profile Connection ������ Connection List�� ������ �� ������ �� �ִ�.
// REST �� Connection List�� ���� �� �޽����� �����Ͽ� ������ �����ұ�?
sendInvitation = function(invitee, inviteMessage) {
	var data = WC.resourceIndex;
	//invitee �� guid �� ���´�.
	var personURM = "urn:oracle:webcenter:people:person";
	var peopleReouserURLTemplate = WC.getLinkAttrByKeyValue(data.links,"resourceType", personURN,"template");
	
	var query = "loginid:equals:"+invitee;
	
	var personUrlTemplate = new UrlTemplate(peopleReouserURLTemplate);
	
	var personUrl = personUrlTemplate.toUrl({
		'q' : query,
		'data' : 'basic'
	});
	
	WC.getResourceData(personUrl, personURN, false, function(data) {
		// resource URL�� �̿��� �񵿱� ȣ���Ͽ� Data ȹ���Ѵ�.
		var user_guid = data.guid;
		var user_displayName = data.displayName;
		var inviteData = "{\"message\":\""+user_displayName+", do you want to join my connection lists\", \"guid\":\""+user_guid+"\"}";	//{"message":"Monty, do you want to join my connections list?","guid":"1AE5AF102E2611E09F062B573E287934"}
		
		WC.actionData(inviteUrl, 'POST', inviteData, function(data){
			//JSON.stringify(data.message);
		});
	});
}