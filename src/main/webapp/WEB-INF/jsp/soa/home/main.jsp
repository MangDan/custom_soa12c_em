<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<noscript>
	<div class="alert alert-block col-md-12">
		<h4 class="alert-heading">Warning!</h4>

		<p>
			You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
				target="_blank">JavaScript</a> enabled to use this site.
		</p>
	</div>
</noscript>
<!-- content starts -->
<div class="box col-md-12" style="padding:0px 0px 0px 0px">
	<div class="box-inner">
		<div class="box-header well" data-original-title="">
			<h2>Composite Instances</h2>
			<!--
			<div class="box-icon">
				<button id="composite-refresh-btn" class="btn btn-inverse btn-default btn-xs">REFRESH</button>
			</div>
			-->
			<div id="refresh-spin1" class="box-icon" style="width:50px;text-align:center;display:none"><i class="fa fa-refresh fa-spin"></i></div>
		</div>
		<div id="soa-composites" class="box-content" style="height:200px;">
		<div style="padding:0px 0px 5px 0px;text-align:right"><strong>Flow ID</strong> : <input type="text" id="SearchFlowId" name="SearchFlowId" value="${param.flowId}"></input>&nbsp;<button id="composite-search-btn" class="btn btn-inverse btn-default btn-xs">SEARCH</button>&nbsp;</div>
		<table class="table table-bordered table-striped table-condensed" style="font-size:11px">
			<thead>
			<tr>
				<th>ID</th>
				<th>COMPOSITE NAME</th>
				<th>TITLE</th>
				<th>STATE</th>
				<th>CREATE DATE</th>
			</tr>
			</thead>
			<tbody>
				<tr>
					<td align="center" colspan="5">no data found.</td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>
</div>
<div class="box col-md-12" style="padding:0px 0px 0px 0px">
	<div class="box-inner">
		<div class="box-header well" data-original-title="">
			<h2>Faults</h2>
			<div class="box-icon">
				<button id="fault-refresh-btn" class="btn btn-inverse btn-default btn-xs">REFRESH</button>
			</div>
			<div id="refresh-spin2" class="box-icon" style="width:50px;text-align:center;display:none"><i class="fa fa-refresh fa-spin"></i></div>
		</div>
		<div id="soa-faults" class="box-content" style="height:200px;">
		<table class="table table-bordered table-striped table-condensed" style="font-size:11px">
			<thead>
			<tr>
				<th>Error Message</th>
				<th>Fault Owner</th>
				<th>Fault Time</th>
				<th>Recovery</th>
			</tr>
			</thead>
			<tbody>
				<tr>
					<td align="center" colspan="4">no data found.</td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>
