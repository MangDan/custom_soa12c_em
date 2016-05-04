/**
 * 문자열 트림
 */
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 문자열 변환
 */
String.prototype.replaceAll = function(oldWord, newWord) { 
	return this.replace(new RegExp(oldWord, "g"), newWord); 
}

/**
 * Basic Common Utility
 */
var CommonUtility = {
	
	/**
	 * 객체 위치 조회
	 */
	getObjectPosition: function(obj, pos_type) {
		if(obj.offsetParent == document.body)
			return (pos_type == "top") ? obj.offsetTop : obj.offsetLeft;
		else
			return ((pos_type == "top") ? obj.offsetTop : obj.offsetLeft) + getObjectPosition(obj.offsetParent, pos_type);
	},
	
	/**
	 * 상위 태그 조회
	 */
	getParentByTagName: function(srcObj, tagName) {
		if (srcObj == null || tagName == null) return null;
		var parentObj = (srcObj.parentNode) ? srcObj.parentNode : srcObj.parentElement;
		return (parentObj != null && parentObj.tagName == tagName) ? parentObj :  getParentByTagName(parentObj, tagName);
	},
	
	/**
	 * 윈도우 오픈
	 */
	win: null,
	openWindow: function(mypage,myname,w,h,scroll,resize) {

		var winl = (screen.width-w)/2;
		var wint = (screen.height-h)/2;
		var settings  ='height='+h+',';
			settings +='width='+w+',';
			settings +='top='+wint+',';
			settings +='left='+winl+',';
			settings +='scrollbars='+scroll+',';
			settings +='resizable='+resize+',';
			settings +='menubar=no,status=no';
		this.win = window.open(mypage,myname,settings);
		if(parseInt(navigator.appVersion) >= 4) this.win.window.focus();
	}

}

/**
 * XML Utility
 */
