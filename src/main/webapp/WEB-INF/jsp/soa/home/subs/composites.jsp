<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--div class="alert alert-info" style="font-size:0.9em">Search Results for Employees <code>(${fn:length(result)} results)</code> and Query Time <code>(${response_time} sec)</code></div-->
<div style="height:180px;overflow:auto;">
<div style="padding:0px 0px 5px 0px;text-align:right"><strong>Flow ID</strong> : <input type="text" id="SearchFlowId" name="SearchFlowId" value="${param.flowId}"></input>&nbsp;<button id="composite-search-btn" class="btn btn-inverse btn-default btn-xs">SEARCH</button>&nbsp;</div>
<table class="table table-bordered table-striped table-condensed" style="font-size:11px">
	<thead>
	<tr>
		<th>Flow ID</th>
		<th>Initiating Composite</th>
		<th>Flow State</th>
		<th>Created</th>
		<th>Last Updated</th>
		<th>Partition</th>
	</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${fn:length(requestScope.result) > 0}">
				<c:set var="tc" value="0" />
				<c:forEach var="compositeInstance" begin="0" items="${requestScope.result}" varStatus="ts">
		<tr>
			<td class="center"><a href="javascript:getFaults(${compositeInstance.flowId})">${compositeInstance.flowId}</a></td>
			<td class="center">${fn:substringAfter(compositeInstance.compositeDN, "/")}</td>
			<td class="center"><c:choose><c:when test="${compositeInstance.state == 0}"><span class="label label-success">Running</span></c:when><c:when test="${compositeInstance.state == 1}"><span class="label label-warning">Recovery</span></c:when><c:when test="${compositeInstance.state == 2}"><span class="label label-success">Completed</span></c:when><c:when test="${compositeInstance.state == 3}"><span class="label label-danger">Faulted</span></c:when><c:when test="${compositeInstance.state == 4}"><span class="label label-info">Aborted</span></c:when><c:when test="${compositeInstance.state == 6}"><span class="label">Stale</span></c:when><c:otherwise>Unknown</c:otherwise></c:choose></td>
			<td class="center">${compositeInstance.creationDate}</td>
			<td class="center">${compositeInstance.modifyDate}</td>
			<td class="center">${fn:substringBefore(compositeInstance.compositeDN, "/")}</td>
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