</div>
<div class="box col-md-12" style="padding:0px 0px 0px 0px">
	<div class="box-inner">
		<div class="box-header well" data-original-title="">
			<h2>Process Instances</h2>
			<!--
			<div class="box-icon">
				<button id="composite-refresh-btn" class="btn btn-inverse btn-default btn-xs">REFRESH</button>
			</div>
			-->
			<div id="refresh-spin3" class="box-icon" style="width:50px;text-align:center;display:none"><i class="fa fa-refresh fa-spin"></i></div>
		</div>
		<div id="bpm-instances" class="box-content" style="height:200px;">
		<div style="padding:0px 0px 5px 0px;text-align:right">
			<strong>Flow ID</strong> : <input type="text" id="SearchFlowId2" name="SearchFlowId" value="${param.flowId}" size="5"></input>&nbsp;
			<strong>Instance ID</strong> : <input type="text" id="SearchInstanceId" name="SearchInstanceId" value="" size="5"></input>&nbsp;
			<strong>Process Name</strong> : <input type="text" id="SearchProcessName" name="SearchProcessName" value="" size="10"></input>&nbsp;
			<strong>State</strong> : <select id="SearchProcessState" style="width:120px">
				<option value="0" selected>running</option>
				<option value="2">completed successfully</option>
				<option value="1">Recovery required</option>
				<option value="3">faulted</option>
				<option value="4">terminated by user</option>
				<option value="5">suspended</option>
				<option value="6">stale</option>
				<option value="7">recovered</option>
			</select>&nbsp;
			<strong>Page</strong> : <select id="SearchProcessPageSize" style="width:50px">
				<option value="5">5</option>
				<option value="10" selected>10</option>
				<option value="20">20</option>
				<option value="50">50</option>
				<option value="100">100</option>
				<option value="500">500</option>
				<option value="1000">1000</option>
			</select>
			<button id="process-search-btn" class="btn btn-inverse btn-default btn-xs">SEARCH</button>&nbsp;
	    </div>
		<table class="table table-bordered table-striped table-condensed" style="font-size:11px">
			<thead>
			<tr>
				<th>Flow ID</th>
				<th>Title</th>
				<th>Number</th>
				<th>Initiator</th>
				<th>Process Name</th>
				<th>Status</th>
			</tr>
			</thead>
			<tbody>
				<tr>
					<td align="center" colspan="6">no data found.</td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		
		//ajax_jquery_loading("<spring:url value="/soa/composite/instance/listbyfilter?partition=default&composite=BPELDBAdapter_SelectEmployees&revision=1.0&flowId=" />", "GET", "#soa-composites","#refresh-spin1", "");
		
		$('#composite-refresh-btn').click(function () {
			var flowId = $("#SearchFlowId").val();
			if(flowId == '') {
				alert('please input flowId!');
				return;
			} else {
				if(flowId == '*') {
					flowId = "";
				}
				ajax_jquery_loading("<spring:url value="/soa/composite/flow/listbyfilter" />?partition=${param.partition}&composite=${param.composite}&revision=${param.revision}&flowId="+flowId, "GET", "#soa-composites","#refresh-spin1", "");
			}
		});
		
		$('#fault-refresh-btn').click(function () {
			var flowId = $("#SearchFlowId").val();
			ajax_jquery_loading("<spring:url value="/soa/composite/faults" />?flowId="+flowId, "GET", "#soa-faults","#refresh-spin2", "");		});
		
		$('#composite-search-btn').click(function () {
			var flowId = $("#SearchFlowId").val();
			if(flowId == '') {
				alert('please input flowId!');
				return;
			} else {
				if(flowId == '*') {
					flowId = "";
				}
				ajax_jquery_loading("<spring:url value="/soa/composite/flow/listbyfilter" />?partition=&composite=&revision=&flowId="+flowId, "GET", "#soa-composites","#refresh-spin1", "");
			}
		});
		
		$('#process-search-btn').click(function () {
			var flowId = $("#SearchFlowId2").val();
			var instanceId = $("#SearchInstanceId").val();
			var componentname = $("#SearchProcessName").val();
			var state = $("#SearchProcessState option:selected").val();
			var pageSize = $("#SearchProcessPageSize option:selected").val();
			
			//if(flowId == '' && instanceId == '' && componentname == '') {
			//	alert('please input search value!');
			//	return;
			//} else {
			//	if(flowId == '*') {
			//		flowId = "";
			//	}
				//flowId=10010&componentInstanceId=bpmn:10020&componentName=SampleProcess&pageSize=0
				ajax_jquery_loading("<spring:url value="soa/component/instance/listbyfilter" />?flowId="+flowId+"&componentInstanceId="+instanceId+"&componentName="+componentname+"&state="+state+"&pageSize="+pageSize, "GET", "#bpm-instances","#refresh-spin3", "");
			//}
		});
	});
	
	getFaults = function(flowId) {
		ajax_jquery_loading("<spring:url value="/soa/composite/faults" />?flowId="+flowId, "GET", "#soa-faults","#refresh-spin2", "");
	}
	
	searchFlow = function(partition, composite, revision) {
		ajax_jquery_loading("<spring:url value="/soa/composite/flow/listbyfilter" />?partition="+partition+"&composite="+composite+"&revision="+revision+"&flowId=", "GET", "#soa-composites","#refresh-spin1", "");
	}
	
	openFaultDetail = function(flowId, faultId)  {
		$('#faultDetailInfo').empty();
		$('#faultDetailAction').modal('show');
		//$('#ModalTracker').css("height", "550px");
		ajax_jquery_loading("<spring:url value="/soa/composite/faultdetail" />?flowId="+flowId+"&faultId="+faultId, "GET", "#faultDetailInfo","", "");
		//ajax_jquery_loading("<spring:url value="/bpm/instance/tracker" />?p_title="+encodeURIComponent(title)+"&p_processdn="+encodeURIComponent(processDn)+"&p_instanceid="+instanceId, "GET", "#faultDetailInfo", "");	
	}
	
	jsonToHtml = function(depth, jsonObject, html) {
	
		for(var i=0; i<jsonObject.length; i++) {
			var flowEntry = jsonObject[i].flowEntry;
			var childFlowEntries = jsonObject[i].childFlowEntries;
			
			//html = indent(depth) + "-" + flowEntry.instanceId + "<br>";
			html = "<tr class=\"treegrid-"+flowEntry.instanceId+(flowEntry.parentInstanceId == undefined ? '' : ' treegrid-parent-'+flowEntry.parentInstanceId)+"\">\n" 	
			+ "<td style=\"font-color:blue;font-size:7px;\">"+flowEntry.entityName+" ["+flowEntry.instanceId+"]</td>\n"
			+ "<td width=\"80\" style=\"font-size:8px;\">"+flowEntry.type+"</td>\n"
			+ "<td width=\"80\" style=\"font-size:8px;\">"+flowEntry.subType+"</td>\n"
			+ "<td width=\"80\" style=\"font-size:8px;\">"+(flowEntry.state == 'Running' ? '<span class="label label-success">Running</span>' : (flowEntry.state == 'Completed' ? '<span class="label label-info">Completed</span>' : (flowEntry.state == 'Failed' ? '<span class="label label-danger">Failed</span>' : (flowEntry.state == 'Running Recovered' ? '<span class="label label-success">Recovered</span>' : (flowEntry.state == 'Completed Recovered' ? '<span class="label label-success">Recovered</span>' : (flowEntry.state == 'Suspended' ? '<span class="label label-warning">Suspended</span>' : (flowEntry.state == 'Resumed' ? '<span class="label label-success">Resumed</span>' : (flowEntry.state == 'Aborted' ? '<span class="label-default label">Aborted</span>' : (flowEntry.state == 'Stale' ? '<span class="label-default label">Stale</span>' : (flowEntry.state == 'Recovery Required' ? '<span class="label label-warning">Recovery Required</span>' : 'Unknown'))))))))))+"</td>\n"	// usage는 먼지 모름...?
			+ "<td width=\"180\" style=\"font-size:8px;\">"+flowEntry.date+"</td>\n"
			+ "<td width=\"180\" style=\"font-size:8px;\">"+flowEntry.compositeName+"</td>\n"
		    + "</tr>\n";
			
			$("#flowTraceInfo").append(html);
			
			if(childFlowEntries.length > 0) {
				jsonToHtml(depth + 1, childFlowEntries, html);
			}
			
			//alert(childFlowEntries.length);
		}
	}
	
	indent = function(depth) {
		var indent = "";
		
		for(var i = 0; i < depth * 4; i++) {
			indent += "&nbsp;"
		}
		
		return indent;
	}
	
	openFlowTrace = function(flowId) {
		var html = "";
		$('#flowTraceInfo').empty();
		$('#flowTrace').modal('show');
		
		$.ajax({
			url:"<spring:url value="/soa/flow/flowTrace" />?flowId="+flowId,
			success:function(data){
				var jsonObject = JSON.parse(data /* your original json data */);
				jsonToHtml(0, jsonObject, html);
				
				$("#flowTraceInfo").html(
				"<table style=\"table-layout:fixed;\" width=\"100%\" class=\"table table-condensed tree\" style=\"font-size:8px;\">\n"
				+ "<thead>\n"
				+ "<tr>\n"
				+ "<th>Instance</th>\n"
				+ "<th width=\"80\">Type</th>\n"
				+ "<th width=\"80\">Usage</th>\n"
				+ "<th width=\"80\">State</th>\n"
				+ "<th width=\"180\">Time</th>\n"
				+ "<th width=\"180\">Composite</th>\n"
				+ "</tr>\n"
				+ "</thead>\n"
				+ "<tbody>\n"
				+$("#flowTraceInfo").html()
				+ "</tbody>\n"
				+ "</table>\n");
				
				$('.tree').treegrid();
			},
			beforeSend: function() {
				$('#spin-auditflow').css("display", "");

			}, 
			error : function(request, status, error) {
				alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
			},
			complete : function() {
				$('#spin-auditflow').css("display", "none");
			}
		});
	}
	
	openAlterFlow = function(componentInstanceId)  {
		$('#alterFlowInfo').empty();
		$('#alterFlow').modal('show');
		
		$.ajax({
			url:"<spring:url value="/bpm/process/grab/info" />?instanceId="+componentInstanceId,
			success:function(data){
				$('#alterFlowInfo').append(data);
			},
			beforeSend: function() {
				$('#spin-alterflow').css("display", "");
			}, 
			error : function(request, status, error) {
				$('#alterFlow').modal('show');
				$('#alterFlowInfo').append(error);
			},
			complete : function() {
				$('#spin-alterflow').css("display", "none");
			}
		});
		
		//$('#ModalTracker').css("height", "550px");
		//ajax_jquery_loading("<spring:url value="/bpm/process/grab/info" />?instanceId="+componentInstanceId, "GET", "#alterFlowInfo","", "");
		//ajax_jquery_loading("<spring:url value="/bpm/instance/tracker" />?p_title="+encodeURIComponent(title)+"&p_processdn="+encodeURIComponent(processDn)+"&p_instanceid="+instanceId, "GET", "#faultDetailInfo", "");	
	}
	
