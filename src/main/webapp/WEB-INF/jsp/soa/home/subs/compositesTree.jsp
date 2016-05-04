<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- left menu starts -->
<div class="sidebar-nav">
	<div class="nav-canvas">
		<div class="nav-sm nav nav-stacked"></div>
		<ul class="nav nav-pills nav-stacked main-menu">
			<li class="nav-header">SOA-INFRA</li>
			<c:choose>
				<c:when test="${fn:length(requestScope.partitions) > 0}">
				<c:forEach var="partition" begin="0" items="${requestScope.partitions}" varStatus="ts">
				<li class="accordion">
				<a href="#"><i class="glyphicon glyphicon-plus"></i><span> <small>${partition.name}</small></span></a>
				<ul class="nav nav-pills nav-stacked">
				<c:choose>
					<c:when test="${fn:length(requestScope.results) > 0}">
					<c:forEach var="composites" begin="0" items="${requestScope.results}" varStatus="ts">
					<c:choose>
						<c:when test="${fn:length(composites[partition.name]) > 0}">
						<c:forEach var="composite" begin="0" items="${composites[partition.name]}" varStatus="ts">
						<li><a href="#"><small>${composite.compositeName} (${composite.revision})</small></a></li>
						</c:forEach>
						</c:when>
					</c:choose>
					</c:forEach>
					</c:when>
				</c:choose>
				</ul>
				</li>
				</c:forEach>
				</c:when>
			</c:choose>
		</ul>
	</div>
</div>
<!--/span-->
<!-- left menu ends -->