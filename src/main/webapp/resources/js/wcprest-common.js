var WC = new WebCenter();

function WebCenter() {
	var self = this;
	this.data = new Map();
	this.resourceIndexUrl = '/rest/api/resourceIndex';
	this.resourceIndex = null;
	this.username="weblogic";
	//this.password="welcome1";
	
	$.ajaxSetup({
		username: this.username, 
		password: this.password,
	    global: false,
	    cache:false
	});
	/*
	this.setAuth = function(username,password) {
		this.username = username;
		this.password = password;
	};
	*/
	// data�� �����´�. urn�� Ű�� �Ͽ� �����Ѵ�. (cache)
	this.getResourceData = function(url, urn, cache, callback) {
		if(this.getMapData(urn) == undefined) {
			$.ajax({
				url: url,
				dataType: "json",
				contentType : 'application/json',
				success: function(data) {
					if(cache)
						WC.data.put(urn, data);	//caching...
					callback(data);
					console.log(data);
				},
				//beforeSend:showRequest,
			    error:function(e){
			        alert(e.responseText);  
			    }
			});
		} else {
			if(cache)
				callback(WC.getMapData(urn));	// get cache data...
		}
	};
	
	// data�� �����´�. 
	this.getData = function(url, callback) {
		$.ajax({
			url: url,
			dataType: "json",
			contentType : 'application/json',
			success: function(data) {
				callback(data);
				console.log(data);
			},
			//beforeSend:showRequest,
		    error:function(e){
		        alert(e.responseText);  
		    }
		});
	};
	
	// AJAX�� PUT/DELETE/UPDATE �Ѵ�.
	// data �� JSON ��������...
	this.actionData = function(url, type, data, callback) {
		$.ajax({
			type : type,
			url : url,
			dataType : 'json',
			contentType : 'application/json',
			Accept : 'application/json',
			data : data,
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Authorization", "Basic " + $.base64('encode', this.username+':'+this.password));
			},
			success : callback,
			error:function(xhr, error){
		        if(xhr.status == 0){
					alert('You are offline!!n Please Check Your Network.');
				}else if(xhr.status == 404){
					alert('Requested URL not found.');
				}else if(xhr.status == 500){
					alert('Internel Server Error.');
				}else if(error == 'parsererror'){
					alert('Error.nParsing JSON Request failed.');
				}else if(error == 'timeout'){
					alert('Request Time out.');
				}else {
					alert('Unknow Error.n'+xhr.responseText);
				}
		    }
		});
	};
	
	// resourceIndex ������ ȹ���Ѵ�.
	// �ѹ� ������ resourceIndex�� WebCenter ��ü�� �����Ѵ�. (cache)
	this.getResourceIndex = function(callback) {
		if(self.resourceIndex == null) {
			$.ajax({
				url: WC.resourceIndexUrl,
				dataType: "json",
				contentType : 'application/json',
				success: function(data) {
					self.resourceIndex = data;
					callback(data);
				},
				//beforeSend:showRequest,
			    error:function(e){
			        alert(e.responseText);  
			    }
			});
		} else {
			callback(self.resourceIndex);
		};
	};
	
	// links ���� key(e.g. resourceTyp)�� attribute(href, template)�� �̿��� ���õ� url�� �����´�.
	this.getLinkAttrByKeyValue = function(links, key, value, attr) {
		if(links == null) links = self.resourceIndex.links;
		for ( var i = 0; i < links.length; i++) {
			if (links[i][key] == value) {
				return links[i][attr];
			}
		}
		return null;
	};
	
	// Map�� �ִ� �����͸� ��ȯ (key : urn)
	this.getMapData = function(urn) {
		return WC.data.get(urn);
	};
	
	// Map�� �ִ� ������ ���� (key : urn)
	this.clearMapData = function() {
		return WC.data.clear();
	};
	
	// Map�� �ִ� ������ ���� (key : urn)
	this.removeMapData = function(urn) {
		return WC.data.remove(urn);
	};
};

// template url�� �и��Ͽ� �����ϴ� ��ü (base url, fixed, keys)
UrlTemplate = function(url) {
	var half = url.split('?');
	this.base = half[0];
	this.fixed = [];
	this.keys = [];
	if (half.length != 1) {
		var params = half[1].split('&');
		// loop through params and divide them into fixed and those of the form
		// foo={foo}
		for ( var i = 0; i < params.length; i++) {
			var arg = params[i];
			if (arg.match(/\{.*?\}/)) {
				this.keys.push(arg.split(/=/)[0]);
			} else {
				this.fixed.push(arg);
			}
		}
	}
	
	// args �� json ���·� �ۼ��Ǹ�, key�� �ش��ϴ� parameter key�� ���� ��ȯ�Ѵ�.
	// e.g) {key1, value1} => ?key1={key1} =? key1=value1
	this.toUrl = function(args) {
		var url = this.base;
		var params = [];
		if (args) {
			for ( var key in args) {
				params.push(key + '=' + args[key]);
			}
		}
		params = params.concat(this.fixed);
		if (params.length) {
			url += '?' + params.join('&');
		}
		return (url);
	};
};
