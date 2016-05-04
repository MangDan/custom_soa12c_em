<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--div class="alert alert-info" style="font-size:0.9em">Search Results for Employees <code>(${fn:length(result)} results)</code> and Query Time <code>(${response_time} sec)</code></div-->
<h6>BPEL System Fault</h6>
<p>
<table class="table table-striped table-condensed" style="font-size:11px">
	<tr>
		<td width="100" height="10">Fault Time</small></td>
		<td>${requestScope.results.cfault.faultTime}</td>
	</tr>
	<tr>
		<td width="100">Fault Location</small></td>
		<td>${fn:substringAfter(requestScope.results.cfault.compositeDN, "/")} : ${requestScope.results.cfault.componentName}</td>
	</tr>
	<tr>
		<td width="100">Fault Owner</small></td>
		<td>${fn:substringAfter(requestScope.results.cfault.compositeDN, "/")} : ${requestScope.results.cfault.componentName} : ${requestScope.results.cfault.internalId}</td>
	</tr>
	<tr>
		<td width="100">Recovery Type</small></td>
		<td>${requestScope.results.recoveryState}</td>
	</tr>
	<tr>
		<td width="100">Retry Count</small></td>
		<td>${requestScope.results.cfault.retryCount}</td>
	</tr>
	<tr>
		<td width="100">Error Message</small></td>
		<td><textarea style="width:450px;height:80px">${requestScope.results.cfault.message}</textarea></td>
	</tr>
</table>
</p>
<c:choose>
	<c:when test="${fn:length(requestScope.results.variableNames) > 0}">
	<h6>Recovery Options</h6>
	<table class="table table-striped table-condensed" style="font-size:11px">
		<tr>
			<td width="100">Recovery Action</td>
			<td>
			<select id="recoveryActions" style="width:150px">
				<option value="">Select</option>
				<option value="ACTION_RETRY">Retry</option>
				<option value="ACTION_CONTINUE">Continue</option>
				<option value="ACTION_RETHROW_FAULT">Rethrow</option>
				<option value="ACTION_REPLAY_SCOPE">Replay</option>
			</select>
			</td>
		</tr>
		<tr>
			<td width="100">Variable</td>
			<td>
			<select id="variableNames" style="width:150px">
				<option value="">Select</option>
				<c:choose>
					<c:when test="${fn:length(requestScope.results.variableNames) > 0}">
						<c:forEach var="variableName" begin="0" items="${requestScope.results.variableNames}" varStatus="ts">
						<option value="${variableName}">${variableName}</option>
						</c:forEach>
					</c:when>
				</c:choose>
			</select>
			</td>
		</tr>
		<tr>
			<td width="100">Value</td>
			<td>
			<textarea id="variableValue" style="width:450px;height:80px"></textarea>
			</td>
		</tr>
	</table>
	</c:when>
</c:choose>

<div style="text-align:right"><div id="refresh-recover-spin" class="box-icon" style="position:absolute;width:350px;text-align:right;display:none"><i class="fa fa-refresh fa-spin"></i></div><a href="#" id="recoverBtn" class="btn btn-primary btn-sm">Recover</a>&nbsp;<a href="#" id="abortBtn" class="btn btn-primary btn-sm">Abort</a>&nbsp;<a href="#" class="btn btn-default btn-sm" data-dismiss="modal">Cancel</a></div>
<c:choose>
	<c:when test="${fn:length(requestScope.results.variableNames) > 0}">
		<c:forEach var="variableName" begin="0" items="${requestScope.results.variableNames}" varStatus="ts">
		<textarea id="${variableName}" style="display:none">${requestScope.results[variableName]}</textarea>
		</c:forEach>
	</c:when>
</c:choose>

<script type="text/javascript">
	$(function() {
		$('#recoveryActions').change(function(){
			var action = $("#recoveryActions option:selected").val();
		});
		
		$('#variableNames').change(function(){
			var varName = $("#variableNames option:selected").val();
			var variableValue = $("#"+varName).val();
			$("#variableValue").val(variableValue);
		});
		
		$('#recoverBtn').click(function(){
			var flowId = "${param.flowId}";
			var faultId = "${param.faultId}";
			var faultType = "${requestScope.results.cfault.faultType}";
			var faultAction = $("#recoveryActions option:selected").val();
			var varName = $("#variableNames option:selected").val();
			var varValue = $("#variableValue").val();
			
			if(faultAction == undefined) {
				faultAction = "ACTION_RETRY";
			}
			
			if(varName == undefined) {
				varName = "";
			}
			
			if(varValue == undefined) {
				varValue = "";
			}
			
			//if(faultType == 'business' && faultAction == '') {
			//	alert("please select a Recovery Action!!");
			//	return;
			//}
			
			$.ajax({
				url:"<spring:url value="/soa/flow/faultRecover" />?flowId="+flowId+"&faultId="+faultId+"&faultAction="+faultAction+"&variableName="+varName+"&variableValue="+varValue,
				success:function(data){
					//getFaults('${param.flowId}');
				},
				beforeSend: function() {
					$("#recoverBtn").prop("disabled",true);
					$("#refresh-recover-spin").css('display', '');
				}, 
				error : function(request, status, error) {
					alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
				},
				complete : function() {
					$("#recoverBtn").prop("disabled",false);
					$("#refresh-recover-spin").css('display', 'none');
					getFaults('${param.flowId}');
					$('#faultDetailInfo').empty();
					$('#faultDetailAction').modal('hide');
				}
			});
		});
		

		$('#abortBtn').click(function(){
			var flowId = "${param.flowId}";
			
			$.ajax({
				url:"<spring:url value="/soa/flow/abortFlow" />?flowId="+flowId,
				success:function(data){
					//getFaults('${param.flowId}');
				},
				beforeSend: function() {
					$("#refresh-recover-spin").css('display', '');
				}, 
				error : function(request, status, error) {
					alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
				},
				complete : function() {
					$("#refresh-recover-spin").css('display', 'none');
					getFaults('${param.flowId}');
					$('#faultDetailInfo').empty();
					$('#faultDetailAction').modal('hide');
				}
			});
			
		});
	});
</script>