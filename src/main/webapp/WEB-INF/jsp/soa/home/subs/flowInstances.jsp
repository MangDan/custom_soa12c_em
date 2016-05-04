<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--div class="alert alert-info" style="font-size:0.9em">Search Results for Employees <code>(${fn:length(result)} results)</code> and Query Time <code>(${response_time} sec)</code></div-->
<div style="height:180px;overflow:auto;">
<div style="padding:0px 0px 5px 0px;text-align:right"><strong>Flow ID</strong> : <input type="text" id="SearchFlowId" name="SearchFlowId" value="${param.flowId}"></input>&nbsp;<button id="composite-search-btn" class="btn btn-inverse btn-default btn-xs">SEARCH</button>&nbsp;</div>
<table class="table table-bordered table-striped table-condensed" style="font-size:11px">
	<thead>
	<tr>
		<th>Flow ID</th>
		<th>Initiating Composite</th>
		<th>Recoverable</th>
		<th>Name</th>
		<th>Created</th>
		<th>Last Updated</th>
		<th>Partition</th>
	</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${fn:length(requestScope.result) > 0}">
				<c:set var="tc" value="0" />
				<c:forEach var="flowInstance" begin="0" items="${requestScope.result}" varStatus="ts">
		<tr>
			<td class="center"><a href="javascript:openFlowTrace('${flowInstance.flowId}')">${flowInstance.flowId}</a></td>
			<td class="center">${fn:substringAfter(flowInstance.initiatingCompositeDN, "/")}</td>
			<td class="center" style="cursor:pointer;" onClick="javascript:getFaults(${flowInstance.flowId})">${flowInstance.adminState == '1' ? '<span class="label label-warning">Suspended</span>' : (flowInstance.adminState == '2' ? '<span class="label-default label">Aborted</span>' : (flowInstance.adminState == '3' ? '<span class="label">Stale</span>' : (flowInstance.adminState == '-1' ? (flowInstance.recoverableFaults > 0 ? '<span class="label label-danger">Recovery</span>' : (flowInstance.activeComponentInstances > 0 ? '<span class="label label-success">Running</span>' : (flowInstance.unhandledFaults > 0 ? '<span class="label label-danger">Faulted</span>' : '<span class="label label-info">Completed</span>'))) : '<span class="label label-danger">Invalid Value</span>')))}</td>
			<td class="center">${flowInstance.title}</td>
			<td class="center">${flowInstance.createdTime}</td>
			<td class="center">${flowInstance.updatedTime}</td>
			<td class="center">${fn:substringBefore(flowInstance.initiatingCompositeDN, "/")}</td>
		</tr>
				<c:set var="tc" value="${ts.index}" />
				</c:forEach>
			</c:when>
			<c:otherwise>
		<tr>
			<td colspan="9">NO DATA...</td>
		</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
</div>
<script type="text/javascript">
	$(function() {
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
	});
</script>