var XMLUtility = {
	
	/**
	 * XML Document 객체 초기화
	 */
	initiateXMLDocument: function(type, param) {
		var xmlDocument;
	
		if(type == "url") {
			xmlDocument = this.createXMLDocument();
			xmlDocument.async=false;
			xmlDocument.load(param);
		} else {
			if(window.ActiveXObject) {
				//Code for internet explorer
				xmlDocument = new ActiveXObject("Microsoft.XMLDOM");
				xmlDocument.async=false;
				xmlDocument.loadXML(param);
			} else if(window.DOMParser) {
				//Code for mozilla
				var parser = new DOMParser();
				xmlDocument = parser.parseFromString(param,"text/xml");
			} else {
				//Unknown browser
				window.alert("Browser does not support XML parsing.");
				return false;
			}
		}
		
		// check for XPath implementation
		if(document.implementation && document.implementation.hasFeature("XPath", "3.0")) {
			// prototying the XMLDocument
			XMLDocument.prototype.selectNodes = function(cXPathString, xNode) {
				if( !xNode ) xNode = this;
				var oNSResolver = this.createNSResolver(this.documentElement);
				var aItems = this.evaluate(cXPathString, xNode, oNSResolver, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
				var aResult = [];
				for( var i = 0; i < aItems.snapshotLength; i++) {
					aResult[i] =  aItems.snapshotItem(i);
				}
				return aResult;
			}
		
			// prototying the Element
			Element.prototype.selectNodes = function(cXPathString) {
			     if(this.ownerDocument.selectNodes) {
			    	 return this.ownerDocument.selectNodes(cXPathString, this);
			     }
			     else{throw "For XML Elements Only";}
			}
	
			// prototying the XMLDocument
			XMLDocument.prototype.selectSingleNode = function(cXPathString, xNode) {
				if( !xNode ) { xNode = this; }
				var xItems = this.selectNodes(cXPathString, xNode);
				if( xItems.length > 0 ) return xItems[0];
			    else return null;
			}
	
			// prototying the Element
			Element.prototype.selectSingleNode = function(cXPathString) {    
				if(this.ownerDocument.selectSingleNode) {
					return this.ownerDocument.selectSingleNode(cXPathString, this);
				}
				else{throw "For XML Elements Only";}
			}
		}
		
		return xmlDocument;
	},
	
	/**
	 * XSL Document 객체 초기화
	 */
	initiateXSLDocument: function(type, param) {
		var xslDocument;

		if(type == "url") {
			xslDocument = this.createXSLDocument();
			xslDocument.async=false;
			xslDocument.load(param);
		} else {
			if(window.ActiveXObject) {
				//Code for internet explorer
				xslDocument = new ActiveXObject("MSXML2.FreeThreadedDomDocument.3.0");
				xslDocument.async=false;
				xslDocument.loadXML(param);
			} else if(window.DOMParser) {
				//Code for mozilla
				var parser = new DOMParser();
				xslDocument = parser.parseFromString(param,"text/xml");
			} else {
				//Unknown browser
				window.alert("Browser does not support XSL parsing.");
				return false;
			}
		}
		
		return xslDocument;
	},

	/**
	 * XML, XSL Document 객체 변환
	 */
	xmlTransformProcess: function(xml, xsl, params) {
		if (window.ActiveXObject) {
			//Code for internet explorer
			var template = new ActiveXObject("MSXML2.XSLTemplate");
			template.stylesheet = xsl;

			var processor = template.createProcessor();
			processor.input = xml;
			
			if(params != null)
				for(var paramIdx = 0; paramIdx < params.length; paramIdx++)
					processor.addParameter("param_"+paramIdx, params[paramIdx]);
				
			processor.transform();
			
			return processor.output;
		} else if (document.implementation && document.implementation.createDocument) {
			//Code for mozilla
			var xsltProcessor= new XSLTProcessor();
			xsltProcessor.importStylesheet(xsl);

			if(params != null)
				for(var paramIdx = 0; paramIdx < params.length; paramIdx++)
					xsltProcessor.setParameter(null, "param_"+paramIdx, params[paramIdx]);
			
			return xsltProcessor.transformToFragment(xml,document);
		} else {
			//Unknown browser
			window.alert("Browser does not support XSLT.");
			return false;
		}
	},
	
	/**
	 * XML Document 객체 생성
	 */
	createXMLDocument: function() {
		return	(window.ActiveXObject)	? new ActiveXObject("Microsoft.XMLDOM") : //Code for internet explorer
				(document.implementation && document.implementation.createDocument) ? document.implementation.createDocument("","",null) : //Code for mozilla
				null; //Unknown browser
	},
	
	/**
	 * XSL Document 객체 생성
	 */
	createXSLDocument: function() {
		return	(window.ActiveXObject)	? new ActiveXObject("MSXML2.FreeThreadedDomDocument.3.0") : //Code for internet explorer
				(document.implementation && document.implementation.createDocument) ? document.implementation.createDocument("","",null) : //Code for mozilla
				null; //Unknown browser
	}
	
}

/**
 * Ajax Utility
 */
var AjaxUtility = {
	/**
	 * XML Request 객체 생성
	 */
	createXMLHttpRequest: function() {
		return	(window.ActiveXObject)	? new ActiveXObject("Microsoft.XMLHTTP") : //Code for internet explorer
				(window.XMLHttpRequest)	? new XMLHttpRequest() : //Code for mozilla
				null; //Unknown browser
	},

	/**
	 * 텍스트 반환 요청 
	 */
	getRequestText: function(method, url, asynchronous) {
		var xmlHttpRequest = this.createXMLHttpRequest();     
		xmlHttpRequest.open(method, url, asynchronous);
		xmlHttpRequest.send('');

		if(xmlHttpRequest.readyState == 4) {
			if(xmlHttpRequest.status == 200) {     
				var xmlResponseText = xmlHttpRequest.responseText;                      
				return xmlResponseText;             
			}
		}            
	},

	/**
	 * XML Document  반환 요청 
	 */
	getRequestXML: function(method, url, asynchronous) {
		var xmlHttpRequest = this.createXMLHttpRequest();     
		xmlHttpRequest.open(method, url, asynchronous);
		xmlHttpRequest.send('');
		if(xmlHttpRequest.readyState == 4) {
			if(xmlHttpRequest.status == 200) {
				var xmlResponseDocument = xmlHttpRequest.responseXML;             
				return xmlResponseDocument;             
			}
		}
	},
	
	/**
	 * Ajax 객체의 이벤트 처리 메서드(prototype.js)
	 */
	successResponse: function(originalRequest) {},
	failureResponse: function(originalRequest) {},
	completeResponse: function(originalRequest) {},

	/**
	 * 레이어에 URI에서 반환되는 HTML을 삽입(prototype,js 필요)
	 */
	loadContext: function(uri,layerKey,query) {
	
		var req = new Ajax.Updater (
			{success : layerKey},
			uri,
			{
				method: 'post', 
				parameters: query,
				onSuccess : this.successResponse,
				onFailure : this.failureResponse,
				onComplete : this.completeResponse,
				asynchronous : true,
				evalScripts : true
			}
		);
	}
}

/**
 * Cipher(encript/decript) Utility
 */
var CipherUtility = {
		
}

/**
 * Base 64 Utility
 */
var Base64Utility = {
	keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

	/**
	 * Base 64 인코딩
	 */
	encode64: function(input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;

		do {
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);

			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;

			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}

			output = output + this.keyStr.charAt(enc1) + this.keyStr.charAt(enc2) + this.keyStr.charAt(enc3) + this.keyStr.charAt(enc4);
		} while (i < input.length);

		return output;
	},

	/**
	 * Base 64 디코딩
	 */
	decode64: function(input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;

		// remove all characters that are not A-Z, a-z, 0-9, +, /, or =
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

		do {
			enc1 = this.keyStr.indexOf(input.charAt(i++));
			enc2 = this.keyStr.indexOf(input.charAt(i++));
			enc3 = this.keyStr.indexOf(input.charAt(i++));
			enc4 = this.keyStr.indexOf(input.charAt(i++));

			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;

			output = output + String.fromCharCode(chr1);

			if (enc3 != 64)
				output = output + String.fromCharCode(chr2);
			if (enc4 != 64)
				output = output + String.fromCharCode(chr3);

		} while (i < input.length);

		return output;
	},

	/**
	 * 한글 Base 64 인코딩
	 */
	encode64Han: function(str) {
		return encode64(escape(str))
	},

	/**
	 * 한글 Base 64 디코딩
	 */
	decode64Han: function(str) {
		return unescape(decode64(str))
	}
}