</script>
<div class="modal" id="faultDetailAction" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h5>Fault Details and Recovery Options</h5>
			</div>
			<div class="modal-body" id="faultDetailInfo">
			Fault Detail...
			</div>
		</div>
	</div>
</div>

<div class="modal" id="flowTrace" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px; overflow:auto">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h5>Audit Trace</h5>
			</div>
			<div class="modal-body" style="height:520px;overflow-y:auto;overflow-x:hidden">
			<div id="spin-auditflow" style="position:relative;height:520px;top:50%;text-align:center;display:none"><img src='<c:url value='/resources/charisma-master/img/ajax-loaders/ajax-loader-7.gif'/>'/></div>
			<div id="flowTraceInfo"></div>
			<div style="text-align:center"><a href="#" class="btn btn-default btn-sm" data-dismiss="modal">Close</a></div>
			</div>
		</div>
	</div>
</div>

<div class="modal" id="alterFlow" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:800px;550px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h5>Alter Flow</h5>
			</div>
			<div class="modal-body" style="height:520px">
				<div id="spin-alterflow" style="position:relative;top:50%;text-align:center;display:none"><img src='<c:url value='/resources/charisma-master/img/ajax-loaders/ajax-loader-7.gif'/>'/></div>
			    <div id="alterFlowInfo"></div>
			</div>
		</div>
	</div>
</div>