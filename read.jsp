<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>参照画面</title>
</head>
<body>
	<!-- サーブレットで設定した社員の氏名を表示する -->
	<!-- <br> -->
	<!-- employeeBeanのデータを読み込んで表示する(スコープの指定は省略しているので注意) -->
	<!--<c:out value="${empInfo.employeeName}" />-->
	
	<!-- empListのデータを読み込んで表示する -->
	<c:forEach var="list" items="${empList}"><br />
		<form action="Update" method="post">
		${list.employeeId}:${list.employeeName}<br />
		Eメール:${list.email}<br />
		<input type="hidden" name="employeeID" value="${list.employeeId}"/>
		<input type="submit" value="更新画面へ"/>
		<br /><br />
		</form>
	</c:forEach>
</body>
</html>