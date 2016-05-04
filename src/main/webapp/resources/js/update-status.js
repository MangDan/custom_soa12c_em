var peopleServiceURN = "urn:oracle:webcenter:people";
var updateStatusURN = "urn:oracle:webcenter:people:person:status";
var profileURN = "urn:oracle:webcenter:spaces:profile";

peopleServiceInitialize = function(data) {
	getPeopleServiceData(data);
};

getPeopleServiceData = function(data) {
	var resourceURL = WC.getLinkAttrByKeyValue(data.links,"resourceType", peopleServiceURN,"href");
	WC.getResourceData(resourceURL, peopleServiceURN, true, displayPeopleService);	// resource URL을 이용해 비동기 호출하여 Data 획득한다.
};

// people data...
displayPeopleService = function(data) {
	resourceData = "<br>============================================================================================================<br>";
	resourceData += "<b>- peopleServiceURN :</b> "+peopleServiceURN+"<br>";
	
	appendObjectChildData(data, 0);
	
	$("#peopleServiceData").html(resourceData);
};

// 상태 업데이트를 위해 버튼 클릭 할 경우 status 값을 받아서 업데이트 함.
// required url : urn:oracle:webcenter:people:person:status 의 href
updateStatus = function(status, callback) {
	var peopledata = WC.getMapData(peopleServiceURN);
	var resourceURL = WC.getLinkAttrByKeyValue(peopledata.links, "resourceType", updateStatusURN,"href");

	var statusMessage = '{"note": "' + status.replace(/\"/g, "\\\"") + '"}';
	
	// put을 위한 ResourceURL 은 획득한다 (href, template 은 없음, 단지 상태 없데이트만 수행)
	// AJAX를 통해 PUT을 한다.
	WC.actionData(resourceURL, "PUT", statusMessage, callback);
};

// rederStatusPut은 별도로 구현을 한다. (여기서는 임의의 필드에 업데이트한 상태 값을 뿌리는 용도임.)
renderStatusPut = function(result) {
	
	//alert(JSON.stringify(result));
	var peopledata = WC.getMapData(peopleServiceURN);
	var name = peopledata.displayName;
	
	// URL for profile as a web page
	var profileURL = WC.getLinkAttrByKeyValue(peopledata.links, "resourceType", profileURN,"href");
	
	var html = 'Status for <a href="' + profileURL + '" target="_blank">' + name + '</a> is: ' + result.note;
	document.getElementById('results').innerHTML = html;
	
	// 다시 peopleServiceData 정보를 삭제 후 가져온다....
	WC.removeMapData(peopleServiceURN);
	WC.getResourceIndex(peopleServiceInitialize);
	
};