<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!--div class="alert alert-info" style="font-size:0.9em">Search Results for Employees <code>(${fn:length(result)} results)</code> and Query Time <code>(${response_time} sec)</code></div-->
<div style="height:180px;overflow:auto;">
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
		<c:choose>
			<c:when test="${fn:length(requestScope.results) > 0}">
				<c:set var="tc" value="0" />
				<c:forEach var="faultAll" begin="0" items="${requestScope.results}" varStatus="ts">
		<tr>
			<td class="center">${faultAll.cfault.faultName}</textarea></td>
			<td class="center">${faultAll.cfault.componentName} : ${faultAll.cfault.ownerScaEntityType}</td>
			<td class="center">${faultAll.cfault.faultTime}</td>
			<td class="center"><c:choose><c:when test="${fn:contains(faultAll.recoveryState, 'RECOVERY_REQUIRED')}"><a href="javascript:openFaultDetail('${faultAll.cfault.flowId}','${faultAll.cfault.faultId}')">${faultAll.recoveryState}</a></c:when><c:otherwise>${faultAll.recoveryState}</c:otherwise></c:choose> (${faultAll.cfault.retryCount} attepmted)</td>
		</tr>
				<c:set var="tc" value="${ts.index}" />
				</c:forEach>
			</c:when>
			<c:otherwise>
		<tr>
			<td colspan="4">NO DATA...</td>
		</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
</div>