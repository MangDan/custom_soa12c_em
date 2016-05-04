<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head></head>
<body>
Hello ${username}!<br>
<a href="<%= request.getContextPath() %>/j_spring_security_logout">[로그아웃]</a>
</body>
</html>