/**
 * Calendar Utility
 */
var CalendarUtility = {
	input_target: null,
	calendar_position_top: null,
	calendar_position_left: null,
	calendar_popup: window.createPopup(),

	/*
		target_obj value type : yyyy-MM-dd
	*/
	calendar_yyyymmdd: function(target_obj) {
		
		var pre_date = target_obj.value.split("-");
		
		input_target = target_obj;
		calendar_position_top	= document.body.clientTop	+ CalendarUtility.getObjectPosition(target_obj, "top")	- document.body.scrollTop;
		calendar_position_left	= document.body.clientLeft	+ CalendarUtility.getObjectPosition(target_obj, "left")	- document.body.scrollLeft;

		if(pre_date.length == 3) {
			CalendarUtility.display_calendar(pre_date[0],pre_date[1],pre_date[2]);					
		} else {
			var curr_date = new Date();
			CalendarUtility.display_calendar(curr_date.getFullYear(), curr_date.getMonth()+1, curr_date.getDate());
		}
	},

	display_calendar: function(sYear, sMonth, sDay) {
//			var month_days = new Array(0,31,28,31,30,31,30,31,31,30,31,30,31)
		var month_values = new Array("01","02","03","04","05","06","07","08","09","10","11","12");
		var iYear = parseInt(sYear,10), iMonth = parseInt(sMonth,10), iDay = parseInt(sDay,10);

		var indexDate = new Date(iYear, iMonth-1, 1);
		
		var selected_date = new Date(iYear, iMonth-1, iDay);
		
//			if((iYear % 4 == 0 && iYear % 100 != 0) || iYear % 400 == 0)
//				month_days[2] = 29;

		var calendar_html = "";
		calendar_html += "<html><body>";
		calendar_html += "<form name='f_calendar'>";
		calendar_html += "<table id='calendar_table' border='0' bgcolor='#f4f4f4' cellpadding='1' cellspacing='1' width='100%' onmouseover='parent.CalendarUtility.do_over(window.event.srcElement)' onmouseout='parent.CalendarUtility.do_out(window.event.srcElement)' style='font-size:12;font-family:굴림;'>";
		calendar_html += "<tr height='35' align='center' bgcolor='#f4f4f4'>";
		calendar_html += "<td colspan='7' align='center'>";
		calendar_html += "	<select name='selectYear' style='font-size:11;' onchange='parent.CalendarUtility.display_calendar(f_calendar.selectYear.value, f_calendar.selectMonth.value, "+iDay+")';>";
		for (var optYearIdx = iYear-5; optYearIdx < iYear+6; optYearIdx++)
			calendar_html += "	<option value='"+optYearIdx+"' " + ((optYearIdx == iYear) ? " selected" : "") + ">"+optYearIdx+"</option>";
		calendar_html += "	</select>";
		calendar_html += "&nbsp;&nbsp;&nbsp;<a style='cursor:hand;' onclick='parent.CalendarUtility.display_calendar("+((iMonth == 1) ? iYear-1 : iYear)+","+((iMonth == 1) ? 12 : iMonth-1)+","+iDay+");'>◀</a> ";
		calendar_html += "<select name='selectMonth' style='font-size:11;' onchange='parent.CalendarUtility.display_calendar(f_calendar.selectYear.value, f_calendar.selectMonth.value, "+iDay+")';>";
		for (var monthIdx=0; monthIdx<12; monthIdx++)
			calendar_html += "	<option value='"+month_values[monthIdx]+"' "+ ((iMonth == parseInt(month_values[monthIdx],10)) ? " selected" : "") + ">"+month_values[monthIdx]+"</option>";
		calendar_html += "	</select>";
		calendar_html += "&nbsp;<a style='cursor:hand;' onclick='parent.CalendarUtility.display_calendar("+((iMonth == 12) ? iYear+1 : iYear)+","+((iMonth == 12) ? 1 : iMonth+1)+","+iDay+");'>▶</a>";
		calendar_html += "</td></tr>";
		calendar_html += "<tr align=center bgcolor='#87B3D6' style='color:#2065DA;' height='25'>";
		calendar_html += "	<td style='padding-top:3px;' width='24'><font color='black'>일</font></td>";
		calendar_html += "	<td style='padding-top:3px;' width='24'><font color='black'>월</font></td>";
		calendar_html += "	<td style='padding-top:3px;' width='24'><font color='black'>화</font></td>";
		calendar_html += "	<td style='padding-top:3px;' width='24'><font color='black'>수</font></td>";
		calendar_html += "	<td style='padding-top:3px;' width='24'><font color='black'>목</font></td>";
		calendar_html += "	<td style='padding-top:3px;' width='24'><font color='black'>금</font></td>";
		calendar_html += "	<td style='padding-top:3px;' width='24'><font color='black'>토</font></td>";
		calendar_html += "</tr>";


		indexDate.setDate(indexDate.getDate()-indexDate.getDay());

		while(true) {
			if(indexDate.getDay() == 0)
				calendar_html += "<tr height='24' align=right bgcolor='white'>"
			
			var indexTitle = indexDate.getFullYear() +"-"+ ((indexDate.getMonth()+1 < 10) ? "0"+(indexDate.getMonth()+1) :indexDate.getMonth()+1) +"-"+ ((indexDate.getDate() < 10) ? "0"+indexDate.getDate() :indexDate.getDate());
			var indexStyle = "cursor:hand;border:1px solid white;color:"+((indexDate.getDay() == 0) ? "red" : (indexDate.getDay() == 6) ? "blue" : "black")+";";
			
			calendar_html += "<td onclick='parent.CalendarUtility.select_date(this);' title='"+indexTitle+"' style='"+indexStyle+"'>"+indexDate.getDate()+"</td>";
			
			if(indexDate.getDay() == 6)
				calendar_html += "</tr>";
			
			indexDate.setDate(indexDate.getDate()+1);

			if(indexDate.getMonth()+1 > iMonth && indexDate.getDay() == 0) break;
			if(iMonth == 12 && indexDate.getMonth()+1 == 1 && indexDate.getDay() == 0) break;
		}

		calendar_html += "</table></form></body></html>";

		var calendar_popup_body = this.calendar_popup.document.body;
		calendar_popup_body.style.backgroundColor = "lightyellow";
		calendar_popup_body.style.border = "solid black 1px";
		calendar_popup_body.innerHTML = calendar_html;

		var calendar_height = this.calendar_popup.document.getElementById("calendar_table").offsetHeight;
		calendar_height = 214;
		
		this.calendar_popup.show(calendar_position_left, calendar_position_top+input_target.offsetHeight, 170, calendar_height, document.body);
	},

	do_over: function(el) {
		if (el.title.length > 7) {
			el.style.borderColor = "#FF0000";
		}
	},

	do_out: function(el) {
		if (el.title.length > 7) {
			el.style.borderColor = "#FFFFFF";
		}
	},

	select_date: function(e) {
		input_target.value = e.title
		this.calendar_popup.hide();
	},

	getObjectPosition: function(obj, pos_type) {
		if(obj.offsetParent == document.body)
			return (pos_type == "top") ? obj.offsetTop : obj.offsetLeft;
		else
			return ((pos_type == "top") ? obj.offsetTop : obj.offsetLeft) + CalendarUtility.getObjectPosition(obj.offsetParent, pos_type);
	}
}

function resizeLeft(){
	var leftpage = document.getElementById("importLeftPage");
	var resizediv = document.getElementById("resizeLeftDiv");
	
	if(leftpage.style.display == "none"){
		leftpage.style.display = "inline";
	}else{
		leftpage.style.display = "none";
	}
}