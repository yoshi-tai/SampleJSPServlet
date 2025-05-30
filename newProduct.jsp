<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Jsp/Servlet必須課題2</title>
<style>.botan{margin-left: 100px;}</style>
</head>
<body>
	<h2>商品登録画面</h2>
	<form action="newProduct" method="post">
		<table>
    		<tr>
    			<td>商品ID</td>
    			<td><input type= "text" name="ID"  disabled ></td>
    		</tr>
    		<tr>
    			<td>商品コード</td>
    			<td><input type= "text" name="CODE" value="${CODEKEY}">
    			<%
       			 String error1 = (String) request.getAttribute("errorMessage1");
       				 if (error1 != null) {
   				 %>
    			    <td><span style="font-size: 8px; "><%= error1 %></span></td>
    			<%
      				  }
   				 %>
   				 </td>
    		</tr>
    		<tr>
    			<td>商品名</td>
    			<td><input type= "text" name="NAME" value="${NAMEKEY}"></td>
    		</tr>
    		<tr>
    			<td>価格</td>
    			<td><input type= "text" name="PRICE" value="${PRICEKEY}"></td>
    			<%
       			 String error2 = (String) request.getAttribute("errorMessage2");
       				 if (error2 != null) {
   				 %>
    			    <td><span style="font-size: 8px; "><%= error2 %></span></td>
    			<%
      				  }
   				 %>
    		</tr>
    		<tr>
    			<td>カテゴリー</td>
    			<td>
    				<select name="CATEGORYID">
    					<c:forEach var="list" items="${productList}">
    <option value="${list.CATEGORYID}" 
        <c:if test="${list.CATEGORYID == CATEGORY}">selected</c:if>>
        ${list.CATEGORYNAME}
    </option>
</c:forEach>

    				</select>
    			</td>
    		</tr>
    	</table>
    		<input type="submit" value="登録" class="botan" />
    		
    		<input type="hidden" name="taiga" value="taiga" />
    		 
    </form>
	<p>${MSGKEY}</p>
</body>
</html>

