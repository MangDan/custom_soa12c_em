<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
	<c:if test="${!empty requestScope.acknowledgeMsg}">
		alert('<c:out value="${requestScope.acknowledgeMsg}"/>');
	</c:if>
	<c:if test="${!empty requestScope.reaction}">
		<c:out value="${requestScope.reaction}"/>
	</c:if>
	<c:choose>
		<c:when test="${requestScope.redirecturi eq 'none'}">
		</c:when>
		<c:when test="${!empty requestScope.redirecturi}">
			location.replace('<c:out value="${requestScope.redirecturi}"/>'); 
		</c:when>
		<c:otherwise>
			window.close();
		</c:otherwise>
	</c:choose>
</script>