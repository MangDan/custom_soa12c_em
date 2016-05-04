spacesURN = "urn:oracle:webcenter:spaces";
var startIndex = 0;
var itemsPerPage = 50;
var visible = 'joined';
var utoken;
var space_rest_url;
var serverurl;

spacesInitialize = function(data) {
	getSpacesData(data);
};

getSpacesData = function(data) {
	var resourceURL = WC.getLinkAttrByKeyValue(data.links,"resourceType", spacesURN,"href");
	
	var spacesUrlTemplate = new UrlTemplate(WC.getLinkAttrByKeyValue(data.links,"resourceType", spacesURN,"template"));
	
	var spacesUrlTemplateUrl = spacesUrlTemplate.toUrl({
		'startIndex' : startIndex,
		'itemsPerPage' : itemsPerPage,
		'visibility' : visible
	});
	
	serverurl = resourceURL.substring(0, resourceURL.indexOf("rest"));
	space_rest_url = resourceURL.substring(0, resourceURL.indexOf("?"));
	utoken = resourceURL.substring(resourceURL.indexOf("utoken"), resourceURL.length);
	
	WC.getData(spacesUrlTemplateUrl, getSpacesResourceCallback);	// resource URL
	//WC.getData(space_rest_url+"/"+portal_id+"/members?"+utoken, getMembers);
};

getMembers = function(data) {
	var items = data.items;
	var role = "";
	var joined_member = "";
	for ( var i = 0; i < items.length; i++) {
		var given_roles = "";
		items[i].displayName;
		items[i].status;
		
		role = items[i].role;
		for (var r = 0; r < role.length; r++) {
			given_roles += role[r] + ",";
		}
		
		given_roles = " [given roles : <b>"+given_roles.substring(0,given_roles.length-1)+"</b>]";
		
		joined_member += "<b>"+items[i].displayName+"</b> joined <b>"+portal_id +"</b>"+ given_roles  + "<br>";
	}
	
	$("#res_message").html(joined_member);	
};

addBulkUGMember = function(target, new_u_g_members) {
	var count=0;
	var url="";
	var portal_id="";
	var new_u_g_member="";
	var bulk_u_res_obj = $("#bulk_u_g_res_message");
	var bulk_u_err_obj = $("#bulk_u_g_err_message");
	var res_u_g_message = "";
	var res_u_g_err_message = "";
	
	var memberarr = new_u_g_members.split('\n');
	for(var i=0; i<memberarr.length; i++) {
		portal_id = memberarr[i].substring(0,memberarr[i].indexOf('{'));
		new_u_g_member = memberarr[i].substring(memberarr[i].indexOf('{'), memberarr[i].length);
		
		url = serverurl + "groupspacews/rest/api/space/member/"+portal_id+"/"+target;
		
		WC.actionData(url, "POST", new_u_g_member, function(data) {
			//var res_u_g_message = "";
			//bulk_u_res_obj.html(JSON.stringify(data));
			
			//for(var i=0; i<data.members.length; i++) {
			//	bulk_u_res_obj.html();
			//	res_u_g_message += "member["+i+"] : <b>" + data.members[i].userId + "</b> [Group : " + data.members[i].isGroup + "]" + "[Role : " + data.members[i].spaceRole+"]<br>";
			//}
			
			if(data.error != undefined) {
				res_u_g_err_message += "<font color='red'>" + data.error + "</font><br>----------------------------------------------------------------------------------------------------------------<br>";
				bulk_u_err_obj.html(res_u_g_err_message);
			} else {
				count++;
				var userId="";
				var role="";
				
				for(var j=0; j<data.members.length; j++) {
					userId+=data.members[j].userId+",";
					role+=data.members[j].spaceRole+",";
				}
				
				res_u_g_message += count + ". " + userId.substring(0,userId.length-1) + " with "+role.substring(0,role.length-1)+" added to "+ portal_id + "<br>";
				bulk_u_res_obj.html(res_u_g_message);
			}
		});
	}
};

