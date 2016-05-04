searchURN = "urn:oracle:webcenter:search:results";
var startIndex = 0;
var itemsPerPage = 50;

searchInitialize = function(data) {
	getSearchData(data);
};

getSearchData = function(data) {
	var resourceURL = WC.getLinkAttrByKeyValue(data.links,"resourceType", searchURN,"template");
	
	resourceURL = resourceURL.replace('&serviceId={serviceId}', '');
	resourceURL = resourceURL.replace('&refiners={refiners}', '');
	resourceURL = resourceURL.replace('&scopeGuid={scopeGuid}', '');
	resourceURL = resourceURL.replace('{q}', 'webcenter');
	
	var searchUrlTemplate = new UrlTemplate(WC.getLinkAttrByKeyValue(data.links,"resourceType", searchURN,"template"));
	
	var searchTemplateUrl = searchUrlTemplate.toUrl({
		'serviceId' : '',
		'refiners' : '',
		'scopeGuid' : '',
		'q' : 'webcenter',
		'startIndex' : startIndex,
		'itemsPerPage' : itemsPerPage
	});
	
	WC.getData(searchTemplateUrl, getSearchDataCallback);	// resource URL을 이용해 비동기 호출하여 Data 획득한다.
};

getSearchDataCallback = function(data) {
	
	resourceData = "<br>============================================================================================================<br>";
	//resourceData += "<b>- peopleServiceURN :</b> "+peopleServiceURN+"<br>";
	
	appendObjectChildData(data, 0);
	
	$("#searchList").html(resourceData);
	
}