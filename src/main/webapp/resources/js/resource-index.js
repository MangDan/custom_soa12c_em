displayResourceIndex = function (displayResourceIndex) {
	var resourceIndex = "";
	resourceIndex = "version : " + displayResourceIndex.version;
	resourceIndex += "<br>resourceType : " + displayResourceIndex.resourceType;
	resourceIndex += "<br><br>=================================================================================";
	
	$.each(displayResourceIndex.links, function(key1, value1) {
		resourceIndex += "<br><br>links["+key1+"]";
		
		$.each(value1, function(key2, value2) {
			resourceIndex += "<br><b>"+key2 + " :</b> " + value2;
		});
	});
	
	//alert(WC.getLinkAttrByKeyValue("resourceType", "urn:oracle:webcenter:spaces:resource:templates", "template"));
	
	$("#resourceIndex").html(resourceIndex);
	//$("#resourceIndex").html(JSON.stringify(WC.resourceIndex));
};