addBulkUMember = function(new_u_members) {
	
	var bulk_u_res_obj = $("#bulk_u_res_message");
	
	var portal_id;
	var u_member;
	var role;
	var url;
	var body;
	
	for(var i=0; i<new_u_members.length; i++) {
		portal_id = new_u_members[i].portal;
		u_member = new_u_members[i].member;
		role = new_u_members[i].role;
		url = space_rest_url+"/"+portal_id+"/members?"+utoken;
		
		body = "{\"name\" : \""+u_member+"\",\"role\" : [\""+role+"\"]}";
		
		WC.actionData(url, "POST", body, function(data) {
			//bulk_res_message.append("<b>"+reqdata.name+"</b> added <b>"+portal_id +"</b> [given roles : <b>"+ role  + "]<br>");
			
			var items = data.items;
			var role = "";
			
			var bulk_u_res_message = "";
			for ( var i = 0; i < items.length; i++) {
				var given_roles = "";
				items[i].displayName;
				items[i].status;
				
				role = items[i].role;
				for (var r = 0; r < role.length; r++) {
					given_roles += role[r] + ",";
				}
				
				given_roles = " [given roles : <b>"+given_roles.substring(0,given_roles.length-1)+"</b>]";
				
				bulk_u_res_message += "<b>"+items[i].displayName+"</b> joined <b>"+portal_id +"</b>"+ given_roles  + "<br>";
			}
			
			bulk_u_res_obj.html(bulk_u_res_message);
			
		});
	}
};

addMember = function(portal_id, u_member, role) {
	var url = space_rest_url+"/"+portal_id+"/members?"+utoken;
	var body = "{\"name\" : \""+u_member+"\",\"role\" : [\""+role+"\"]}";
	
	//var items = data.items;
	//for ( var i = 0; i < items.length; i++) {
	//	alert(items.name);
	//}
	
	WC.actionData(url, "POST", body, function(data) {
		var res_u_message = "";
		
		var items = data.items;
		var role = "";
		
		for ( var i = 0; i < items.length; i++) {
			var given_roles = "";
			items[i].displayName;
			items[i].status;
			
			role = items[i].role;
			for (var r = 0; r < role.length; r++) {
				given_roles += role[r] + ",";
			}
			
			given_roles = " [given roles : <b>"+given_roles.substring(0,given_roles.length-1)+"</b>]";
			
			if(u_member == items[i].name)
				res_u_message += "<font color='red'>just added ==> </font><b>"+items[i].displayName+"</b> joined <b>"+portal_id +"</b>"+ given_roles  + "<br>";
			else
				res_u_message += "<b>"+items[i].displayName+"</b> joined <b>"+portal_id +"</b>"+ given_roles  + "<br>";
		}
		
		$("#res_message").html(res_message);
	});
};

getSpacesResourceCallback = function(data) {
	var portalName;
	var portalDisplayName;
	var portalGUID;
	var isPublic;
	var isOffline;
	var portals = "";
	
	resourceData = "<br>=================================== Spaces Resource Data ====================================<br>";
	resourceData += "<b>- spacesURN :</b> "+spacesURN+"<br>";
	
	appendObjectChildData(data, 0);
	
	var items = data.items;
	for ( var i = 0; i < items.length; i++) {
		portalName = items[i].name;
		portalDisplayName = items[i].displayName ;
		portalGUID = items[i].guid;
		isPublic = items[i].isPublic;
		isOffline = items[i].isOffline;
		portals += "portalName : " + portalName + "<br>" + "portalDisplayName : " + portalDisplayName + "<br>" + "portalGUID : " + portalGUID + "<br>" + "isPublic : " + isPublic + "<br>" + "isOffline : " + isOffline + "<p>";
	}

	$("#joined_portals").html(portals);
	$("#spacesResourceData").html(resourceData);
};