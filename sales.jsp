<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Jsp/Servlet必須課題1</title>
</head>
<body>
	<table border="1">
		<tr>
			<th>従業員名</th>
			<th>売上金額</th>
		</tr>
		<c:forEach var="list" items="${salesList}">
			<tr>
				<td>${list.getEmployeeName()}</td>
				<td>${list.getSalesSum()}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>