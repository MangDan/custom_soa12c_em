<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--div class="alert alert-info" style="font-size:0.9em">Search Results for Employees <code>(${fn:length(result)} results)</code> and Query Time <code>(${response_time} sec)</code></div-->
<h6>Make changes and resume process</h6>
<div class="box col-md-9" style="padding:0px 0px 0px 0px">
	<div class="box-inner">
		<div class="box-header well" data-original-title="">
			<h2>Open Activities</h2>
		</div>
		<div class="box-content" style="height:150px;">
			<div style="height:130px;overflow:auto;">
				<table class="table table-bordered table-striped table-condensed" style="font-size:11px">
					<thead>
					<tr>
						<th>Current Activity</th>
						<th>New Activity</th>
						<th>Location</th>
					</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(requestScope.openActivities) > 0}">
								<c:set var="tc" value="0" />
								<c:forEach var="openActivityItem" begin="0" items="${requestScope.openActivities}" varStatus="ts">
						<tr>
							<input type="hidden" id="openActivitie_${ts.index}" name="openActivities" displayName="${openActivityItem.openActivity.displayName}" processId="${openActivityItem.openActivity.processId}" value="${openActivityItem.openActivity.id}"/>
							<td class="center" width="180">${openActivityItem.openActivity.displayName}</td>
							<td class="center" width="180">
							<select id="targetActivities_${ts.index}" name="targetActivities">
								<option value="">--Select Activity--</option>
								<c:choose>
									<c:when test="${fn:length(openActivityItem.targetActivities) > 0}">
										<c:forEach var="targetActivity" begin="0" items="${openActivityItem.targetActivities}">
											<option value="${targetActivity.id}" processId="${targetActivity.processId}" >${targetActivity.displayName}</option>
										</c:forEach>
									</c:when>
								</c:choose>
							</select>
							</td>
							<td class="center">${openActivityItem.openActivity.processId}</td>
						</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
							<tr>
							<td class="center" colspan="3">No Activity Changes Available</td>
							</tr>
							</c:otherwise>
						</c:choose>
						<!--tr>
							<input type="hidden" id="openActivitie_0" name="openActivities" displayName="test" value="test"/>
							<td class="center">test</td>
							<td class="center">
							<select id="targetActivities_0" name="targetActivities">
								<option value="">--Select Activity--</option>
								<option value="test">test</option>
							</select>
							</td>
							<td class="center">test</td>
						</tr-->
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="box col-md-3" style="padding:0px 0px 0px 10px">
	<div class="box-inner">
		<div class="box-header well" data-original-title="">
			<h2>Comments</h2>
		</div>
		<div class="box-content" style="height:150px;">
			<textarea id="alterFlowComment" style="width:160px; height:130px;"></textarea>
		</div>
	</div>
</div>
<p>
<div class="box col-md-6" style="padding:0px 0px 0px 0px">
	<div class="box-inner">
		<div class="box-header well" data-original-title="">
			<h2>Data Objects</h2>
		</div>
		<div id="availableVariables" class="box-content" style="height:150px;">
			<div style="height:130px;overflow:auto;">
				<table class="table table-bordered table-striped table-condensed" style="font-size:11px">
					<thead>
					<tr>
						<th>Name</th>
						<th>Scope</th>
					</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(requestScope.availableVariables) > 0}">
								<c:set var="tc" value="0" />
								<c:forEach var="availableVariable" begin="0" items="${requestScope.availableVariables}" varStatus="ts">
						<tr style="cursor:pointer;color:blue" onclick="showVariableDetail('${availableVariable.name}')">
							<td class="center">${availableVariable.name}</td>
							<td class="center">${requestScope.processName}</td>
						</tr>
								</c:forEach>
							</c:when>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="box col-md-6" style="padding:0px 0px 0px 10px">
	<div class="box-inner">
		<div class="box-header well" data-original-title="">
			<h2>Details</h2>
		</div>
		<div id="variableDetails" class="box-content" style="width:370px;height:150px;text-align:center;vertical-align:middle;display:table-cell;">
			<div id="defaultVariableDetails" style="display:">select a data object to see its details</div>
			<div id="variableDetail">
			<c:choose>
				<c:when test="${fn:length(requestScope.availableVariables) > 0}">
					<c:forEach var="availableVariable" begin="0" items="${requestScope.availableVariables}" varStatus="ts">
					<textarea id="${availableVariable.name}" dataType="${availableVariable.type}" contenteditable="true" style="font-size:12px;border:1px solid grey;text-align:left;width:348px; height:130px;display:none">${availableVariable.value}</textarea>
					</c:forEach>
				</c:when>
			</c:choose>
			</div>
		</div>
	</div>
</div>
<p>
<div style="text-align:right"><div id="refresh-recover-spin" class="box-icon" style="position:absolute;width:350px;text-align:right;display:none"><i class="fa fa-refresh fa-spin"></i></div><a href="#" id="resumeBtn" class="btn btn-primary btn-sm">Resume</a>&nbsp;<a href="#" id="saveBtn" class="btn btn-primary btn-sm">Save</a>&nbsp;<a href="#" class="btn btn-default btn-sm" data-dismiss="modal">Cancel</a></div>

<script type="text/javascript">
	var grabInfo;
	var activityArray;
	var variableArray;
	
	$(function() {
		$('#resumeBtn').click(function () {
			try {
				if(confirm("Are you sure you want to resume?")) {
					alterFlowInfo('resume');
				} else 
					return false;
				
			} catch (err) {
				alert(err);
				return false;
			}
			
			submitAlterFlow();
		});
		
		$('#saveBtn').click(function () {
			try {
				if(confirm("Are you sure you want to save?")) {
					alterFlowInfo('save');
				} else 
					return false;
			} catch (err) {
				alert(err);
				return false;
			}
			
			submitAlterFlow();
		});
	});
	
	submitAlterFlow = function() {
		var jsonInfo = JSON.stringify(grabInfo);
		if(Object.getOwnPropertyNames(grabInfo).length == 0)
			return false;
		
		//console.log(Object.getOwnPropertyNames(grabInfo).length);
		console.log(jsonInfo);
		$.ajax({
			type:"post",
			url:"<spring:url value="/bpm/process/grab/action" />",
			data:jsonInfo,
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			
			success:function(data){
				if(data.success == 'true')
					alert("Your request was processed successfully");
				else {
					alert(data.error.message);
				}
				$('#alterFlow').modal('hide');
			},
			beforeSend: function() {
				
			}, 
			error : function(request, status, error) {
				
			},
			complete : function() {
				
			}
		});
	};
	
	alterFlowInfo = function(mode) {
		grabInfo = new Object();
		activityArray = new Array();
		variableArray = new Array();

		// open activity 정보.
		
		$("select[name=targetActivities]").each(function(index){
			$(this).find("option:selected").each(function() {
				if(this.value != '') {
					
					var openActivity = $("input[name=openActivities]:eq("+index+")");
					openActivityMap = new Object();
					
					openActivityMap.openActivityId = $(openActivity).val();
					openActivityMap.openActivityName = $(openActivity).attr("displayName");
					openActivityMap.openActivityProcessId = $(openActivity).attr("processId");	// todo
					openActivityMap.targetActivityId = $(this).val();
					openActivityMap.targetActivityName = $(this).text();
					openActivityMap.targetActivityProcessId = $(this).attr("processId");	//todo
					//alert(openActivityMap.openActivityId+":"+openActivityMap.openActivityName+":"+openActivityMap.targetActivityId+":"+openActivityMap.targetActivityName);
					activityArray.push(openActivityMap);
				}
			});
		});
		
		if(activityArray.length == 0)
			throw "please select one or more target activities!";
		
		
		// variable data
		$("#variableDetail").find("textarea").each(function(){
			if(this.value != this.defaultValue) {	// value has been changed.
				variableMap = new Object();
				variableMap.name = this.id;
				variableMap.value = this.value;
				
				try {
					// XMLSchema prebuilt type (no complex type)
					// prebuilt type은 xml이 아니라고 가정한다. (꼭 그렇지만은 않겠지만, 거의 대부분 단순 타입)
					if($(this).attr("dataType").indexOf('http://www.w3.org/2001/XMLSchema') == -1) {
						$.parseXML(this.value);	// for xml validation
					}
				} catch (err) {
					throw err;
				}

				variableArray.push(variableMap);
			}
		});
		
		grabInfo.instanceId = "${param.instanceId}"; // instanceId 값 매핑	todo
		grabInfo.comment = $("#alterFlowComment").val(); // comment 값 매핑	todo
		grabInfo.mode = mode; // mode 값 매핑	todo
		grabInfo.openActivities = activityArray;
		grabInfo.changeVariables = variableArray;
		
		//var jsonInfo = JSON.stringify(grabInfo);
		//console.log(jsonInfo); //브라우저 f12개발자 모드에서 confole로 확인 가능
	};
	
	showVariableDetail = function(name) {
		//var curTextArea = $("#"+name);
		//var myCodeMirror = CodeMirror.fromTextArea(curTextArea);
		
		//alert(($("#sampleDO").val()));
		$("#defaultVariableDetails").css("display","none");
		$("#variableDetail").find("textarea").css("display","none");
		$("#"+name).css("display", "");
		
	};
</script>